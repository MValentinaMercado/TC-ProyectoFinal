package com.compilador.optimizacion;
import com.compilador.codigoIntermedio.CodigoTresDir;
import com.compilador.codigoIntermedio.Instruccion;
import java.io.PrintWriter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Optimizador de código intermedio (TAC - Three Address Code).
 *
 * Opera internamente sobre la representación String de cada instrucción
 * (obtenida vía Instruccion.toString()) y usa el tipo Instruccion.Tipo
 * para clasificar cada línea.
 *
 * Optimizaciones aplicadas (en orden, hasta punto fijo):
 *   1. Propagación de constantes
 *   2. Simplificación de expresiones (constant folding)
 *   3. Eliminación de código muerto
 *   4. Eliminación de temporales muertos
 *
 * Si se proporciona un nombreBase, guarda un archivo por pasada:
 *   {nombreBase}_optimizacion_pasada_N.txt
 */
public class Optimizador {
    // =========================================================================
    // REPRESENTACIÓN INTERNA
    // =========================================================================
    /**
     * Vista parseada de una instrucción TAC.
     * Se construye una sola vez desde Instruccion.toString().
     */
    private static class TacLine {
        final Instruccion.Tipo tipo;
        final String raw;        // texto original (de toString())
        // Campos semánticos extraídos del texto
        String dest;   // lado izquierdo / etiqueta / nombre función
        String arg1;   // primer operando / condición / tipo
        String op;     // operador binario/unario
        String arg2;   // segundo operando / índice array / size array
        TacLine(Instruccion.Tipo tipo, String raw) {
            this.tipo = tipo;
            this.raw  = raw;
        }
        /** Reconstruye la instrucción como Instruccion a partir de los campos semánticos. */
        Instruccion toInstruccion() {
            switch (tipo) {
                case COMENTARIO:         return Instruccion.comentario(dest);
                case ETIQUETA:           return Instruccion.etiqueta(dest);
                case DECLARE:            return Instruccion.declare(dest, arg1);
                case DECLARE_ARRAY:      return Instruccion.declareArray(dest, arg2, arg1);
                case PARAM_DEF:          return Instruccion.paramDef(dest, arg1);
                case ASIGNAR:            return Instruccion.asignar(dest, arg1);
                case ASIGNAR_ARRAY_W:    return Instruccion.asignarArrayW(dest, arg1, arg2);
                case ASIGNAR_ARRAY_R:    return Instruccion.asignarArrayR(dest, arg1, arg2);
                case BINARIA:            return Instruccion.binaria(dest, arg1, op, arg2);
                case UNARIA:             return Instruccion.unaria(dest, op, arg1);
                case SALTO_INCOND:       return Instruccion.saltoIncond(dest);
                case SALTO_COND_VERDAD:  return Instruccion.saltoCondVerdad(arg1, dest);
                case CALL:               return Instruccion.call(dest, arg1 != null ? arg1 : "");
                case RETURN_VALUE_ASSIGN:return Instruccion.returnValueAssign(dest);
                case RETORNO:            return Instruccion.retorno(arg1);
                default:                 return Instruccion.comentario("???");
            }
        }
        @Override public String toString() { return toInstruccion().toString(); }
    }
    // =========================================================================
    // PARSERS DE CADA FORMATO (basados en Instruccion.toString())
    // =========================================================================
    // Patrones compilados una sola vez
    private static final Pattern P_COMMENT     = Pattern.compile("^// (.*)$");
    private static final Pattern P_LABEL       = Pattern.compile("^(\\w+):$");
    private static final Pattern P_DECLARE     = Pattern.compile("^DECLARE (\\w+) (\\w+)$");
    private static final Pattern P_DECLARE_ARR = Pattern.compile("^DECLARE (\\w+)\\[(\\w+)] (\\w+)$");
    private static final Pattern P_PARAM       = Pattern.compile("^PARAM (\\w+) (\\w+)$");
    private static final Pattern P_RET_ASSIGN  = Pattern.compile("^(\\w+) = RETURN_VALUE$");
    // arr[idx] = val
    private static final Pattern P_ARR_W       = Pattern.compile("^(\\w+)\\[(\\w+)] = (.+)$");
    // dest = arr[idx]
    private static final Pattern P_ARR_R       = Pattern.compile("^(\\w+) = (\\w+)\\[(\\w+)]$");
    // dest = op operand  (unaria)
    private static final Pattern P_UNARIA      = Pattern.compile("^(\\w+) = ([!\\-])(\\w+)$");
    // dest = a op b  (binaria) — op puede ser ==, !=, <=, >=, <, >, +, -, *, /, %, &&, ||
    private static final Pattern P_BINARIA     = Pattern.compile(
            "^(\\w+) = (\\S+) (==|!=|<=|>=|<|>|\\+|-|\\*|/|%|&&|\\|\\|) (\\S+)$");
    private static final Pattern P_ASIGNAR     = Pattern.compile("^(\\w+) = (.+)$");
    private static final Pattern P_GOTO        = Pattern.compile("^goto (\\w+)$");
    private static final Pattern P_IF_GOTO     = Pattern.compile("^if (\\S+) goto (\\w+)$");
    // CALL func  o  CALL func, args
    private static final Pattern P_CALL        = Pattern.compile("^CALL (\\w+)(?:, (.+))?$");
    private static final Pattern P_RETURN_VAL  = Pattern.compile("^return (.+)$");
    /**
     * Parsea una instrucción desde su representación textual (Instruccion.toString()).
     * Devuelve un TacLine con tipo y campos semánticos rellenos.
     */
    private static TacLine parsear(Instruccion instr) {
        String raw = instr.toString();
        Instruccion.Tipo tipo = instr.getTipo();
        TacLine t = new TacLine(tipo, raw);
        Matcher m;
        switch (tipo) {
            case COMENTARIO:
                m = P_COMMENT.matcher(raw);
                t.dest = m.matches() ? m.group(1) : raw.substring(3);
                break;
            case ETIQUETA:
                m = P_LABEL.matcher(raw);
                t.dest = m.matches() ? m.group(1) : raw.replace(":", "");
                break;
            case DECLARE:
                m = P_DECLARE.matcher(raw);
                if (m.matches()) { t.dest = m.group(1); t.arg1 = m.group(2); }
                break;
            case DECLARE_ARRAY:
                m = P_DECLARE_ARR.matcher(raw);
                if (m.matches()) { t.dest = m.group(1); t.arg2 = m.group(2); t.arg1 = m.group(3); }
                break;
            case PARAM_DEF:
                m = P_PARAM.matcher(raw);
                if (m.matches()) { t.dest = m.group(1); t.arg1 = m.group(2); }
                break;
            case RETURN_VALUE_ASSIGN:
                m = P_RET_ASSIGN.matcher(raw);
                t.dest = m.matches() ? m.group(1) : raw.split(" ")[0];
                break;
            case ASIGNAR_ARRAY_W:
                m = P_ARR_W.matcher(raw);
                if (m.matches()) { t.dest = m.group(1); t.arg1 = m.group(2); t.arg2 = m.group(3); }
                break;
            case ASIGNAR_ARRAY_R:
                m = P_ARR_R.matcher(raw);
                if (m.matches()) { t.dest = m.group(1); t.arg1 = m.group(2); t.arg2 = m.group(3); }
                break;
            case UNARIA:
                m = P_UNARIA.matcher(raw);
                if (m.matches()) { t.dest = m.group(1); t.op = m.group(2); t.arg1 = m.group(3); }
                break;
            case BINARIA:
                m = P_BINARIA.matcher(raw);
                if (m.matches()) {
                    t.dest = m.group(1); t.arg1 = m.group(2);
                    t.op   = m.group(3); t.arg2 = m.group(4);
                }
                break;
            case ASIGNAR:
                m = P_ASIGNAR.matcher(raw);
                if (m.matches()) { t.dest = m.group(1); t.arg1 = m.group(2); }
                break;
            case SALTO_INCOND:
                m = P_GOTO.matcher(raw);
                t.dest = m.matches() ? m.group(1) : raw.replace("goto ", "");
                break;
            case SALTO_COND_VERDAD:
                m = P_IF_GOTO.matcher(raw);
                if (m.matches()) { t.arg1 = m.group(1); t.dest = m.group(2); }
                break;
            case CALL:
                m = P_CALL.matcher(raw);
                if (m.matches()) { t.dest = m.group(1); t.arg1 = m.group(2); }
                break;
            case RETORNO:
                m = P_RETURN_VAL.matcher(raw);
                if (m.matches()) { t.arg1 = m.group(1); }
                // si es "return" solo, arg1 queda null
                break;
            default:
                break;
        }
        return t;
    }
    // =========================================================================
    // ESTADO
    // =========================================================================
    private final List<Instruccion> original;
    private List<TacLine>           trabajo;
    private int eliminadas = 0;
    private final Set<String>       globales;
    // =========================================================================
    // CONSTRUCTOR
    // =========================================================================
    public Optimizador(List<Instruccion> instrucciones) {
        List<Instruccion> copia = instrucciones == null ? Collections.emptyList() : new ArrayList<>(instrucciones);
        this.original  = Collections.unmodifiableList(copia);
        this.trabajo   = parsearTodas(copia);
        this.globales  = recolectarGlobales(this.trabajo);
    }
    /** Atajo para optimizar directamente la salida de CodigoTresDir sin modificarlo. */
    public Optimizador(CodigoTresDir generador) {
        this(generador != null ? generador.getInstrucciones() : Collections.emptyList());
    }
    private static List<TacLine> parsearTodas(List<Instruccion> lista) {
        List<TacLine> r = new ArrayList<>(lista.size());
        for (Instruccion i : lista) r.add(parsear(i));
        return r;
    }
    // =========================================================================
    // PUNTO DE ENTRADA
    // =========================================================================
    /** Optimiza sin guardar archivos por pasada. */
    public List<Instruccion> optimizar() {
        return optimizar(null);
    }
    /**
     * Optimiza hasta punto fijo.
     * Si nombreBase != null, guarda {nombreBase}_optimizacion_pasada_N.txt por cada iteración.
     */
    public List<Instruccion> optimizar(String nombreBase) {
        int pasada = 1;
        int antesSize;
        do {
            antesSize = trabajo.size();
            propagarConstantes();
            simplificarExpresiones();
            eliminarCodigoMuerto();
            eliminarTemporalesMuertos();
            if (nombreBase != null) {
                guardarPasada(nombreBase, pasada);
            }
            pasada++;
        } while (trabajo.size() != antesSize);
        eliminadas = original.size() - trabajo.size();
        return reconstruir();
    }
    // =========================================================================
    // GUARDAR ARCHIVO POR PASADA
    // =========================================================================
    private void guardarPasada(String nombreBase, int pasada) {
        String archivo = nombreBase + "_optimizacion_pasada_" + pasada + ".txt";
        try (PrintWriter pw = new PrintWriter(archivo, "UTF-8")) {
            pw.println("// Codigo optimizado - Pasada " + pasada);
            pw.printf( "// Instrucciones en esta pasada: %d%n", trabajo.size());
            pw.println();
            int n = 0;
            for (TacLine t : trabajo) {
                pw.printf("%2d: %s%n", n++, t.toString());
            }
            System.out.println("   Pasada " + pasada + " guardada en: " + archivo);
        } catch (Exception e) {
            System.out.println("   Error guardando pasada " + pasada + ": " + e.getMessage());
        }
    }
    // =========================================================================
    // GETTERS DE ESTADÍSTICAS
    // =========================================================================
    public int getCantidadOriginal()    { return original.size(); }
    public int getCantidadOptimizada()  { return trabajo.size(); }
    public int getCantidadEliminadas()  { return eliminadas; }
    public double getPorcentajeReduccion() {
        if (original.isEmpty()) return 0.0;
        return (eliminadas * 100.0) / original.size();
    }
    // =========================================================================
    // 1. PROPAGACIÓN DE CONSTANTES
    // =========================================================================
    private void propagarConstantes() {
        Map<String, String> sustituciones = new HashMap<>();
        List<TacLine> res = new ArrayList<>();
        for (TacLine t : trabajo) {
            switch (t.tipo) {
                case COMENTARIO:
                case DECLARE:
                case DECLARE_ARRAY:
                case PARAM_DEF:
                    res.add(t);
                    break;
                case ETIQUETA:
                case SALTO_INCOND:
                    sustituciones.clear();
                    res.add(t);
                    break;
                case CALL:
                    // FIX 2: solo sustituir temporales en argumentos de CALL,
                    // no variables con nombre semántico real
                    t.arg1 = sustituirArgsCall(t.arg1, sustituciones);
                    res.add(t);
                    sustituciones.clear();
                    break;
                case ASIGNAR: {
                    String dest = t.dest, src = t.arg1;
                    String val = esLiteral(src) ? src : resolverAlias(src, sustituciones);
                    t.arg1 = val;
                    res.add(t);
                    if (val != null) sustituciones.put(dest, val);
                    else sustituciones.remove(dest);
                    break;
                }
                case BINARIA: {
                    String dest = t.dest;
                    String a  = resolver(t.arg1, sustituciones);
                    String b  = resolver(t.arg2, sustituciones);
                    String valor = calcularBinaria(a, t.op, b);
                    if (valor != null) {
                        res.add(hacerAsignar(dest, valor));
                    } else {
                        t.arg1 = a;
                        t.arg2 = b;
                        res.add(t);
                    }
                    sustituciones.remove(dest);
                    break;
                }
                case UNARIA: {
                    String arg = resolver(t.arg1, sustituciones);
                    String valor = calcularUnaria(t.op, arg);
                    if (valor != null) {
                        res.add(hacerAsignar(t.dest, valor));
                    } else {
                        t.arg1 = arg;
                        res.add(t);
                    }
                    sustituciones.remove(t.dest);
                    break;
                }
                case SALTO_COND_VERDAD: {
                    t.arg1 = resolver(t.arg1, sustituciones);
                    res.add(t);
                    break;
                }
                case RETORNO:
                    res.add(t);
                    break;
                case ASIGNAR_ARRAY_W:
                    t.arg1 = resolver(t.arg1, sustituciones);
                    t.arg2 = resolver(t.arg2, sustituciones);
                    res.add(t);
                    break;
                case ASIGNAR_ARRAY_R:
                    t.arg1 = resolver(t.arg1, sustituciones);
                    t.arg2 = resolver(t.arg2, sustituciones);
                    res.add(t);
                    sustituciones.remove(t.dest);
                    break;
                case RETURN_VALUE_ASSIGN:
                    sustituciones.remove(t.dest);
                    res.add(t);
                    break;
                default:
                    invalidarDest(t, sustituciones);
                    res.add(t);
                    break;
            }
        }
        trabajo = res;
    }
    /** Invalida del mapa de constantes el destino de instrucciones que asignan sin literal. */
    private void invalidarDest(TacLine t, Map<String, String> constantes) {
        switch (t.tipo) {
            case RETURN_VALUE_ASSIGN:
                if (t.dest != null) constantes.remove(t.dest);
                break;
            case ASIGNAR_ARRAY_R:
                if (t.dest != null) constantes.remove(t.dest);
                break;
            case UNARIA:
                if (t.dest != null) constantes.remove(t.dest);
                break;
            default:
                break;
        }
    }
    // =========================================================================
    // 2. SIMPLIFICACIÓN DE EXPRESIONES (Constant Folding)
    // =========================================================================
    private void simplificarExpresiones() {
        List<TacLine> res = new ArrayList<>();
        for (TacLine t : trabajo) {
            if (t.tipo == Instruccion.Tipo.BINARIA
                    && esLiteralSimple(t.arg1)
                    && esLiteralSimple(t.arg2)) {
                String valor = calcularBinaria(t.arg1, t.op, t.arg2);
                if (valor != null) {
                    res.add(hacerAsignar(t.dest, valor));
                    continue;
                }
            } else if (t.tipo == Instruccion.Tipo.UNARIA && esLiteralSimple(t.arg1)) {
                String valor = calcularUnaria(t.op, t.arg1);
                if (valor != null) {
                    res.add(hacerAsignar(t.dest, valor));
                    continue;
                }
            }
            res.add(t);
        }
        trabajo = res;
    }
    // =========================================================================
    // 3. ELIMINACIÓN DE CÓDIGO MUERTO
    // =========================================================================
    private void eliminarCodigoMuerto() {
        // Paso a: x = x
        List<TacLine> paso1 = new ArrayList<>();
        for (TacLine t : trabajo) {
            if (t.tipo == Instruccion.Tipo.ASIGNAR
                    && t.dest != null && t.dest.equals(t.arg1)) continue;
            paso1.add(t);
        }
        // Recolectar etiquetas destino de saltos
        Set<String> etiquetasUsadas = new HashSet<>();
        for (TacLine t : paso1) {
            if (t.tipo == Instruccion.Tipo.SALTO_INCOND && t.dest != null)
                etiquetasUsadas.add(t.dest);
            else if (t.tipo == Instruccion.Tipo.SALTO_COND_VERDAD && t.dest != null)
                etiquetasUsadas.add(t.dest);
        }
        // Paso b: código inalcanzable tras goto
        List<TacLine> paso2 = new ArrayList<>();
        boolean muerto = false;
        for (TacLine t : paso1) {
            if (t.tipo == Instruccion.Tipo.ETIQUETA) {
                String lbl = t.dest;
                if (muerto) {
                    if (lbl != null && (etiquetasUsadas.contains(lbl)
                            || lbl.startsWith("PROGRAMA") || lbl.startsWith("FINAL"))) {
                        muerto = false;
                        paso2.add(t);
                    }
                    // si nadie salta aquí y estamos en código muerto, omitir
                } else {
                    paso2.add(t);
                }
                continue;
            }
            if (muerto) continue;
            paso2.add(t);
            if (t.tipo == Instruccion.Tipo.SALTO_INCOND) muerto = true;
        }
        trabajo = paso2;
    }
    // =========================================================================
    // 4. ELIMINACIÓN DE TEMPORALES MUERTOS
    // =========================================================================
    private void eliminarTemporalesMuertos() {
        List<TacLine> lista = new ArrayList<>(trabajo);
        boolean[] eliminar = new boolean[lista.size()];
        Set<String> usados = new HashSet<>();
        // Recorrido hacia atrás
        for (int i = lista.size() - 1; i >= 0; i--) {
            TacLine t = lista.get(i);
            switch (t.tipo) {
                case ASIGNAR: {
                    String dest = t.dest, src = t.arg1;
                    if (src != null && !esLiteral(src)) usados.add(src);
                    if (dest != null && (globales.contains(dest) || usados.contains(dest))) {
                        usados.remove(dest);
                    } else if (dest != null && !globales.contains(dest)) {
                        eliminar[i] = true;
                    }
                    break;
                }
                case BINARIA: {
                    String dest = t.dest;
                    if (t.arg1 != null && !esLiteral(t.arg1)) usados.add(t.arg1);
                    if (t.arg2 != null && !esLiteral(t.arg2)) usados.add(t.arg2);
                    if (dest != null && (globales.contains(dest) || usados.contains(dest))) {
                        usados.remove(dest);
                    } else if (dest != null && !globales.contains(dest)) {
                        eliminar[i] = true;
                    }
                    break;
                }
                case UNARIA: {
                    String dest = t.dest;
                    if (t.arg1 != null && !esLiteral(t.arg1)) usados.add(t.arg1);
                    if (dest != null && (globales.contains(dest) || usados.contains(dest))) {
                        usados.remove(dest);
                    } else if (dest != null && !globales.contains(dest)) {
                        eliminar[i] = true;
                    }
                    break;
                }
                case SALTO_COND_VERDAD:
                    if (t.arg1 != null) usados.add(t.arg1);
                    break;
                case RETORNO:
                    if (t.arg1 != null && !esLiteral(t.arg1)) usados.add(t.arg1);
                    break;
                case CALL:
                    if (t.arg1 != null && !t.arg1.isEmpty()) {
                        for (String arg : t.arg1.split(",\\s*")) {
                            String a = arg.trim();
                            if (!esLiteral(a)) usados.add(a);
                        }
                    }
                    break;
                case ASIGNAR_ARRAY_W:
                    if (t.arg1 != null && !esLiteral(t.arg1)) usados.add(t.arg1);
                    if (t.arg2 != null && !esLiteral(t.arg2)) usados.add(t.arg2);
                    break;
                case ASIGNAR_ARRAY_R: {
                    String dest = t.dest;
                    if (esTemporal(dest) && !usados.contains(dest)) {
                        eliminar[i] = true;
                    } else if (dest != null && !globales.contains(dest) && !usados.contains(dest)) {
                        eliminar[i] = true;
                    } else {
                        if (t.arg2 != null && !esLiteral(t.arg2)) usados.add(t.arg2);
                        usados.remove(dest);
                    }
                    break;
                }
                case RETURN_VALUE_ASSIGN: {
                    String dest = t.dest;
                    if (dest != null && !globales.contains(dest) && !usados.contains(dest)) {
                        eliminar[i] = true;
                    } else {
                        usados.remove(dest);
                    }
                    break;
                }
                case DECLARE:
                case DECLARE_ARRAY:
                    if (t.dest != null && esTemporal(t.dest) && !usados.contains(t.dest)) {
                        // Temporal no usado → eliminar
                        eliminar[i] = true;
                    } else if (t.dest != null && !esTemporal(t.dest)
                            && !globales.contains(t.dest)   // no es global
                            && !usados.contains(t.dest)) {  // y no fue usada
                        // Variable local con nombre real no usada → eliminar
                        eliminar[i] = true;
                    }
                    // Globales siempre se conservan, usadas o no
                    break;
                default:
                    break;
            }
        }
        List<TacLine> res = new ArrayList<>();
        for (int i = 0; i < lista.size(); i++) {
            if (!eliminar[i]) res.add(lista.get(i));
        }
        trabajo = res;
    }
    // =========================================================================
    // IMPRESIÓN Y ACCESO
    // =========================================================================
    public void imprimir() {
        System.out.println("   Codigo optimizado:");
        System.out.println();
        for (int i = 0; i < trabajo.size(); i++) {
            System.out.printf("  %2d: %s%n", i, trabajo.get(i));
        }
    }
    public List<String> getLineas() {
        List<String> lineas = new ArrayList<>();
        for (int i = 0; i < trabajo.size(); i++) {
            lineas.add(String.format("%2d: %s", i, trabajo.get(i)));
        }
        return lineas;
    }
    /** Reconstruye la lista de Instruccion desde el estado interno. */
    private List<Instruccion> reconstruir() {
        List<Instruccion> r = new ArrayList<>(trabajo.size());
        for (TacLine t : trabajo) r.add(t.toInstruccion());
        return r;
    }
    // =========================================================================
    // FÁBRICAS DE TacLine (para reemplazos internos)
    // =========================================================================
    private static TacLine hacerAsignar(String dest, String src) {
        Instruccion i = Instruccion.asignar(dest, src);
        return parsear(i);
    }
    private static Set<String> recolectarGlobales(List<TacLine> lista) {
        Set<String> globales = new HashSet<>();
        boolean dentroDeFunciones = false;
        for (TacLine t : lista) {
            if (t.tipo == Instruccion.Tipo.ETIQUETA && t.dest != null && t.dest.startsWith("func_")) {
                dentroDeFunciones = true;
            }
            if (!dentroDeFunciones && (t.tipo == Instruccion.Tipo.DECLARE || t.tipo == Instruccion.Tipo.DECLARE_ARRAY)) {
                if (t.dest != null) globales.add(t.dest);
            }
        }
        return globales;
    }
    // =========================================================================
    // AUXILIARES
    // =========================================================================
    private String sustituir(String nombre, Map<String, String> constantes) {
        if (nombre == null) return nombre;
        String actual = nombre;
        Set<String> vistos = new HashSet<>();
        while (constantes.containsKey(actual) && !vistos.contains(actual)) {
            vistos.add(actual);
            actual = constantes.get(actual);
        }
        return actual;
    }
    private String resolver(String nombre, Map<String, String> sustituciones) {
        if (nombre == null) return null;
        return sustituir(nombre, sustituciones);
    }
    private String resolverAlias(String nombre, Map<String, String> sustituciones) {
        if (nombre == null) return null;
        if (esLiteral(nombre)) return nombre;
        String actual = nombre;
        Set<String> vistos = new HashSet<>();
        while (sustituciones.containsKey(actual) && !vistos.contains(actual)) {
            vistos.add(actual);
            String siguiente = sustituciones.get(actual);
            if (siguiente == null) break;
            if (esLiteral(siguiente)) {
                return actual;
            }
            actual = siguiente;
        }
        return actual;
    }
    private boolean esLiteral(String token) {
        if (token == null) return false;
        if (token.equals("true") || token.equals("false")) return true;
        if (token.startsWith("'") && token.endsWith("'")) return true;
        if (token.startsWith("\"") && token.endsWith("\"")) return true;
        return esNumericoLiteral(token);
    }
    private boolean esLiteralSimple(String token) {
        return esLiteral(token);
    }
    private boolean esNumericoLiteral(String token) {
        if (token == null) return false;
        try { Double.parseDouble(token); return true; }
        catch (NumberFormatException e) { return false; }
    }
    private boolean esTemporal(String nombre) {
        if (nombre == null || nombre.length() < 2) return false;
        if (nombre.charAt(0) != 't') return false;
        for (int i = 1; i < nombre.length(); i++) {
            if (!Character.isDigit(nombre.charAt(i))) return false;
        }
        return true;
    }
    private String calcularBinaria(String a, String op, String b) {
        try {
            if (a == null || b == null) return null;
            if ((a.equals("true") || a.equals("false")) && (b.equals("true") || b.equals("false"))) {
                boolean ba = Boolean.parseBoolean(a);
                boolean bb = Boolean.parseBoolean(b);
                switch (op) {
                    case "&&": return String.valueOf(ba && bb);
                    case "||": return String.valueOf(ba || bb);
                    case "==": return String.valueOf(ba == bb);
                    case "!=": return String.valueOf(ba != bb);
                    default: return null;
                }
            }
            if (esLiteralCadenaOChar(a) && esLiteralCadenaOChar(b)) {
                switch (op) {
                    case "==": return String.valueOf(a.equals(b));
                    case "!=": return String.valueOf(!a.equals(b));
                    default: return null;
                }
            }
            boolean esEntero = !a.contains(".") && !b.contains(".");
            if (esEntero) {
                long ia = Long.parseLong(a), ib = Long.parseLong(b), res;
                switch (op) {
                    case "+": res = ia + ib; break;
                    case "-": res = ia - ib; break;
                    case "*": res = ia * ib; break;
                    case "/": if (ib == 0) return null; res = ia / ib; break;
                    case "%": if (ib == 0) return null; res = ia % ib; break;
                    case "<": return String.valueOf(ia < ib);
                    case ">": return String.valueOf(ia > ib);
                    case "<=": return String.valueOf(ia <= ib);
                    case ">=": return String.valueOf(ia >= ib);
                    case "==": return String.valueOf(ia == ib);
                    case "!=": return String.valueOf(ia != ib);
                    default:  return null;
                }
                return String.valueOf(res);
            } else {
                double da = Double.parseDouble(a), db = Double.parseDouble(b), res;
                switch (op) {
                    case "+": res = da + db; break;
                    case "-": res = da - db; break;
                    case "*": res = da * db; break;
                    case "/": if (db == 0) return null; res = da / db; break;
                    case "<": return String.valueOf(da < db);
                    case ">": return String.valueOf(da > db);
                    case "<=": return String.valueOf(da <= db);
                    case ">=": return String.valueOf(da >= db);
                    case "==": return String.valueOf(da == db);
                    case "!=": return String.valueOf(da != db);
                    default:  return null;
                }
                if (res == Math.floor(res) && !Double.isInfinite(res))
                    return String.valueOf((long) res);
                return String.valueOf(res);
            }
        } catch (NumberFormatException e) { return null; }
    }
    private String calcularUnaria(String op, String a) {
        if (a == null) return null;
        try {
            switch (op) {
                case "-":
                    if (a.startsWith("\"") || a.startsWith("'")) return null;
                    if (a.contains(".")) return String.valueOf(-Double.parseDouble(a));
                    return String.valueOf(-Long.parseLong(a));
                case "!":
                    if ("true".equals(a) || "false".equals(a)) {
                        return String.valueOf(!Boolean.parseBoolean(a));
                    }
                    return null;
                default:
                    return null;
            }
        } catch (NumberFormatException e) {
            return null;
        }
    }
    private boolean esLiteralCadenaOChar(String token) {
        return token != null && ((token.startsWith("'") && token.endsWith("'")) || (token.startsWith("\"") && token.endsWith("\"")));
    }

    private String sustituirArgsCall(String args, Map<String, String> sustituciones) {
        if (args == null || args.isEmpty()) return args;
        StringBuilder sb = new StringBuilder();
        String[] partes = args.split(",\\s*");
        for (int i = 0; i < partes.length; i++) {
            String parte = partes[i].trim();
            if (i > 0) sb.append(", ");
            // Solo propagar si el argumento es un temporal (tN)
            if (esTemporal(parte)) {
                sb.append(resolver(parte, sustituciones));
            } else {
                sb.append(parte);
            }
        }
        return sb.toString();
    }
}