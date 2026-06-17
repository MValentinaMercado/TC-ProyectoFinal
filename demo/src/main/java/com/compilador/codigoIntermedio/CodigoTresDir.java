package com.compilador.codigoIntermedio;

import com.compilador.MiLenguajeBaseVisitor;
import com.compilador.MiLenguajeParser;

import java.util.ArrayList;
import java.util.List;

/**
 * ============================================================
 * CodigoTresDir
 * ============================================================
 *
 * Generador de Código de Tres Direcciones
 *
 *     Recorre el AST (ParseTree) generado por ANTLR y produce
 *     una representación intermedia (TAC) que será usada por
 *     etapas posteriores (optimización).
 *
 *   - Extiende MiLenguajeBaseVisitor<String>.
 *   - Las visitas a expresiones devuelven un "lugar" (place):
 *       • un temporal (tN)
 *       • el nombre de una variable
 *       • un literal (ej. "5", "true", "\"hola\"")
 *       • o la cadena especial "RETURN_VALUE" cuando una llamada
 *         a función produce un valor que será asignado por el
 *         contexto llamador.
 *
 *   - Las visitas a declaraciones y sentencias devuelven null
 *     (su efecto es emitir instrucciones).
 *
 *   - Los temporales se generan con nuevaTemp() (t1, t2, ...).
 *   - Las etiquetas para control de flujo son únicas por contador.
 *   - Se emiten instrucciones mediante emitir(Instruccion).
 *   - La lista instrucciones contiene el TAC en orden secuencial.
 *
 */

public class CodigoTresDir extends MiLenguajeBaseVisitor<String> {

    /** Lista de instrucciones TAC generadas */

    private final List<Instruccion> instrucciones = new ArrayList<>();

    /** Contador de temporales: t1, t2, t3, ... */

    private int contTemp = 0;

    /** Contador de etiquetas para if/else */

    private int contIf   = 0;

    /** Contador de etiquetas para while */

    private int contWhile = 0;

    /** Contador de etiquetas para for */

    private int contFor  = 0;

    /** Contexto: true = estamos en ámbito global (para DECLARE global) */

    private boolean esGlobal = true;

    /** Nombre de la función actual (útil para emitir PARAM/RETURN en contexto) */

    private String funcionActual = null;


    /**
     * Genera un nuevo temporal único.
     */

    private String nuevaTemp() {
        return "t" + (++contTemp);
    }

    /**
     * Añade una instrucción a la lista interna.
     * Centraliza el punto donde se acumulan las instrucciones.
     */

    private void emitir(Instruccion i) {
        instrucciones.add(i);
    }

    public List<Instruccion> getInstrucciones() {
        return instrucciones;
    }

    public int getCantidadInstrucciones() {
        return instrucciones.size();
    }

    /**
     * Imprime las instrucciones numeradas por consola.
     */

    public void imprimir() {
        System.out.println();
        for (int i = 0; i < instrucciones.size(); i++) {
            System.out.printf("  %2d: %s%n", i, instrucciones.get(i).toString());
        }
    }

    /**
     * Devuelve el código TAC como lista de líneas formateadas.
     * Cada línea incluye el número de instrucción
     */

    public List<String> getLineas() {
        List<String> lineas = new ArrayList<>();
        for (int i = 0; i < instrucciones.size(); i++) {
            lineas.add(String.format("%2d: %s", i, instrucciones.get(i).toString()));
        }
        return lineas;
    }

    /**
     * visitPrograma:
     *
     * Flujo general:
     *   1) Emitir etiqueta de inicio del programa.
     *   2) Emitir declaraciones globales (DECLARE).
     *   3) Emitir funciones (cada función con su etiqueta y cuerpo).
     *   4) Emitir etiqueta de fin del programa.
     *
     */

    @Override
    public String visitPrograma(MiLenguajeParser.ProgramaContext ctx) {
        emitir(Instruccion.etiqueta("PROGRAMA_INICIO"));

        // Primero emitir las declaraciones de variables globales
        emitir(Instruccion.comentario("Declaracion de variables globales"));
        esGlobal = true;

        for (MiLenguajeParser.DeclaracionContext decl : ctx.declaracion()) {
            if (decl.declaracionVariable() != null) {
                visitDeclaracionVariableGlobal(decl.declaracionVariable());
            }
        }

        // Luego emitir las funciones
        esGlobal = false;
        for (MiLenguajeParser.DeclaracionContext decl : ctx.declaracion()) {
            if (decl.declaracionFuncion() != null) {
                visitDeclaracionFuncion(decl.declaracionFuncion());
            }
        }

        emitir(Instruccion.etiqueta("PROGRAMA_FIN"));
        return null;
    }

    /**
     * visitDeclaracionVariableGlobal:
     *
     * - En ámbito global emitimos DECLARE (o DECLARE_ARRAY).
     * - Si hay inicializador global, se emite la asignación correspondiente.
     *
     */

    private void visitDeclaracionVariableGlobal(MiLenguajeParser.DeclaracionVariableContext ctx) {
        String nombreTipo = ctx.tipo().getText();
        String nombreVar  = ctx.ID().getText();

        // Detectar array: tipo ID '[' NUM ']' ';'
        if (ctx.getChildCount() > 3 && ctx.getChild(2).getText().equals("[")) {
            String size = ctx.NUM().getText();
            emitir(Instruccion.declareArray(nombreVar, size, nombreTipo));
        } else {
            emitir(Instruccion.declare(nombreVar, nombreTipo));
            // Si tiene inicializador global (posible aunque poco común)
            if (ctx.expresion() != null) {
                String lugar = visit(ctx.expresion());
                emitir(Instruccion.asignar(nombreVar, lugar));
            }
        }
    }

    /**
     * visitDeclaracionVariable (local):
     *
     * - Emite DECLARE para variables locales.
     * - Si hay inicializador, evalúa la expresión y emite la asignación.
     * - Maneja arrays y asignaciones especiales.
     */

    @Override
    public String visitDeclaracionVariable(MiLenguajeParser.DeclaracionVariableContext ctx) {
        String nombreTipo = ctx.tipo().getText();
        String nombreVar  = ctx.ID().getText();

        // Array: tipo ID '[' NUM ']' ';'
        if (ctx.getChildCount() > 3 && ctx.getChild(2).getText().equals("[")) {
            String size = ctx.NUM().getText();
            emitir(Instruccion.declareArray(nombreVar, size, nombreTipo));
        } else {
            emitir(Instruccion.declare(nombreVar, nombreTipo));
            if (ctx.expresion() != null) {
                String lugar = visit(ctx.expresion());
                if ("RETURN_VALUE".equals(lugar)) {
                    // Caso especial: la expresión es el valor de retorno de una llamada
                    emitir(Instruccion.returnValueAssign(nombreVar));
                } else {
                    emitir(Instruccion.asignar(nombreVar, lugar));
                }
            }
        }
        return null;
    }

    /**
     * visitDeclaracionFuncion:
     *
     * - Emite una etiqueta para la función (func_nombre).
     * - Emite definiciones de parámetros (PARAM_DEF).
     * - Visita el bloque (cuerpo) de la función.
     *
     */

    @Override
    public String visitDeclaracionFuncion(MiLenguajeParser.DeclaracionFuncionContext ctx) {
        String nombre = ctx.ID().getText();
        funcionActual = nombre;

        // Etiqueta func_nombre:
        emitir(Instruccion.etiqueta("func_" + nombre));

        // Emitir PARAM para cada parámetro (definición)
        if (ctx.parametros() != null) {
            for (MiLenguajeParser.ParametroContext param : ctx.parametros().parametro()) {
                String tipoParam   = param.tipo().getText();
                String nombreParam = param.ID().getText();
                emitir(Instruccion.paramDef(nombreParam, tipoParam));
            }
        }

        // Cuerpo
        visitBloque(ctx.bloque());

        funcionActual = null;
        return null;
    }

    @Override
    public String visitBloque(MiLenguajeParser.BloqueContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public String visitSentencia(MiLenguajeParser.SentenciaContext ctx) {
        visitChildren(ctx);
        return null;
    }

    /**
     * visitAsignacion:
     *
     * Dos formas:
     *   - ID = expr
     *   - ID[expr] = expr  (asignación a array)
     *
     * Evalúa las expresiones y emite la instrucción TAC correspondiente.
     */

    @Override
    public String visitAsignacion(MiLenguajeParser.AsignacionContext ctx) {
        // ID '[' expr ']' '=' expr
        if (ctx.getChildCount() > 3 && ctx.getChild(1).getText().equals("[")) {
            String idx = visit(ctx.expresion(0));
            String val = visit(ctx.expresion(1));
            emitir(Instruccion.asignarArrayW(ctx.ID().getText(), idx, val));
        } else {
            // ID '=' expr
            String lugar = visit(ctx.expresion(0));
            if ("RETURN_VALUE".equals(lugar)) {
                // Caso especial: asignar el valor retornado por una llamada
                emitir(Instruccion.returnValueAssign(ctx.ID().getText()));
            } else {
                emitir(Instruccion.asignar(ctx.ID().getText(), lugar));
            }
        }
        return null;
    }

    /**
     * visitIncremento:
     *
     * Implementa i++ y i-- como:
     *   tN = i + 1
     *   i = tN
     *
     */

    @Override
    public String visitIncremento(MiLenguajeParser.IncrementoContext ctx) {
        String nombre = ctx.ID().getText();
        String op     = ctx.getChild(1).getText(); // "++" o "--"
        String t      = nuevaTemp();
        if (op.equals("++")) {
            emitir(Instruccion.binaria(t, nombre, "+", "1"));
        } else {
            emitir(Instruccion.binaria(t, nombre, "-", "1"));
        }
        emitir(Instruccion.asignar(nombre, t));
        return null;
    }

    /**
     * visitLlamadaFuncion (como sentencia):
     *
     * - Construye la lista de argumentos evaluando cada expresión.
     * - Emite CALL sin capturar el valor de retorno (uso como sentencia).
     */

    @Override
    public String visitLlamadaFuncion(MiLenguajeParser.LlamadaFuncionContext ctx) {
        String argsStr = construirArgsCall(ctx.argumentos());
        emitir(Instruccion.call("func_" + ctx.ID().getText(), argsStr));
        return null;
    }

    /**
     * visitRetorno:
     *
     * - Si hay expresión, la evalúa y emite RETURN con el lugar.
     * - Si no hay expresión, emite RETURN vacío.
     */

    @Override
    public String visitRetorno(MiLenguajeParser.RetornoContext ctx) {
        if (ctx.expresion() != null) {
            String lugar = visit(ctx.expresion());
            emitir(Instruccion.retorno(lugar));
        } else {
            emitir(Instruccion.retorno(null));
        }
        return null;
    }

    /**
     * visitSentenciaBreak / visitSentenciaContinue:
     *
     * - Aquí se emiten saltos simbólicos "BREAK" y "CONTINUE".
     *
     */

    @Override
    public String visitSentenciaBreak(MiLenguajeParser.SentenciaBreakContext ctx) {
        emitir(Instruccion.saltoIncond("BREAK"));
        return null;
    }

    @Override
    public String visitSentenciaContinue(MiLenguajeParser.SentenciaContinueContext ctx) {
        emitir(Instruccion.saltoIncond("CONTINUE"));
        return null;
    }

    /**
     * visitSentenciaIf:
     *
     * Patrón TAC para IF/ELSE:
     *
     *   tX = <condicion>
     *   if tX goto THEN_N
     *   goto ELSE_N | END_IF_N
     *   THEN_N:
     *     ...then...
     *     goto END_IF_N
     *   ELSE_N:
     *     ...else...
     *   END_IF_N:
     *
     */

    @Override
    public String visitSentenciaIf(MiLenguajeParser.SentenciaIfContext ctx) {
        int n = ++contIf;
        String labelThen   = "THEN_" + n;
        String labelEndIf  = "END_IF_" + n;

        String cond = visit(ctx.expresion());
        boolean tieneElse = ctx.bloque().size() == 2;

        if (tieneElse) {
            String labelElse = "ELSE_" + n;
            emitir(Instruccion.saltoCondVerdad(cond, labelThen));
            emitir(Instruccion.saltoIncond(labelElse));
            emitir(Instruccion.etiqueta(labelThen));
            visitBloque(ctx.bloque(0));
            emitir(Instruccion.saltoIncond(labelEndIf));
            emitir(Instruccion.etiqueta(labelElse));
            visitBloque(ctx.bloque(1));
        } else {
            emitir(Instruccion.saltoCondVerdad(cond, labelThen));
            emitir(Instruccion.saltoIncond(labelEndIf));
            emitir(Instruccion.etiqueta(labelThen));
            visitBloque(ctx.bloque(0));
        }
        emitir(Instruccion.etiqueta(labelEndIf));
        return null;
    }

    /**
     * visitSentenciaWhile:
     *
     * Patrón TAC para WHILE:
     *
     *   WHILE_START_N:
     *     tX = <condicion>
     *     if tX goto WHILE_BODY_N
     *     goto WHILE_END_N
     *   WHILE_BODY_N:
     *     ...cuerpo...
     *     goto WHILE_START_N
     *   WHILE_END_N:
     */

    @Override
    public String visitSentenciaWhile(MiLenguajeParser.SentenciaWhileContext ctx) {
        int n = ++contWhile;
        String labelStart = "WHILE_START_" + n;
        String labelBody  = "WHILE_BODY_" + n;
        String labelEnd   = "WHILE_END_" + n;

        emitir(Instruccion.etiqueta(labelStart));
        String cond = visit(ctx.expresion());
        emitir(Instruccion.saltoCondVerdad(cond, labelBody));
        emitir(Instruccion.saltoIncond(labelEnd));
        emitir(Instruccion.etiqueta(labelBody));
        visitBloque(ctx.bloque());
        emitir(Instruccion.saltoIncond(labelStart));
        emitir(Instruccion.etiqueta(labelEnd));
        return null;
    }

    /**
     * visitSentenciaFor:
     *
     * Patrón TAC para FOR:
     *
     *   <inicializacion>
     *   FOR_START_N:
     *     tX = <condicion>
     *     if tX goto FOR_BODY_N
     *     goto FOR_END_N
     *   FOR_BODY_N:
     *     ...cuerpo...
     *     <actualizacion>
     *     goto FOR_START_N
     *   FOR_END_N:
     */

    @Override
    public String visitSentenciaFor(MiLenguajeParser.SentenciaForContext ctx) {
        int n = ++contFor;
        String labelStart = "FOR_START_" + n;
        String labelBody  = "FOR_BODY_" + n;
        String labelEnd   = "FOR_END_" + n;

        // Inicializacion (declaracion o asignacion)
        if (ctx.inicializacionFor() != null) {
            visitInicializacionFor(ctx.inicializacionFor());
        }

        emitir(Instruccion.etiqueta(labelStart));

        // Condicion (puede ser vacia - for infinito)
        if (ctx.expresion() != null) {
            String cond = visit(ctx.expresion());
            emitir(Instruccion.saltoCondVerdad(cond, labelBody));
            emitir(Instruccion.saltoIncond(labelEnd));
        }

        emitir(Instruccion.etiqueta(labelBody));
        visitBloque(ctx.bloque());

        // Actualizacion
        if (ctx.actualizacionFor() != null) {
            visitActualizacionFor(ctx.actualizacionFor());
        }

        emitir(Instruccion.saltoIncond(labelStart));
        emitir(Instruccion.etiqueta(labelEnd));
        return null;
    }

    @Override
    public String visitInicializacionFor(MiLenguajeParser.InicializacionForContext ctx) {
        visitChildren(ctx);
        return null;
    }

    @Override
    public String visitDeclaracionFor(MiLenguajeParser.DeclaracionForContext ctx) {
        String nombreTipo = ctx.tipo().getText();
        String nombre     = ctx.ID().getText();
        emitir(Instruccion.declare(nombre, nombreTipo));
        String lugar = visit(ctx.expresion());
        if ("RETURN_VALUE".equals(lugar)) {
            emitir(Instruccion.returnValueAssign(nombre));
        } else {
            emitir(Instruccion.asignar(nombre, lugar));
        }
        return null;
    }

    @Override
    public String visitAsignacionFor(MiLenguajeParser.AsignacionForContext ctx) {
        String lugar = visit(ctx.expresion());
        emitir(Instruccion.asignar(ctx.ID().getText(), lugar));
        return null;
    }

    @Override
    public String visitActualizacionFor(MiLenguajeParser.ActualizacionForContext ctx) {
        String nombre = ctx.ID().getText();
        String op     = ctx.getChild(1).getText();
        if (op.equals("++") || op.equals("--")) {
            String t = nuevaTemp();
            emitir(Instruccion.binaria(t, nombre, op.equals("++") ? "+" : "-", "1"));
            emitir(Instruccion.asignar(nombre, t));
        } else {
            // ID = expr
            String lugar = visit(ctx.expresion());
            emitir(Instruccion.asignar(nombre, lugar));
        }
        return null;
    }

    /**
     * Para cada operación binaria:
     *   1) evaluar operando izquierdo (puede devolver temporal o literal)
     *   2) evaluar operando derecho
     *   3) crear nuevo temporal tN
     *   4) emitir instrucción binaria: tN = izq op der
     *   5) devolver tN como "lugar" del resultado
     *
     * Este patrón se aplica a OR, AND, igualdad, relacionales, aditivas y multiplicativas.
     */

    @Override
    public String visitExprOr(MiLenguajeParser.ExprOrContext ctx) {
        String izq = visit(ctx.expresion(0));
        String der = visit(ctx.expresion(1));
        String t   = nuevaTemp();
        emitir(Instruccion.binaria(t, izq, "||", der));
        return t;
    }

    @Override
    public String visitExprAnd(MiLenguajeParser.ExprAndContext ctx) {
        String izq = visit(ctx.expresion(0));
        String der = visit(ctx.expresion(1));
        String t   = nuevaTemp();
        emitir(Instruccion.binaria(t, izq, "&&", der));
        return t;
    }

    @Override
    public String visitExprIgualdad(MiLenguajeParser.ExprIgualdadContext ctx) {
        String izq = visit(ctx.expresion(0));
        String der = visit(ctx.expresion(1));
        String op  = ctx.getChild(1).getText();
        String t   = nuevaTemp();
        emitir(Instruccion.binaria(t, izq, op, der));
        return t;
    }

    @Override
    public String visitExprRelacional(MiLenguajeParser.ExprRelacionalContext ctx) {
        String izq = visit(ctx.expresion(0));
        String der = visit(ctx.expresion(1));
        String op  = ctx.getChild(1).getText();
        String t   = nuevaTemp();
        emitir(Instruccion.binaria(t, izq, op, der));
        return t;
    }

    @Override
    public String visitExprAditiva(MiLenguajeParser.ExprAditivaContext ctx) {
        String izq = visit(ctx.expresion(0));
        String der = visit(ctx.expresion(1));
        String op  = ctx.getChild(1).getText();
        String t   = nuevaTemp();
        emitir(Instruccion.binaria(t, izq, op, der));
        return t;
    }

    @Override
    public String visitExprMultiplicativa(MiLenguajeParser.ExprMultiplicativaContext ctx) {
        String izq = visit(ctx.expresion(0));
        String der = visit(ctx.expresion(1));
        String op  = ctx.getChild(1).getText();
        String t   = nuevaTemp();
        emitir(Instruccion.binaria(t, izq, op, der));
        return t;
    }

    @Override
    public String visitExprNot(MiLenguajeParser.ExprNotContext ctx) {
        String operando = visit(ctx.expresion());
        String t        = nuevaTemp();
        emitir(Instruccion.unaria(t, "!", operando));
        return t;
    }

    @Override
    public String visitExprNegativo(MiLenguajeParser.ExprNegativoContext ctx) {
        String operando = visit(ctx.expresion());
        String t        = nuevaTemp();
        emitir(Instruccion.unaria(t, "-", operando));
        return t;
    }

    @Override
    public String visitExprAgrupada(MiLenguajeParser.ExprAgrupadaContext ctx) {
        return visit(ctx.expresion());
    }

    @Override
    public String visitExprEntero(MiLenguajeParser.ExprEnteroContext ctx) {
        return ctx.NUM().getText();
    }

    @Override
    public String visitExprDecimal(MiLenguajeParser.ExprDecimalContext ctx) {
        return ctx.DECIMAL().getText();
    }

    @Override
    public String visitExprCaracter(MiLenguajeParser.ExprCaracterContext ctx) {
        return ctx.CHARACTER().getText();
    }

    @Override
    public String visitExprCadena(MiLenguajeParser.ExprCadenaContext ctx) {
        return ctx.CADENA().getText();
    }

    @Override
    public String visitExprTrue(MiLenguajeParser.ExprTrueContext ctx) {
        return "true";
    }

    @Override
    public String visitExprFalse(MiLenguajeParser.ExprFalseContext ctx) {
        return "false";
    }

    @Override
    public String visitExprIdentificador(MiLenguajeParser.ExprIdentificadorContext ctx) {
        // Devuelve el nombre de la variable; el llamador decide si usarlo o cargarlo.
        return ctx.ID().getText();
    }

    /**
     * visitExprArray:
     *
     * Acceso a array: ID '[' expresion ']'
     * Genera:
     *   tN = arr[idx]   (lectura)
     *
     * Devuelve tN para que el llamador use el valor.
     */

    @Override
    public String visitExprArray(MiLenguajeParser.ExprArrayContext ctx) {
        String idx = visit(ctx.expresion());
        String t   = nuevaTemp();
        emitir(Instruccion.asignarArrayR(t, ctx.ID().getText(), idx));
        return t;
    }

    /**
     * visitExprLlamada:
     *
     * - Emite CALL func_nombre, arg1, arg2...
     * - Devuelve "RETURN_VALUE" como lugar especial.
     *
     * El contexto que recibe este valor decide si asignarlo a una variable
     * o usarlo en otra expresión. La separación permite emitir CALL de forma
     * uniforme y manejar la asignación del valor retornado en el contexto.
     */

    @Override
    public String visitExprLlamada(MiLenguajeParser.ExprLlamadaContext ctx) {
        MiLenguajeParser.LlamadaFuncionContext llamada = ctx.llamadaFuncion();
        String argsStr = construirArgsCall(llamada.argumentos());
        emitir(Instruccion.call("func_" + llamada.ID().getText(), argsStr));
        return "RETURN_VALUE";
    }


    /**
     * Evalua cada argumento y construye la cadena "arg1, arg2, ..."
     * que se pone en la instrucción CALL.
     *
     * Observación:
     *   - Cada argumento puede ser un literal, un identificador o un temporal.
     *   - Se evalúan en orden izquierdo a derecho.
     */

    private String construirArgsCall(MiLenguajeParser.ArgumentosContext argumentos) {
        if (argumentos == null) return "";
        StringBuilder sb = new StringBuilder();
        List<MiLenguajeParser.ExpresionContext> args = argumentos.expresion();
        for (int i = 0; i < args.size(); i++) {
            String lugar = visit(args.get(i));
            if (i > 0) sb.append(", ");
            sb.append(lugar);
        }
        return sb.toString();
    }
}
