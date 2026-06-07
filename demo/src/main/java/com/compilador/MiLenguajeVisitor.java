// Generated from com\compilador\MiLenguaje.g4 by ANTLR 4.9.3
package com.compilador;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link MiLenguajeParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface MiLenguajeVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#programa}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPrograma(MiLenguajeParser.ProgramaContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#declaracion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracion(MiLenguajeParser.DeclaracionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#declaracionVariable}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracionVariable(MiLenguajeParser.DeclaracionVariableContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#tipo}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTipo(MiLenguajeParser.TipoContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#declaracionFuncion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracionFuncion(MiLenguajeParser.DeclaracionFuncionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#parametros}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParametros(MiLenguajeParser.ParametrosContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#parametro}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParametro(MiLenguajeParser.ParametroContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#bloque}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBloque(MiLenguajeParser.BloqueContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#sentencia}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSentencia(MiLenguajeParser.SentenciaContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#incremento}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitIncremento(MiLenguajeParser.IncrementoContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#sentenciaIf}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSentenciaIf(MiLenguajeParser.SentenciaIfContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#sentenciaWhile}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSentenciaWhile(MiLenguajeParser.SentenciaWhileContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#sentenciaFor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSentenciaFor(MiLenguajeParser.SentenciaForContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#inicializacionFor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInicializacionFor(MiLenguajeParser.InicializacionForContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#declaracionFor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDeclaracionFor(MiLenguajeParser.DeclaracionForContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#asignacionFor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsignacionFor(MiLenguajeParser.AsignacionForContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#actualizacionFor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitActualizacionFor(MiLenguajeParser.ActualizacionForContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#sentenciaBreak}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSentenciaBreak(MiLenguajeParser.SentenciaBreakContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#sentenciaContinue}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSentenciaContinue(MiLenguajeParser.SentenciaContinueContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#asignacion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAsignacion(MiLenguajeParser.AsignacionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#retorno}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRetorno(MiLenguajeParser.RetornoContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#llamadaFuncion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitLlamadaFuncion(MiLenguajeParser.LlamadaFuncionContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#argumentos}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitArgumentos(MiLenguajeParser.ArgumentosContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprAgrupada}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprAgrupada(MiLenguajeParser.ExprAgrupadaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprIgualdad}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprIgualdad(MiLenguajeParser.ExprIgualdadContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprRelacional}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprRelacional(MiLenguajeParser.ExprRelacionalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprCadena}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprCadena(MiLenguajeParser.ExprCadenaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprIdentificador}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprIdentificador(MiLenguajeParser.ExprIdentificadorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprNegativo}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprNegativo(MiLenguajeParser.ExprNegativoContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprFalse}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprFalse(MiLenguajeParser.ExprFalseContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprCaracter}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprCaracter(MiLenguajeParser.ExprCaracterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprTrue}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprTrue(MiLenguajeParser.ExprTrueContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprLlamada}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprLlamada(MiLenguajeParser.ExprLlamadaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprNot}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprNot(MiLenguajeParser.ExprNotContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprArray}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprArray(MiLenguajeParser.ExprArrayContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprAditiva}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprAditiva(MiLenguajeParser.ExprAditivaContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprAnd}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprAnd(MiLenguajeParser.ExprAndContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprDecimal}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprDecimal(MiLenguajeParser.ExprDecimalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprNumero}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprNumero(MiLenguajeParser.ExprNumeroContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprOr}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprOr(MiLenguajeParser.ExprOrContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ExprMultiplicativa}
	 * labeled alternative in {@link MiLenguajeParser#expresion}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitExprMultiplicativa(MiLenguajeParser.ExprMultiplicativaContext ctx);
}