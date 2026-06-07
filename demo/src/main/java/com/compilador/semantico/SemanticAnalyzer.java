package com.compilador.semantico;

import com.compilador.MiLenguajeBaseVisitor;
import com.compilador.MiLenguajeParser;

import java.util.ArrayList;
import java.util.List;

public class SemanticAnalyzer extends MiLenguajeBaseVisitor<String> {

    private final SymbolTable tabla = new SymbolTable();

    private final List<SemanticError> errores  = new ArrayList<>();
    private final List<SemanticError> warnings = new ArrayList<>();

    // =========================================================
    // UTIL
    // =========================================================

    private void error(int line, int col, String msg) {
        errores.add(new SemanticError(line, col, msg, SemanticError.Severidad.ERROR));
    }

    private void warning(int line, int col, String msg) {
        warnings.add(new SemanticError(line, col, msg, SemanticError.Severidad.ADVERTENCIA));
    }

    public SymbolTable            getTablaSimbolos()  { return tabla;    }
    public List<SemanticError>    getErrores()        { return errores;  }
    public List<SemanticError>    getAdvertencias()   { return warnings; }
    public boolean                hayAdvertencias()   { return !warnings.isEmpty(); }
    public boolean                hayErrores()        { return !errores.isEmpty();  }

    // =========================================================
    // PROGRAMA
    // =========================================================

    @Override
    public String visitPrograma(MiLenguajeParser.ProgramaContext ctx) {
        return visitChildren(ctx);
    }

    // =========================================================
    // VARIABLES
    // =========================================================

    @Override
    public String visitDeclaracionVariable(MiLenguajeParser.DeclaracionVariableContext ctx) {

        String tipo   = ctx.tipo().getText();
        String nombre = ctx.ID().getText();

        int line = ctx.ID().getSymbol().getLine();
        // Columna basada en el tipo (primer token de la declaracion)
        int col  = ctx.tipo().start.getCharPositionInLine();

        if (tabla.estaDeclaradoLocalmente(nombre)) {
            error(line, col,
                    "La variable '" + nombre + "' ya esta declarada en el ambito '" +
                            resolverNombreAmbitoActual() + "' (linea " + line + ", columna " + col + ")");
            return TypeSystem.ERROR;
        }

        Symbol sym;
        // Detectar declaracion de array: tipo ID [ NUM ] ;
        if (ctx.NUM() != null) {
            int tamanio = Integer.parseInt(ctx.NUM().getText());
            sym = Symbol.array(nombre, tipo, tamanio, line, col);
        } else {
            sym = Symbol.variable(nombre, tipo, false, line, col);
        }
        tabla.definir(sym);

        if (ctx.expresion() != null) {
            String tipoExpr = visit(ctx.expresion());
            if (!TypeSystem.esCompatibleAsignacion(tipo, tipoExpr)) {
                error(line, col, TypeSystem.msgIncompatible(tipo, tipoExpr));
            }
            sym.setInicializado(true);
        }

        return TypeSystem.VOID;
    }

    // =========================================================
    // FUNCIONES  (cubre tipo = void tambien, ya que void es parte de 'tipo')
    // =========================================================

    @Override
    public String visitDeclaracionFuncion(MiLenguajeParser.DeclaracionFuncionContext ctx) {

        String nombre      = ctx.ID().getText();
        String tipoRetorno = ctx.tipo().getText();

        int line = ctx.ID().getSymbol().getLine();
        int col  = ctx.tipo().start.getCharPositionInLine();

        if (tabla.estaDeclaradoLocalmente(nombre)) {
            error(line, col,
                    "La funcion '" + nombre + "' ya esta declarada en el ambito '" +
                            resolverNombreAmbitoActual() + "'");
            return TypeSystem.ERROR;
        }

        Symbol funSym = Symbol.funcion(nombre, tipoRetorno, line, col);
        tabla.definir(funSym);

        tabla.entrarScope(nombre);

        if (ctx.parametros() != null) {
            for (MiLenguajeParser.ParametroContext p : ctx.parametros().parametro()) {
                String tipo = p.tipo().getText();
                String id   = p.ID().getText();

                int pLine = p.ID().getSymbol().getLine();
                int pCol  = p.ID().getSymbol().getCharPositionInLine();

                Symbol paramSym = Symbol.parametro(id, tipo, pLine, pCol);
                paramSym.setUsado(true); // parametros se consideran usados
                tabla.definir(paramSym);

                // Agregar tipo a la firma de la funcion
                funSym.addParamType(tipo);
            }
        }

        visit(ctx.bloque());

        // Detectar variables no usadas en el scope de la funcion
        checkUnusedVariables(tabla.getScopeActual());

        tabla.salirScope();

        return TypeSystem.VOID;
    }

    // =========================================================
    // BLOQUE
    // =========================================================

    @Override
    public String visitBloque(MiLenguajeParser.BloqueContext ctx) {
        tabla.entrarScope("bloque");
        visitChildren(ctx);
        // Detectar variables no usadas en bloques internos
        checkUnusedVariables(tabla.getScopeActual());
        tabla.salirScope();
        return TypeSystem.VOID;
    }

    private void checkUnusedVariables(Scope scope) {
        for (Symbol s : scope.getSimbolos()) {
            if (s.getCategoria() == Symbol.Categoria.VARIABLE && !s.isUsado()) {
                warning(s.getLinea(), s.getColumna(),
                        "Variable '" + s.getNombre() + "' declarada pero nunca utilizada en el ambito '" +
                                scope.getNombreAmbito() + "' (linea " + s.getLinea() + ")");
            }
        }
    }

    // =========================================================
    // ASIGNACION
    // =========================================================

    @Override
    public String visitAsignacion(MiLenguajeParser.AsignacionContext ctx) {

        String nombre = ctx.ID().getText();

        int line = ctx.ID().getSymbol().getLine();
        int col  = ctx.ID().getSymbol().getCharPositionInLine();

        Symbol sym = tabla.resolver(nombre);

        if (sym == null) {
            error(line, col,
                    "Variable '" + nombre + "' no declarada en el ambito '" +
                            resolverNombreAmbitoActual() + "' (linea " + line + ")");
            if (ctx.expresion(0) != null) visit(ctx.expresion(0));
            return TypeSystem.ERROR;
        }

        // Error: asignar valor a una funcion
        if (sym.getCategoria() == Symbol.Categoria.FUNCION) {
            error(line, col,
                    "No se puede asignar valor a '" + nombre + "' porque no es una variable (linea " + line + ")");
            if (ctx.expresion(0) != null) visit(ctx.expresion(0));
            return TypeSystem.ERROR;
        }

        sym.setUsado(true);

        String tipoExpr = ctx.expresion(0) != null ? visit(ctx.expresion(0)) : TypeSystem.ERROR;

        if (!TypeSystem.esCompatibleAsignacion(sym.getTipo(), tipoExpr)) {
            error(line, col, TypeSystem.msgIncompatible(sym.getTipo(), tipoExpr));
        }

        sym.setInicializado(true);
        return sym.getTipo();
    }

    // =========================================================
    // CONTROL
    // =========================================================

    @Override
    public String visitSentenciaIf(MiLenguajeParser.SentenciaIfContext ctx) {
        visit(ctx.expresion());
        visit(ctx.bloque(0));
        if (ctx.bloque().size() > 1) visit(ctx.bloque(1));
        return TypeSystem.VOID;
    }

    @Override
    public String visitSentenciaWhile(MiLenguajeParser.SentenciaWhileContext ctx) {
        visit(ctx.expresion());
        visit(ctx.bloque());
        return TypeSystem.VOID;
    }

    // =========================================================
    // LLAMADA A FUNCION (como sentencia)
    // =========================================================

    @Override
    public String visitLlamadaFuncion(MiLenguajeParser.LlamadaFuncionContext ctx) {
        String nombre = ctx.ID().getText();
        Symbol sym = tabla.resolver(nombre);
        if (sym != null) sym.setUsado(true);
        if (ctx.argumentos() != null) {
            for (MiLenguajeParser.ExpresionContext arg : ctx.argumentos().expresion()) {
                visit(arg);
            }
        }
        return sym != null ? sym.getTipo() : TypeSystem.ERROR;
    }

    // =========================================================
    // EXPRESIONES
    // =========================================================

    @Override
    public String visitExprAditiva(MiLenguajeParser.ExprAditivaContext ctx) {
        return TypeSystem.inferirAritmetico(visit(ctx.expresion(0)), visit(ctx.expresion(1)));
    }

    @Override
    public String visitExprMultiplicativa(MiLenguajeParser.ExprMultiplicativaContext ctx) {
        return TypeSystem.inferirAritmetico(visit(ctx.expresion(0)), visit(ctx.expresion(1)));
    }

    @Override
    public String visitExprRelacional(MiLenguajeParser.ExprRelacionalContext ctx) {
        return TypeSystem.inferirRelacional(visit(ctx.expresion(0)), visit(ctx.expresion(1)));
    }

    @Override
    public String visitExprIgualdad(MiLenguajeParser.ExprIgualdadContext ctx) {
        return TypeSystem.inferirIgualdad(visit(ctx.expresion(0)), visit(ctx.expresion(1)));
    }

    @Override
    public String visitExprAnd(MiLenguajeParser.ExprAndContext ctx) {
        return TypeSystem.inferirLogico(visit(ctx.expresion(0)), visit(ctx.expresion(1)));
    }

    @Override
    public String visitExprOr(MiLenguajeParser.ExprOrContext ctx) {
        return TypeSystem.inferirLogico(visit(ctx.expresion(0)), visit(ctx.expresion(1)));
    }

    @Override
    public String visitExprNot(MiLenguajeParser.ExprNotContext ctx) {
        return TypeSystem.inferirNot(visit(ctx.expresion()));
    }

    @Override
    public String visitExprNegativo(MiLenguajeParser.ExprNegativoContext ctx) {
        return TypeSystem.inferirNegativo(visit(ctx.expresion()));
    }

    @Override
    public String visitExprAgrupada(MiLenguajeParser.ExprAgrupadaContext ctx) {
        return visit(ctx.expresion());
    }

    // =========================================================
    // LLAMADA A FUNCION como expresion (estado = sumar(a, b))
    // =========================================================

    @Override
    public String visitExprLlamada(MiLenguajeParser.ExprLlamadaContext ctx) {
        return visitLlamadaFuncion(ctx.llamadaFuncion());
    }

    // =========================================================
    // LITERALES
    // =========================================================

    @Override public String visitExprEntero(MiLenguajeParser.ExprEnteroContext ctx)    { return TypeSystem.INT;    }
    @Override public String visitExprDecimal(MiLenguajeParser.ExprDecimalContext ctx)   { return TypeSystem.DOUBLE; }
    @Override public String visitExprCaracter(MiLenguajeParser.ExprCaracterContext ctx) { return TypeSystem.CHAR;   }
    @Override public String visitExprCadena(MiLenguajeParser.ExprCadenaContext ctx)     { return TypeSystem.STRING; }
    @Override public String visitExprTrue(MiLenguajeParser.ExprTrueContext ctx)         { return TypeSystem.BOOL;   }
    @Override public String visitExprFalse(MiLenguajeParser.ExprFalseContext ctx)       { return TypeSystem.BOOL;   }

    // =========================================================
    // IDENTIFICADOR como expresion
    // =========================================================

    @Override
    public String visitExprIdentificador(MiLenguajeParser.ExprIdentificadorContext ctx) {

        String name = ctx.ID().getText();
        Symbol sym  = tabla.resolver(name);

        if (sym == null) {
            error(ctx.start.getLine(), ctx.start.getCharPositionInLine(),
                    "Variable '" + name + "' no declarada en el ambito '" +
                            resolverNombreAmbitoActual() + "'");
            return TypeSystem.ERROR;
        }

        sym.setUsado(true);
        return sym.getTipo();
    }

    // =========================================================
    // AUXILIAR: nombre legible del ambito actual
    // =========================================================

    private String resolverNombreAmbitoActual() {
        return tabla.getScopeActual().getNombreAmbito();
    }
}
