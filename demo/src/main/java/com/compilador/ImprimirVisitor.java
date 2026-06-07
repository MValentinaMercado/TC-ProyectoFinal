package com.compilador;

public class ImprimirVisitor extends MiLenguajeBaseVisitor<Void> {

    private int nivel = 0;

    private void print(String msg) {
        System.out.println("  ".repeat(nivel) + msg);
    }

    // =========================================================
    // PROGRAMA
    // =========================================================

    @Override
    public Void visitPrograma(MiLenguajeParser.ProgramaContext ctx) {
        print("PROGRAMA");
        nivel++;
        visitChildren(ctx);
        nivel--;
        return null;
    }

    // =========================================================
    // DECLARACIONES
    // =========================================================

    @Override
    public Void visitDeclaracionVariable(MiLenguajeParser.DeclaracionVariableContext ctx) {

        String tipo = ctx.tipo().getText();
        String id = ctx.ID().getText();

        print("DECLARACION VARIABLE: " + tipo + " " + id);

        if (ctx.expresion() != null) {
            nivel++;
            print("INICIALIZACION:");
            nivel++;
            visit(ctx.expresion());
            nivel -= 2;
        }

        return null;
    }

    @Override
    public Void visitDeclaracionFuncion(MiLenguajeParser.DeclaracionFuncionContext ctx) {

        String tipo = ctx.tipo().getText();
        String id = ctx.ID().getText();

        print("FUNCION: " + tipo + " " + id);

        nivel++;

        if (ctx.parametros() != null) {
            print("PARAMETROS:");
            nivel++;
            visit(ctx.parametros());
            nivel--;
        }

        print("BLOQUE:");
        visit(ctx.bloque());

        nivel--;

        return null;
    }

    // =========================================================
    // PARAMETROS
    // =========================================================

    @Override
    public Void visitParametros(MiLenguajeParser.ParametrosContext ctx) {
        return visitChildren(ctx);
    }

    @Override
    public Void visitParametro(MiLenguajeParser.ParametroContext ctx) {
        print("PARAMETRO: " + ctx.tipo().getText() + " " + ctx.ID().getText());
        return null;
    }

    // =========================================================
    // BLOQUE
    // =========================================================

    @Override
    public Void visitBloque(MiLenguajeParser.BloqueContext ctx) {
        print("BLOQUE {");
        nivel++;
        visitChildren(ctx);
        nivel--;
        print("}");
        return null;
    }

    // =========================================================
    // SENTENCIAS
    // =========================================================

    @Override
    public Void visitAsignacion(MiLenguajeParser.AsignacionContext ctx) {
        print("ASIGNACION: " + ctx.ID().getText());
        nivel++;

        visit(ctx.expresion(0));

        if (ctx.expresion().size() > 1) {
            visit(ctx.expresion(1)); // array assignment
        }

        nivel--;
        return null;
    }

    @Override
    public Void visitIncremento(MiLenguajeParser.IncrementoContext ctx) {
        print("INCREMENTO/DECREMENTO: " + ctx.ID().getText() + " " + ctx.getChild(1).getText());
        return null;
    }

    @Override
    public Void visitSentenciaIf(MiLenguajeParser.SentenciaIfContext ctx) {

        print("IF");

        nivel++;
        print("CONDICION:");
        nivel++;
        visit(ctx.expresion());
        nivel -= 2;

        print("BLOQUE IF:");
        nivel++;
        visit(ctx.bloque(0));
        nivel--;

        if (ctx.bloque().size() > 1) {
            print("BLOQUE ELSE:");
            nivel++;
            visit(ctx.bloque(1));
            nivel--;
        }

        nivel--;
        return null;
    }

    @Override
    public Void visitSentenciaWhile(MiLenguajeParser.SentenciaWhileContext ctx) {

        print("WHILE");

        nivel++;
        print("CONDICION:");
        nivel++;
        visit(ctx.expresion());
        nivel--;

        print("CUERPO:");
        visit(ctx.bloque());
        nivel--;

        return null;
    }

    @Override
    public Void visitSentenciaFor(MiLenguajeParser.SentenciaForContext ctx) {
        print("FOR");
        nivel++;
        visitChildren(ctx);
        nivel--;
        return null;
    }

    @Override
    public Void visitSentenciaBreak(MiLenguajeParser.SentenciaBreakContext ctx) {
        print("BREAK");
        return null;
    }

    @Override
    public Void visitSentenciaContinue(MiLenguajeParser.SentenciaContinueContext ctx) {
        print("CONTINUE");
        return null;
    }

    @Override
    public Void visitRetorno(MiLenguajeParser.RetornoContext ctx) {
        print("RETURN");

        if (ctx.expresion() != null) {
            nivel++;
            visit(ctx.expresion());
            nivel--;
        }

        return null;
    }

    @Override
    public Void visitLlamadaFuncion(MiLenguajeParser.LlamadaFuncionContext ctx) {
        print("LLAMADA FUNCION: " + ctx.ID().getText());

        if (ctx.argumentos() != null) {
            nivel++;
            visit(ctx.argumentos());
            nivel--;
        }

        return null;
    }

    // =========================================================
    // EXPRESIONES
    // =========================================================

    @Override
    public Void visitExprAditiva(MiLenguajeParser.ExprAditivaContext ctx) {
        print("EXPRESION ARITMETICA: " + ctx.getChild(1).getText());
        nivel++;
        visit(ctx.expresion(0));
        visit(ctx.expresion(1));
        nivel--;
        return null;
    }

    @Override
    public Void visitExprMultiplicativa(MiLenguajeParser.ExprMultiplicativaContext ctx) {
        print("EXPRESION ARITMETICA: " + ctx.getChild(1).getText());
        nivel++;
        visit(ctx.expresion(0));
        visit(ctx.expresion(1));
        nivel--;
        return null;
    }

    @Override
    public Void visitExprRelacional(MiLenguajeParser.ExprRelacionalContext ctx) {
        print("RELACIONAL: " + ctx.getChild(1).getText());
        nivel++;
        visit(ctx.expresion(0));
        visit(ctx.expresion(1));
        nivel--;
        return null;
    }

    @Override
    public Void visitExprIgualdad(MiLenguajeParser.ExprIgualdadContext ctx) {
        print("IGUALDAD: " + ctx.getChild(1).getText());
        nivel++;
        visit(ctx.expresion(0));
        visit(ctx.expresion(1));
        nivel--;
        return null;
    }

    @Override
    public Void visitExprAnd(MiLenguajeParser.ExprAndContext ctx) {
        print("AND");
        nivel++;
        visit(ctx.expresion(0));
        visit(ctx.expresion(1));
        nivel--;
        return null;
    }

    @Override
    public Void visitExprOr(MiLenguajeParser.ExprOrContext ctx) {
        print("OR");
        nivel++;
        visit(ctx.expresion(0));
        visit(ctx.expresion(1));
        nivel--;
        return null;
    }

    @Override
    public Void visitExprNot(MiLenguajeParser.ExprNotContext ctx) {
        print("NOT");
        nivel++;
        visit(ctx.expresion());
        nivel--;
        return null;
    }

    @Override
    public Void visitExprNegativo(MiLenguajeParser.ExprNegativoContext ctx) {
        print("NEGATIVO");
        nivel++;
        visit(ctx.expresion());
        nivel--;
        return null;
    }

    @Override
    public Void visitExprAgrupada(MiLenguajeParser.ExprAgrupadaContext ctx) {
        print("( )");
        nivel++;
        visit(ctx.expresion());
        nivel--;
        return null;
    }

    // =========================================================
    // LITERALES (CORREGIDO SEGUN TU GRAMATICA)
    // =========================================================

    @Override
    public Void visitExprEntero(MiLenguajeParser.ExprEnteroContext ctx) {
        print("NUMERO: " + ctx.NUM().getText());
        return null;
    }

    @Override
    public Void visitExprDecimal(MiLenguajeParser.ExprDecimalContext ctx) {
        print("DECIMAL: " + ctx.DECIMAL().getText());
        return null;
    }

    @Override
    public Void visitExprCaracter(MiLenguajeParser.ExprCaracterContext ctx) {
        print("CHAR: " + ctx.CHARACTER().getText());
        return null;
    }

    @Override
    public Void visitExprCadena(MiLenguajeParser.ExprCadenaContext ctx) {
        print("STRING: " + ctx.CADENA().getText());
        return null;
    }

    @Override
    public Void visitExprTrue(MiLenguajeParser.ExprTrueContext ctx) {
        print("TRUE");
        return null;
    }

    @Override
    public Void visitExprFalse(MiLenguajeParser.ExprFalseContext ctx) {
        print("FALSE");
        return null;
    }

    @Override
    public Void visitExprIdentificador(MiLenguajeParser.ExprIdentificadorContext ctx) {
        print("ID: " + ctx.ID().getText());
        return null;
    }
}