package com.compilador.optimizacion;
import com.compilador.codigoIntermedio.CodigoTresDir;
import com.compilador.codigoIntermedio.Instruccion;
import java.io.PrintWriter;
import java.util.*;
import java.util.LinkedHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * ============================================================
 * OPTIMIZADOR
 * ============================================================
 *
 * Implementa :
 *   1. Propagación de constantes
 *   2. Simplificación de expresiones
 *   3. Eliminación de subexpresiones comunes (CSE)
 *   4. Eliminación de código muerto (DCE)
 *
 * El optimizador trabaja sobre una representación interna (TacLine)
 * derivada de Instruccion.toString(), lo que permite aplicar análisis
 * y transformaciones sin modificar la estructura original del generador.
 *
 * El proceso se ejecuta hasta alcanzar un punto fijo: cuando una pasada
 * completa no produce cambios en el TAC.
 *
 * Este enfoque corresponde al modelo de optimización global basado en
 * flujo de control, donde cada iteración refina el programa hasta que
 * no es posible mejorar más sin alterar su semántica.
 */

public class Optimizador {

    /**
     * Representa una instrucción TAC ya parseada.
     *
     * Esta estructura desacopla la sintaxis textual de Instruccion.toString()
     * y expone campos semánticos (dest, arg1, op, arg2) que permiten aplicar
     * análisis estático y transformaciones.
     *
     * Es equivalente a un "árbol de instrucción" simplificado, útil para:
     *   - detectar patrones algebraicos,
     *   - identificar dependencias entre variables,
     *   - reconstruir instrucciones optimizadas.
     *
     * La conversión inversa (toInstruccion) garantiza que el optimizador
     * siempre produzca TAC válido y consistente con el generador original.
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

    /**
     * Patrones compilados una sola vez
     *
     * El parser utiliza expresiones regulares para reconocer los formatos
     * de salida de Instruccion.toString(). Cada patrón captura los campos
     * semánticos relevantes (dest, arg1, op, arg2) que luego usa el optimizador.
     *
     */

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
     * Parsea una instrucción TAC desde su representación textual.
     *
     * Cada tipo de instrucción se reconoce mediante expresiones regulares
     * precompiladas. Esto permite:
     *   - extraer operandos y operadores,
     *   - identificar destinos y fuentes,
     *   - clasificar la instrucción según su semántica.
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

    // ESTADO

    private final List<Instruccion> original;
    private List<TacLine>           trabajo;
    private int eliminadas = 0;

    // Lista de estadisticas por pasada: cada int[] = {instrAntes, instrDespues, eliminadasEnPasada}

    private final List<int[]> estadisticasPorPasada = new ArrayList<>();

    private final Set<String>       globales;



    // CONSTRUCTOR

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


    /**
     * Convierte la lista de Instruccion a la representación TacLine para
     * facilitar el análisis y las transformaciones posteriores.
     */

    private static List<TacLine> parsearTodas(List<Instruccion> lista) {
        List<TacLine> r = new ArrayList<>(lista.size());
        for (Instruccion i : lista) r.add(parsear(i));
        return r;
    }



    // PUNTO DE ENTRADA

    /** Optimiza sin guardar archivos por pasada. */

    public List<Instruccion> optimizar() {
        return optimizar(null);
    }

    /**
     * Ejecuta el pipeline completo de optimización hasta alcanzar un punto fijo.
     *
     * En cada pasada se aplican, en orden:
     *   1. Propagación de constantes
     *   2. Simplificación de expresiones
     *   3. Eliminación de subexpresiones comunes (CSE)
     *   4. Eliminación de código muerto (DCE)
     *
     * El proceso se repite mientras el contenido del TAC cambie.
     * Esto implementa un algoritmo iterativo de optimización global.
     *
     */

    public List<Instruccion> optimizar(String nombreBase) {
        estadisticasPorPasada.clear();

        // Guardar pasada 0: el TAC original, sin ninguna optimización
        if (nombreBase != null) {
            guardarPasada(nombreBase, 0);
        }

        int pasada = 1;
        int eliminadasPasadaAnterior = Integer.MAX_VALUE;
        String antesContenido;

        do {
            int antesSize = trabajo.size();
            antesContenido = serializar(trabajo);

            // Pipeline de optimizaciones
            propagarConstantes();            // 1. Propagacion de constantes
            simplificarExpresiones();        // 2. Simplificacion de expresiones
            eliminarSubexpresionesComunes(); // 3. CSE - Subexpresiones comunes
            eliminarCodigoMuerto();          // 4. Eliminacion de codigo muerto

            int despuesSize = trabajo.size();
            int eliminadasEnPasada = antesSize - despuesSize;
            estadisticasPorPasada.add(new int[]{antesSize, despuesSize, eliminadasEnPasada});

            // Verificar invariante de convergencia
            if (eliminadasEnPasada > eliminadasPasadaAnterior) {
                System.out.printf(
                        "   ADVERTENCIA convergencia: pasada %d eliminó %d instrucciones " +
                                "(más que la anterior: %d). Posible ciclo.%n",
                        pasada, eliminadasEnPasada, eliminadasPasadaAnterior);
            }
            eliminadasPasadaAnterior = eliminadasEnPasada;

            if (nombreBase != null) {
                guardarPasada(nombreBase, pasada);
            }
            pasada++;
        } while (!serializar(trabajo).equals(antesContenido));

        eliminadas = original.size() - trabajo.size();
        return reconstruir();
    }


    /**
     * Serializa la lista de instrucciones TAC en un único String.
     *
     * Se utiliza para comparar el contenido entre pasadas y detectar
     * convergencia (punto fijo). Dos programas son equivalentes si
     * contienen exactamente las mismas instrucciones en el mismo orden.
     */

    private String serializar(List<TacLine> lista) {
        StringBuilder sb = new StringBuilder();
        for (TacLine t : lista) {
            sb.append(t.toString()).append('\n');
        }
        return sb.toString();
    }


    // GUARDAR ARCHIVO POR PASADA

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

    // GETTERS DE ESTADÍSTICAS

    public int getCantidadOriginal()    { return original.size(); }
    public int getCantidadOptimizada()  { return trabajo.size(); }
    public int getCantidadEliminadas()  { return eliminadas; }
    public double getPorcentajeReduccion() {
        if (original.isEmpty()) return 0.0;
        return (eliminadas * 100.0) / original.size();
    }
    /** Devuelve las estadisticas por pasada: cada int[] = {instrAntes, instrDespues, eliminadasEnPasada}. */
    public List<int[]> getEstadisticasPorPasada() {
        return Collections.unmodifiableList(estadisticasPorPasada);
    }
    /** Devuelve la cantidad total de pasadas ejecutadas. */
    public int getCantidadPasadas() {
        return estadisticasPorPasada.size();
    }


    // =========================================================================
    // 1. PROPAGACIÓN DE CONSTANTES
    // =========================================================================

    /**
     * Implementa Propagación de Constantes
     *
     * Mantiene un mapa de sustituciones (variable - literal) que se
     * actualiza mientras se recorren las instrucciones en orden.
     *
     * Para cada instrucción:
     *   - Si usa una variable conocida como constante, se reemplaza por su valor.
     *   - Si ambos operandos son literales, se evalúa la operación en tiempo
     *     de compilación (constant folding).
     *   - Si una instrucción redefine una variable, se invalida su entrada
     *     en el mapa de constantes.
     *
     */

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
                    t.arg1 = sustituirArgsCall(t.arg1, sustituciones);
                    res.add(t);
                    sustituciones.clear();
                    break;

                case ASIGNAR: {
                    String dest = t.dest;
                    String src = t.arg1;

                    String val = esLiteral(src)
                            ? src
                            : resolverAlias(src, sustituciones);

                    t.arg1 = val;
                    res.add(t);

                    if (val != null)
                        sustituciones.put(dest, val);
                    else
                        sustituciones.remove(dest);

                    break;
                }

                case BINARIA: {
                    String dest = t.dest;

                    String a = resolver(t.arg1, sustituciones);
                    String b = resolver(t.arg2, sustituciones);

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

                case SALTO_COND_VERDAD:
                    t.arg1 = resolver(t.arg1, sustituciones);
                    res.add(t);
                    break;

                case RETORNO:
                    if (t.arg1 != null) {
                        t.arg1 = resolver(t.arg1, sustituciones);
                    }
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
    // 2. SIMPLIFICACIÓN DE EXPRESIONES
    // =========================================================================
    /**
     * Aplica dos tipos de simplificaciones a instrucciones BINARIA y UNARIA:
     *
     * a) Plegado de constantes (constant folding):
     *    Si ambos operandos son literales, se evalúa la expresión en tiempo
     *    de compilación y se reemplaza por una asignación con el resultado.
     *       Ej: T1 = 5 + 3  →  T1 = 8
     *
     * b) Identidades algebraicas:
     *    Se simplifican expresiones que siempre producen el mismo resultado
     *    sin importar el valor del operando variable:
     *       x * 1  → x          x * 0  → 0
     *       x + 0  → x          0 + x  → x
     *       x - 0  → x          x - x  → 0
     *       x / 1  → x          (x / x NO se simplifica: no se garantiza x != 0)
     *       x ** 2 → x * x      (se convierte en multiplicación)
     *       x && true → x       x || false → x
     *       x && false → false  x || true  → true
     */

    private void simplificarExpresiones() {
        List<TacLine> res = new ArrayList<>();

        for (TacLine t : trabajo) {

            if (t.tipo == Instruccion.Tipo.SALTO_COND_VERDAD) {

                if ("true".equals(t.arg1)) {
                    TacLine nuevo = parsear(
                            Instruccion.saltoIncond(t.dest)
                    );

                    res.add(nuevo);
                    continue;
                }

                if ("false".equals(t.arg1)) {
                    continue;
                }
            }

            if (t.tipo == Instruccion.Tipo.BINARIA) {

                if (esLiteralSimple(t.arg1) &&
                        esLiteralSimple(t.arg2)) {

                    String valor =
                            calcularBinaria(t.arg1, t.op, t.arg2);

                    if (valor != null) {
                        res.add(hacerAsignar(t.dest, valor));
                        continue;
                    }
                }

                String simplif =
                        aplicarIdentidad(t.arg1, t.op, t.arg2);

                if (simplif != null) {
                    res.add(hacerAsignar(t.dest, simplif));
                    continue;
                }

                if ("**".equals(t.op) &&
                        "2".equals(t.arg2)) {

                    TacLine nueva =
                            new TacLine(
                                    Instruccion.Tipo.BINARIA,
                                    t.dest + " = "
                                            + t.arg1
                                            + " * "
                                            + t.arg1);

                    nueva.dest = t.dest;
                    nueva.arg1 = t.arg1;
                    nueva.arg2 = t.arg1;
                    nueva.op = "*";

                    res.add(nueva);
                    continue;
                }
            }

            else if (t.tipo == Instruccion.Tipo.UNARIA &&
                    esLiteralSimple(t.arg1)) {

                String valor =
                        calcularUnaria(t.op, t.arg1);

                if (valor != null) {
                    res.add(hacerAsignar(t.dest, valor));
                    continue;
                }
            }

            res.add(t);
        }

        trabajo = res;
    }

    /**
     * Aplica identidades algebraicas a una operación binaria.
     * Retorna el valor simplificado (literal o nombre de variable) o null si no aplica.
     *
     * Casos soportados:
     *   Multiplicación:  x*1 → x,  1*x → x,  x*0 → 0,  0*x → 0
     *   Suma:            x+0 → x,  0+x → x
     *   Resta:           x-0 → x,  x-x → 0
     *   División:        x/1 → x   (x/x NO se simplifica: 0/0 es indefinido)
     *   Lógico AND:      x&&true → x,  true&&x → x,  x&&false → false,  false&&x → false
     *   Lógico OR:       x||false → x, false||x → x, x||true → true,   true||x → true
     */

    private String aplicarIdentidad(String a, String op, String b) {
        if (a == null || b == null || op == null) return null;
        switch (op) {
            case "*":
                if ("0".equals(b) || "0.0".equals(b)) return "0";
                if ("0".equals(a) || "0.0".equals(a)) return "0";
                if ("1".equals(b) || "1.0".equals(b)) return a;
                if ("1".equals(a) || "1.0".equals(a)) return b;
                break;
            case "+":
                if ("0".equals(b) || "0.0".equals(b)) return a;
                if ("0".equals(a) || "0.0".equals(a)) return b;
                break;
            case "-":
                if ("0".equals(b) || "0.0".equals(b)) return a;
                if (a.equals(b) && !esLiteral(a))      return "0";
                break;
            case "/":
                if ("1".equals(b) || "1.0".equals(b)) return a;
                break;
            case "&&":
                if ("true".equals(b))  return a;
                if ("true".equals(a))  return b;
                if ("false".equals(b)) return "false";
                if ("false".equals(a)) return "false";
                break;
            case "||":
                if ("false".equals(b)) return a;
                if ("false".equals(a)) return b;
                if ("true".equals(b))  return "true";
                if ("true".equals(a))  return "true";
                break;
        }
        return null;
    }


    // =========================================================================
    // 3. ELIMINACIÓN DE SUBEXPRESIONES COMUNES (CSE)
    // =========================================================================

    /**
     * Detecta expresiones binarias repetidas del tipo:
     *     T1 = a + b
     *     ...
     *     T2 = a + b   ← misma expresión, sin cambios en a ni b
     *
     * Si los operandos no han sido modificados entre ambas apariciones,
     * la segunda instrucción se reemplaza por:
     *     T2 = T1
     *
     * El algoritmo:
     *   - Normaliza expresiones conmutativas (a+b ≡ b+a).
     *   - Mantiene un mapa expresión → temporal que la contiene.
     *   - Invalida entradas cuando una variable usada en la expresión
     *     es redefinida.
     *   - Limpia el mapa en puntos con efectos laterales (CALL, etiquetas).
     *
     */

    private void eliminarSubexpresionesComunes() {

        // expresion normalizada -> temporal/variable que ya contiene el resultado
        Map<String, String> exprATemp = new LinkedHashMap<>();
        List<TacLine> res = new ArrayList<>();

        for (TacLine t : trabajo) {

            switch (t.tipo) {

                case BINARIA: {

                    String clave = claveCSE(t.arg1, t.op, t.arg2);

                    // ¿Ya calculamos exactamente esta expresión?
                    if (exprATemp.containsKey(clave)) {

                        String existente = exprATemp.get(clave);

                        // tX = a+b  ->  tX = tY
                        res.add(hacerAsignar(t.dest, existente));

                    } else {

                        // El destino recibe un nuevo valor.
                        // Todas las expresiones que dependían de él dejan de ser válidas.
                        if (t.dest != null) {
                            invalidarPorVariable(exprATemp, t.dest);
                        }

                        // Registrar la nueva expresión calculada
                        exprATemp.put(clave, t.dest);

                        res.add(t);
                    }

                    break;
                }

                case ASIGNAR:
                case UNARIA:
                case ASIGNAR_ARRAY_R:
                case RETURN_VALUE_ASSIGN:

                    // El destino cambia de valor.
                    if (t.dest != null) {
                        invalidarPorVariable(exprATemp, t.dest);
                    }

                    res.add(t);
                    break;

                case ASIGNAR_ARRAY_W:

                    // Escritura en array => invalida expresiones
                    // que dependan de ese array.
                    if (t.dest != null) {
                        invalidarPorVariable(exprATemp, t.dest);
                    }

                    res.add(t);
                    break;

                case ETIQUETA:
                case CALL:

                    // Punto de control / posible efecto lateral.
                    exprATemp.clear();

                    res.add(t);
                    break;

                default:
                    res.add(t);
                    break;
            }
        }

        trabajo = res;
    }

    /**
     * Normaliza una expresión binaria para CSE.
     * Operadores conmutativos (+, *) se ordenan lexicográficamente para
     * detectar equivalencias a+b == b+a.
     */

    private String claveCSE(String a, String op, String b) {

        boolean conmutativo =
                "+".equals(op)
                        || "*".equals(op)
                        || "==".equals(op)
                        || "!=".equals(op)
                        || "&&".equals(op)
                        || "||".equals(op);

        if (conmutativo &&
                a != null &&
                b != null &&
                a.compareTo(b) > 0) {

            return b + "|" + op + "|" + a;
        }

        return a + "|" + op + "|" + b;
    }

    /**
     * Invalida del mapa de CSE todas las entradas que dependen de la variable dada.
     * Esto es necesario cuando la variable es redefinida: cualquier expresión que
     * la usaba ya no es válida.
     */

    private void invalidarPorVariable(Map<String, String> mapa, String var) {
        if (var == null) return;
        mapa.entrySet().removeIf(e -> {
            String[] partes = e.getKey().split("\\|");
            return partes.length == 3
                    && (partes[0].equals(var) || partes[2].equals(var));
        });
    }

    // =========================================================================
    // 4. ELIMINACIÓN DE CÓDIGO MUERTO (DCE)
    // =========================================================================

    /**
     * Elimina:
     *   - Instrucciones cuyo resultado nunca es usado.
     *   - Código inalcanzable después de saltos incondicionales.
     *   - Asignaciones redundantes (x = x).
     *
     * Utiliza un análisis de uso-definición (liveness analysis) simplificado:
     *   - Recorre el TAC hacia atrás recolectando variables vivas.
     *   - Si una instrucción define una variable que no está viva,
     *     puede eliminarse sin alterar la semántica del programa.
     *
     */

    private void eliminarCodigoMuerto() {
        // --- Subtipo c: x = x ---
        List<TacLine> paso1 = new ArrayList<>();
        for (TacLine t : trabajo) {
            if (t.tipo == Instruccion.Tipo.ASIGNAR
                    && t.dest != null && t.dest.equals(t.arg1)) continue;
            paso1.add(t);
        }

        // Recolectar etiquetas destino de saltos (para subtipo b)
        Set<String> etiquetasUsadas = new HashSet<>();
        for (TacLine t : paso1) {
            if (t.tipo == Instruccion.Tipo.SALTO_INCOND && t.dest != null)
                etiquetasUsadas.add(t.dest);
            else if (t.tipo == Instruccion.Tipo.SALTO_COND_VERDAD && t.dest != null)
                etiquetasUsadas.add(t.dest);
        }

        // --- Subtipo b: codigo inalcanzable tras goto ---
        List<TacLine> paso2 = new ArrayList<>();
        boolean muerto = false;
        for (TacLine t : paso1) {
            if (t.tipo == Instruccion.Tipo.ETIQUETA) {
                String lbl = t.dest;
                if (muerto) {
                    if (lbl != null && (etiquetasUsadas.contains(lbl)
                            || lbl.startsWith("func_")
                            || lbl.startsWith("PROGRAMA") || lbl.startsWith("FINAL"))) {
                        muerto = false;
                        paso2.add(t);
                    }
                } else {
                    paso2.add(t);
                }
                continue;
            }
            if (muerto) continue;
            paso2.add(t);
            // El código que sigue a un goto INCONDICIONAL o a un RETURN es
            // inalcanzable (salvo que retome en una etiqueta usada/especial).
            if (t.tipo == Instruccion.Tipo.SALTO_INCOND || t.tipo == Instruccion.Tipo.RETORNO) muerto = true;
        }

        trabajo = paso2;

        // --- Subtipo a: variables y temporales cuyo resultado nunca es usado ---
        eliminarVariablesSinUso();
    }

    /**
     * Implementacion del subtipo a de eliminacion de codigo muerto.
     *
     * Recorre de atras hacia adelante manteniendo el conjunto de variables
     * "vivas". Una instruccion que asigna a una variable no-viva se elimina.
     * Aplica igualmente a temporales (tN) y variables de usuario no globales.
     */

    private void eliminarVariablesSinUso() {
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
                    if (t.dest != null) usados.add(t.dest);
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
                        // dest = arr[idx]  ->  el array (arg1) se está leyendo: queda usado
                        if (t.arg1 != null) usados.add(t.arg1);
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
                case PARAM_DEF:
                    // Las declaraciones NUNCA se eliminan: forman parte del TAC
                    // aunque la variable no se "use" según este análisis, es un error semántico.
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


    // IMPRESIÓN Y ACCESO

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


    /**
     * Reconstruye la lista de Instruccion a partir de TacLine para devolver
     * el resultado final del optimizador.
     */

    private List<Instruccion> reconstruir() {
        List<Instruccion> r = new ArrayList<>(trabajo.size());
        for (TacLine t : trabajo) r.add(t.toInstruccion());
        return r;
    }

    // FÁBRICAS DE TacLine (para reemplazos internos)

    /**
     * Construye una TacLine que representa "dest = valor" usando el parser
     * para garantizar consistencia con Instruccion.
     */

    private static TacLine hacerAsignar(String dest, String src) {
        Instruccion i = Instruccion.asignar(dest, src);
        return parsear(i);
    }

    /**
     * Recolecta nombres de variables globales a partir de declaraciones en el TAC.
     * Esto evita eliminar declaraciones globales por DCE.
     */

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

    // AUXILIARES

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

    /**
     * Resuelve un operando aplicando el mapa de sustituciones (alias → literal).
     * Si el operando es literal, se devuelve tal cual.
     * Si es una variable y existe sustitución, devuelve la sustitución.
     * Si no, devuelve el nombre original.
     */

    private String resolver(String nombre, Map<String, String> sustituciones) {
        if (nombre == null) return null;
        return sustituir(nombre, sustituciones);
    }

    /**
     * Resuelve alias encadenados en el mapa de sustituciones.
     * Si la variable apunta a otra variable que a su vez es literal, sigue la cadena.
     */

    private String resolverAlias(String nombre, Map<String, String> sustituciones) {
        if (nombre == null) return null;

        String actual = nombre;
        Set<String> vistos = new HashSet<>();

        while (sustituciones.containsKey(actual) && !vistos.contains(actual)) {
            vistos.add(actual);

            String siguiente = sustituciones.get(actual);

            if (siguiente == null) {
                break;
            }

            actual = siguiente;
        }

        return actual;
    }

    /**
     * Determina si un token es un literal (número entero o booleano o literal simple).
     * Esta función es conservadora: reconoce enteros y true/false.
     */

    private boolean esLiteral(String token) {
        if (token == null) return false;
        if (token.equals("true") || token.equals("false")) return true;
        if (token.startsWith("'") && token.endsWith("'")) return true;
        if (token.startsWith("\"") && token.endsWith("\"")) return true;
        return esNumericoLiteral(token);
    }

    /**
     * Versión más estricta que acepta literales simples sin espacios.
     */

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

    /**
     * Intenta evaluar una operación binaria si ambos operandos son literales.
     * Soporta enteros y booleanos básicos. Devuelve el literal resultado o null.
     */

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

    /**
     * Intenta evaluar una operación unaria si el operando es literal simple.
     * Soporta negación booleana (!) y negación aritmética (-).
     */

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

    /**
     * Sustituye argumentos de una llamada CALL cuando hay constantes conocidas.
     * Recibe la cadena de argumentos tal como aparece en la instrucción y
     * reemplaza cada token por su literal si existe en el mapa.
     */

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