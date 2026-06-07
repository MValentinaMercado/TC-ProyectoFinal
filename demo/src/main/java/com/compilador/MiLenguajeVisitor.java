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
	 * Visit a parse tree produced by the {@code OrExpr}
	 * labeled alternative in {@link MiLenguajeParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOrExpr(MiLenguajeParser.OrExprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JustTerm}
	 * labeled alternative in {@link MiLenguajeParser#expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJustTerm(MiLenguajeParser.JustTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code AndTerm}
	 * labeled alternative in {@link MiLenguajeParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAndTerm(MiLenguajeParser.AndTermContext ctx);
	/**
	 * Visit a parse tree produced by the {@code JustFactor}
	 * labeled alternative in {@link MiLenguajeParser#term}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitJustFactor(MiLenguajeParser.JustFactorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code NotFactor}
	 * labeled alternative in {@link MiLenguajeParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNotFactor(MiLenguajeParser.NotFactorContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Parentheses}
	 * labeled alternative in {@link MiLenguajeParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParentheses(MiLenguajeParser.ParenthesesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code Number}
	 * labeled alternative in {@link MiLenguajeParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(MiLenguajeParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by the {@code TrueLit}
	 * labeled alternative in {@link MiLenguajeParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTrueLit(MiLenguajeParser.TrueLitContext ctx);
	/**
	 * Visit a parse tree produced by the {@code FalseLit}
	 * labeled alternative in {@link MiLenguajeParser#factor}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFalseLit(MiLenguajeParser.FalseLitContext ctx);
	/**
	 * Visit a parse tree produced by {@link MiLenguajeParser#token}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitToken(MiLenguajeParser.TokenContext ctx);
}