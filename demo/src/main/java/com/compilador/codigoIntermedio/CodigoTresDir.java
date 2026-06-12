package com.compilador.codigoIntermedio;

import com.compilador.MiLenguajeBaseVisitor;
import com.compilador.MiLenguajeParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Genera codigo de tres direcciones (TAC) recorriendo el AST.
 *
 * Extiende MiLenguajeBaseVisitor<String>:
 *   - Metodos de EXPRESIONES devuelven el "lugar" del resultado
 *     (un temporal "tN", un nombre de variable, o un literal).
 *   - Metodos de SENTENCIAS/DECLARACIONES devuelven null.
 *
 */
public class CodigoTresDir extends MiLenguajeBaseVisitor<String> {

    // =========================================================================
    // ESTADO INTERNO
    // =========================================================================

    private final List<Instruccion> instrucciones = new ArrayList<>();

    // Contador de temporales: t1, t2, t3, ...
    private int contTemp = 0;

    // Contador de etiquetas de if/else
    private int contIf   = 0;

    // Contador de etiquetas de while
    private int contWhile = 0;

    // Contador de etiquetas de for
    private int contFor  = 0;

    // Contexto: true = estamos en ambito global (para DECLARE global)
    private boolean esGlobal = true;

    // Nombre de la funcion actual (para PARAM y contexto)
    private String funcionActual = null;

    // =========================================================================
    // AUXILIARES
    // =========================================================================

    private String nuevaTemp() {
        return "t" + (++contTemp);
    }

    private void emitir(Instruccion i) {
        instrucciones.add(i);
    }

    public List<Instruccion> getInstrucciones() {
        return instrucciones;
    }

    public int getCantidadInstrucciones() {
        return instrucciones.size();
    }

    // =========================================================================
    // IMPRESION
    // =========================================================================

    /**
     * Imprime las instrucciones numeradas.
     * Las etiquetas y comentarios reciben numero de linea como el resto,
     * igual al formato del ejemplo_correcto donde todas las lineas estan numeradas.
     */
    public void imprimir() {
        System.out.println();
        for (int i = 0; i < instrucciones.size(); i++) {
            System.out.printf("  %2d: %s%n", i, instrucciones.get(i).toString());
        }
    }

    /**
     * Devuelve el codigo como lista de strings para guardar en archivo.
     */
    public List<String> getLineas() {
        List<String> lineas = new ArrayList<>();
        for (int i = 0; i < instrucciones.size(); i++) {
            lineas.add(String.format("%2d: %s", i, instrucciones.get(i).toString()));
        }
        return lineas;
    }

    // =========================================================================
    // VISITOR — Programa
    // =========================================================================

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

    // =========================================================================
    // VISITOR — Declaraciones de variables
    // =========================================================================

    /**
     * Declaracion de variable global: solo emite DECLARE (sin inicializador en TAC global).
     * La inicializacion de globales se hara dentro de main/funciones.
     */
    private void visitDeclaracionVariableGlobal(MiLenguajeParser.DeclaracionVariableContext ctx) {
        String nombreTipo = ctx.tipo().getText();
        String nombreVar  = ctx.ID().getText();

        // Verificar si es array: tipo ID '[' NUM ']' ';'
        if (ctx.getChildCount() > 3 && ctx.getChild(2).getText().equals("[")) {
            String size = ctx.NUM().getText();
            emitir(Instruccion.declareArray(nombreVar, size, nombreTipo));
        } else {
            emitir(Instruccion.declare(nombreVar, nombreTipo));
            // Si tiene inicializador global (raro pero posible)
            if (ctx.expresion() != null) {
                String lugar = visit(ctx.expresion());
                emitir(Instruccion.asignar(nombreVar, lugar));
            }
        }
    }

    /**
     * Declaracion de variable local (dentro de funcion): emite DECLARE + asignacion si aplica.
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
                    emitir(Instruccion.returnValueAssign(nombreVar));
                } else {
                    emitir(Instruccion.asignar(nombreVar, lugar));
                }
            }
        }
        return null;
    }

    // =========================================================================
    // VISITOR — Funciones
    // =========================================================================

    @Override
    public String visitDeclaracionFuncion(MiLenguajeParser.DeclaracionFuncionContext ctx) {
        String nombre = ctx.ID().getText();
        funcionActual = nombre;

        // Etiqueta func_nombre:
        emitir(Instruccion.etiqueta("func_" + nombre));

        // Emitir PARAM para cada parametro
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

    // =========================================================================
    // VISITOR — Bloque y sentencias
    // =========================================================================

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

    // =========================================================================
    // VISITOR — Asignacion
    // =========================================================================

    @Override
    public String visitAsignacion(MiLenguajeParser.AsignacionContext ctx) {
        // Dos formas: ID = expr  |  ID[expr] = expr
        if (ctx.getChildCount() > 3 && ctx.getChild(1).getText().equals("[")) {
            // ID '[' expr ']' '=' expr
            String idx = visit(ctx.expresion(0));
            String val = visit(ctx.expresion(1));
            emitir(Instruccion.asignarArrayW(ctx.ID().getText(), idx, val));
        } else {
            // ID '=' expr
            String lugar = visit(ctx.expresion(0));
            if ("RETURN_VALUE".equals(lugar)) {
                emitir(Instruccion.returnValueAssign(ctx.ID().getText()));
            } else {
                emitir(Instruccion.asignar(ctx.ID().getText(), lugar));
            }
        }
        return null;
    }

    // =========================================================================
    // VISITOR — Incremento / Decremento (como sentencia)
    // =========================================================================

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

    // =========================================================================
    // VISITOR — LlamadaFuncion como sentencia
    // =========================================================================

    @Override
    public String visitLlamadaFuncion(MiLenguajeParser.LlamadaFuncionContext ctx) {
        // Emitir CALL sin capturar el valor de retorno
        String argsStr = construirArgsCall(ctx.argumentos());
        emitir(Instruccion.call("func_" + ctx.ID().getText(), argsStr));
        return null;
    }

    // =========================================================================
    // VISITOR — Return
    // =========================================================================

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

    // =========================================================================
    // VISITOR — Break / Continue
    // =========================================================================

    @Override
    public String visitSentenciaBreak(MiLenguajeParser.SentenciaBreakContext ctx) {
        // Se genera un goto a la etiqueta de fin del bucle mas cercano.
        // Como no tenemos stack de etiquetas aqui, emitimos un goto simbolico.
        // En un compilador real se usaria un stack de etiquetas de salida.
        emitir(Instruccion.saltoIncond("BREAK"));
        return null;
    }

    @Override
    public String visitSentenciaContinue(MiLenguajeParser.SentenciaContinueContext ctx) {
        emitir(Instruccion.saltoIncond("CONTINUE"));
        return null;
    }

    // =========================================================================
    // VISITOR — If
    // =========================================================================

    /**
     * Patron TAC para IF (igual al ejemplo_correcto):
     *
     *   tX = <condicion>
     *   if tX goto THEN_N
     *   goto END_IF_N        (sin else)  |  goto ELSE_N  (con else)
     *   THEN_N:
     *   ...bloque then...
     *   goto END_IF_N        (con else)
     *   ELSE_N:              (con else)
     *   ...bloque else...
     *   END_IF_N:
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

    // =========================================================================
    // VISITOR — While
    // =========================================================================

    /**
     * Patron TAC para WHILE:
     *   WHILE_START_N:
     *   tX = <condicion>
     *   if tX goto WHILE_BODY_N
     *   goto WHILE_END_N
     *   WHILE_BODY_N:
     *   ...cuerpo...
     *   goto WHILE_START_N
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

    // =========================================================================
    // VISITOR — For
    // =========================================================================

    /**
     * Patron TAC para FOR:
     *   <inicializacion>
     *   FOR_START_N:
     *   tX = <condicion>
     *   if tX goto FOR_BODY_N
     *   goto FOR_END_N
     *   FOR_BODY_N:
     *   ...cuerpo...
     *   <actualizacion>
     *   goto FOR_START_N
     *   FOR_END_N:
     */
    @Override
    public String visitSentenciaFor(MiLenguajeParser.SentenciaForContext ctx) {
        int n = ++contFor;
        String labelStart = "FOR_START_" + n;
        String labelBody  = "FOR_BODY_" + n;
        String labelEnd   = "FOR_END_" + n;

        // Inicializacion
        if (ctx.inicializacionFor() != null) {
            visitInicializacionFor(ctx.inicializacionFor());
        }

        emitir(Instruccion.etiqueta(labelStart));

        // Condicion (puede ser vacia → for infinito)
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

    // =========================================================================
    // VISITOR — Expresiones binarias
    // =========================================================================

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

    // =========================================================================
    // VISITOR — Expresiones unarias
    // =========================================================================

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

    // =========================================================================
    // VISITOR — Literales (NO crean temporal, devuelven el valor directo)
    // =========================================================================

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

    // =========================================================================
    // VISITOR — Identificadores y arrays
    // =========================================================================

    @Override
    public String visitExprIdentificador(MiLenguajeParser.ExprIdentificadorContext ctx) {
        return ctx.ID().getText();
    }

    /**
     * Acceso a array: ID '[' expresion ']'
     * Genera:  tN = arr[idx]
     */
    @Override
    public String visitExprArray(MiLenguajeParser.ExprArrayContext ctx) {
        String idx = visit(ctx.expresion());
        String t   = nuevaTemp();
        emitir(Instruccion.asignarArrayR(t, ctx.ID().getText(), idx));
        return t;
    }

    // =========================================================================
    // VISITOR — Llamada a funcion como expresion
    // =========================================================================

    /**
     * Llamada a funcion dentro de una expresion.
     * Patron:
     *   CALL func_nombre, arg1, arg2
     *   dest = RETURN_VALUE
     *
     * Devuelve dest para que el llamador pueda usarlo.
     *
     * Ejemplo: estado = sumar(temp, 5)
     *   CALL func_sumar, temp, 5
     *   estado = RETURN_VALUE     (esto lo emite visitAsignacion)
     *
     * Nota: el valor de retorno se captura con "dest = RETURN_VALUE"
     * solo cuando el resultado de la llamada se asigna a algo.
     * visitExprLlamada devuelve "RETURN_VALUE" como lugar,
     * y el visitor de asignacion/declaracion emite la asignacion.
     */
    @Override
    public String visitExprLlamada(MiLenguajeParser.ExprLlamadaContext ctx) {
        // El nodo ExprLlamada envuelve un LlamadaFuncion
        MiLenguajeParser.LlamadaFuncionContext llamada = ctx.llamadaFuncion();
        String argsStr = construirArgsCall(llamada.argumentos());
        emitir(Instruccion.call("func_" + llamada.ID().getText(), argsStr));
        return "RETURN_VALUE";
    }

    // =========================================================================
    // AUXILIAR — construir string de argumentos para CALL
    // =========================================================================

    /**
     * Evalua cada argumento y construye la cadena "arg1, arg2, ..."
     * que se pone en la instruccion CALL.
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