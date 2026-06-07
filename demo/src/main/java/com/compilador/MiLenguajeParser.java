// Generated from com\compilador\MiLenguaje.g4 by ANTLR 4.9.3
package com.compilador;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class MiLenguajeParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.9.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		PA=1, PC=2, CA=3, CC=4, LA=5, LC=6, PYC=7, COMA=8, IGUAL=9, EQL=10, DISTINTO=11, 
		MAYOR_IGUAL=12, MENOR_IGUAL=13, MAYOR=14, MENOR=15, INC=16, DEC=17, SUM=18, 
		RES=19, MUL=20, DIV=21, MOD=22, OR=23, AND=24, NOT=25, IF=26, ELSE=27, 
		WHILE=28, FOR=29, BREAK=30, CONTINUE=31, RETURN=32, INT=33, DOUBLE=34, 
		CHAR=35, STRING=36, BOOL=37, VOID=38, TRUE=39, FALSE=40, ID=41, DECIMAL=42, 
		NUM=43, CHARACTER=44, CADENA=45, COMENTARIO_LINEA=46, COMENTARIO_BLOQUE=47, 
		WS=48, OTRO=49;
	public static final int
		RULE_programa = 0, RULE_declaracion = 1, RULE_declaracionVariable = 2, 
		RULE_tipo = 3, RULE_declaracionFuncion = 4, RULE_parametros = 5, RULE_parametro = 6, 
		RULE_bloque = 7, RULE_sentencia = 8, RULE_sentenciaIf = 9, RULE_sentenciaWhile = 10, 
		RULE_sentenciaFor = 11, RULE_inicializacionFor = 12, RULE_declaracionFor = 13, 
		RULE_asignacionFor = 14, RULE_actualizacionFor = 15, RULE_sentenciaBreak = 16, 
		RULE_sentenciaContinue = 17, RULE_retorno = 18, RULE_asignacion = 19, 
		RULE_llamadaFuncion = 20, RULE_argumentos = 21, RULE_incremento = 22, 
		RULE_expresion = 23;
	private static String[] makeRuleNames() {
		return new String[] {
			"programa", "declaracion", "declaracionVariable", "tipo", "declaracionFuncion", 
			"parametros", "parametro", "bloque", "sentencia", "sentenciaIf", "sentenciaWhile", 
			"sentenciaFor", "inicializacionFor", "declaracionFor", "asignacionFor", 
			"actualizacionFor", "sentenciaBreak", "sentenciaContinue", "retorno", 
			"asignacion", "llamadaFuncion", "argumentos", "incremento", "expresion"
		};
	}
	public static final String[] ruleNames = makeRuleNames();

	private static String[] makeLiteralNames() {
		return new String[] {
			null, "'('", "')'", "'['", "']'", "'{'", "'}'", "';'", "','", "'='", 
			"'=='", "'!='", "'>='", "'<='", "'>'", "'<'", "'++'", "'--'", "'+'", 
			"'-'", "'*'", "'/'", "'%'", "'||'", "'&&'", "'!'", "'if'", "'else'", 
			"'while'", "'for'", "'break'", "'continue'", "'return'", "'int'", "'double'", 
			"'char'", "'string'", "'bool'", "'void'", "'true'", "'false'"
		};
	}
	private static final String[] _LITERAL_NAMES = makeLiteralNames();
	private static String[] makeSymbolicNames() {
		return new String[] {
			null, "PA", "PC", "CA", "CC", "LA", "LC", "PYC", "COMA", "IGUAL", "EQL", 
			"DISTINTO", "MAYOR_IGUAL", "MENOR_IGUAL", "MAYOR", "MENOR", "INC", "DEC", 
			"SUM", "RES", "MUL", "DIV", "MOD", "OR", "AND", "NOT", "IF", "ELSE", 
			"WHILE", "FOR", "BREAK", "CONTINUE", "RETURN", "INT", "DOUBLE", "CHAR", 
			"STRING", "BOOL", "VOID", "TRUE", "FALSE", "ID", "DECIMAL", "NUM", "CHARACTER", 
			"CADENA", "COMENTARIO_LINEA", "COMENTARIO_BLOQUE", "WS", "OTRO"
		};
	}
	private static final String[] _SYMBOLIC_NAMES = makeSymbolicNames();
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}

	@Override
	public String getGrammarFileName() { return "MiLenguaje.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public MiLenguajeParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	public static class ProgramaContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(MiLenguajeParser.EOF, 0); }
		public List<DeclaracionContext> declaracion() {
			return getRuleContexts(DeclaracionContext.class);
		}
		public DeclaracionContext declaracion(int i) {
			return getRuleContext(DeclaracionContext.class,i);
		}
		public ProgramaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_programa; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitPrograma(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ProgramaContext programa() throws RecognitionException {
		ProgramaContext _localctx = new ProgramaContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_programa);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(51);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << DOUBLE) | (1L << CHAR) | (1L << STRING) | (1L << BOOL) | (1L << VOID))) != 0)) {
				{
				{
				setState(48);
				declaracion();
				}
				}
				setState(53);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(54);
			match(EOF);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclaracionContext extends ParserRuleContext {
		public DeclaracionVariableContext declaracionVariable() {
			return getRuleContext(DeclaracionVariableContext.class,0);
		}
		public DeclaracionFuncionContext declaracionFuncion() {
			return getRuleContext(DeclaracionFuncionContext.class,0);
		}
		public DeclaracionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaracion; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitDeclaracion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclaracionContext declaracion() throws RecognitionException {
		DeclaracionContext _localctx = new DeclaracionContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_declaracion);
		try {
			setState(58);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,1,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(56);
				declaracionVariable();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(57);
				declaracionFuncion();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclaracionVariableContext extends ParserRuleContext {
		public TipoContext tipo() {
			return getRuleContext(TipoContext.class,0);
		}
		public TerminalNode ID() { return getToken(MiLenguajeParser.ID, 0); }
		public TerminalNode PYC() { return getToken(MiLenguajeParser.PYC, 0); }
		public TerminalNode IGUAL() { return getToken(MiLenguajeParser.IGUAL, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public TerminalNode CA() { return getToken(MiLenguajeParser.CA, 0); }
		public TerminalNode NUM() { return getToken(MiLenguajeParser.NUM, 0); }
		public TerminalNode CC() { return getToken(MiLenguajeParser.CC, 0); }
		public DeclaracionVariableContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaracionVariable; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitDeclaracionVariable(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclaracionVariableContext declaracionVariable() throws RecognitionException {
		DeclaracionVariableContext _localctx = new DeclaracionVariableContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_declaracionVariable);
		int _la;
		try {
			setState(75);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(60);
				tipo();
				setState(61);
				match(ID);
				setState(64);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==IGUAL) {
					{
					setState(62);
					match(IGUAL);
					setState(63);
					expresion(0);
					}
				}

				setState(66);
				match(PYC);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(68);
				tipo();
				setState(69);
				match(ID);
				setState(70);
				match(CA);
				setState(71);
				match(NUM);
				setState(72);
				match(CC);
				setState(73);
				match(PYC);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TipoContext extends ParserRuleContext {
		public TerminalNode INT() { return getToken(MiLenguajeParser.INT, 0); }
		public TerminalNode DOUBLE() { return getToken(MiLenguajeParser.DOUBLE, 0); }
		public TerminalNode CHAR() { return getToken(MiLenguajeParser.CHAR, 0); }
		public TerminalNode STRING() { return getToken(MiLenguajeParser.STRING, 0); }
		public TerminalNode BOOL() { return getToken(MiLenguajeParser.BOOL, 0); }
		public TerminalNode VOID() { return getToken(MiLenguajeParser.VOID, 0); }
		public TipoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_tipo; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitTipo(this);
			else return visitor.visitChildren(this);
		}
	}

	public final TipoContext tipo() throws RecognitionException {
		TipoContext _localctx = new TipoContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_tipo);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << DOUBLE) | (1L << CHAR) | (1L << STRING) | (1L << BOOL) | (1L << VOID))) != 0)) ) {
			_errHandler.recoverInline(this);
			}
			else {
				if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
				_errHandler.reportMatch(this);
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclaracionFuncionContext extends ParserRuleContext {
		public TipoContext tipo() {
			return getRuleContext(TipoContext.class,0);
		}
		public TerminalNode ID() { return getToken(MiLenguajeParser.ID, 0); }
		public TerminalNode PA() { return getToken(MiLenguajeParser.PA, 0); }
		public TerminalNode PC() { return getToken(MiLenguajeParser.PC, 0); }
		public BloqueContext bloque() {
			return getRuleContext(BloqueContext.class,0);
		}
		public ParametrosContext parametros() {
			return getRuleContext(ParametrosContext.class,0);
		}
		public DeclaracionFuncionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaracionFuncion; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitDeclaracionFuncion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclaracionFuncionContext declaracionFuncion() throws RecognitionException {
		DeclaracionFuncionContext _localctx = new DeclaracionFuncionContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_declaracionFuncion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(79);
			tipo();
			setState(80);
			match(ID);
			setState(81);
			match(PA);
			setState(83);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << DOUBLE) | (1L << CHAR) | (1L << STRING) | (1L << BOOL) | (1L << VOID))) != 0)) {
				{
				setState(82);
				parametros();
				}
			}

			setState(85);
			match(PC);
			setState(86);
			bloque();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParametrosContext extends ParserRuleContext {
		public List<ParametroContext> parametro() {
			return getRuleContexts(ParametroContext.class);
		}
		public ParametroContext parametro(int i) {
			return getRuleContext(ParametroContext.class,i);
		}
		public List<TerminalNode> COMA() { return getTokens(MiLenguajeParser.COMA); }
		public TerminalNode COMA(int i) {
			return getToken(MiLenguajeParser.COMA, i);
		}
		public ParametrosContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parametros; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitParametros(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParametrosContext parametros() throws RecognitionException {
		ParametrosContext _localctx = new ParametrosContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_parametros);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(88);
			parametro();
			setState(93);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(89);
				match(COMA);
				setState(90);
				parametro();
				}
				}
				setState(95);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ParametroContext extends ParserRuleContext {
		public TipoContext tipo() {
			return getRuleContext(TipoContext.class,0);
		}
		public TerminalNode ID() { return getToken(MiLenguajeParser.ID, 0); }
		public TerminalNode CA() { return getToken(MiLenguajeParser.CA, 0); }
		public TerminalNode CC() { return getToken(MiLenguajeParser.CC, 0); }
		public ParametroContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parametro; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitParametro(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParametroContext parametro() throws RecognitionException {
		ParametroContext _localctx = new ParametroContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_parametro);
		try {
			setState(104);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,6,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(96);
				tipo();
				setState(97);
				match(ID);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(99);
				tipo();
				setState(100);
				match(ID);
				setState(101);
				match(CA);
				setState(102);
				match(CC);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class BloqueContext extends ParserRuleContext {
		public TerminalNode LA() { return getToken(MiLenguajeParser.LA, 0); }
		public TerminalNode LC() { return getToken(MiLenguajeParser.LC, 0); }
		public List<SentenciaContext> sentencia() {
			return getRuleContexts(SentenciaContext.class);
		}
		public SentenciaContext sentencia(int i) {
			return getRuleContext(SentenciaContext.class,i);
		}
		public BloqueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bloque; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitBloque(this);
			else return visitor.visitChildren(this);
		}
	}

	public final BloqueContext bloque() throws RecognitionException {
		BloqueContext _localctx = new BloqueContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_bloque);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(106);
			match(LA);
			setState(110);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LA) | (1L << IF) | (1L << WHILE) | (1L << FOR) | (1L << BREAK) | (1L << CONTINUE) | (1L << RETURN) | (1L << INT) | (1L << DOUBLE) | (1L << CHAR) | (1L << STRING) | (1L << BOOL) | (1L << VOID) | (1L << ID))) != 0)) {
				{
				{
				setState(107);
				sentencia();
				}
				}
				setState(112);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(113);
			match(LC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SentenciaContext extends ParserRuleContext {
		public DeclaracionVariableContext declaracionVariable() {
			return getRuleContext(DeclaracionVariableContext.class,0);
		}
		public AsignacionContext asignacion() {
			return getRuleContext(AsignacionContext.class,0);
		}
		public IncrementoContext incremento() {
			return getRuleContext(IncrementoContext.class,0);
		}
		public TerminalNode PYC() { return getToken(MiLenguajeParser.PYC, 0); }
		public LlamadaFuncionContext llamadaFuncion() {
			return getRuleContext(LlamadaFuncionContext.class,0);
		}
		public SentenciaIfContext sentenciaIf() {
			return getRuleContext(SentenciaIfContext.class,0);
		}
		public SentenciaWhileContext sentenciaWhile() {
			return getRuleContext(SentenciaWhileContext.class,0);
		}
		public SentenciaForContext sentenciaFor() {
			return getRuleContext(SentenciaForContext.class,0);
		}
		public SentenciaBreakContext sentenciaBreak() {
			return getRuleContext(SentenciaBreakContext.class,0);
		}
		public SentenciaContinueContext sentenciaContinue() {
			return getRuleContext(SentenciaContinueContext.class,0);
		}
		public RetornoContext retorno() {
			return getRuleContext(RetornoContext.class,0);
		}
		public BloqueContext bloque() {
			return getRuleContext(BloqueContext.class,0);
		}
		public SentenciaContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sentencia; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitSentencia(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SentenciaContext sentencia() throws RecognitionException {
		SentenciaContext _localctx = new SentenciaContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_sentencia);
		try {
			setState(130);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,8,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(115);
				declaracionVariable();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(116);
				asignacion();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(117);
				incremento();
				setState(118);
				match(PYC);
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(120);
				llamadaFuncion();
				setState(121);
				match(PYC);
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(123);
				sentenciaIf();
				}
				break;
			case 6:
				enterOuterAlt(_localctx, 6);
				{
				setState(124);
				sentenciaWhile();
				}
				break;
			case 7:
				enterOuterAlt(_localctx, 7);
				{
				setState(125);
				sentenciaFor();
				}
				break;
			case 8:
				enterOuterAlt(_localctx, 8);
				{
				setState(126);
				sentenciaBreak();
				}
				break;
			case 9:
				enterOuterAlt(_localctx, 9);
				{
				setState(127);
				sentenciaContinue();
				}
				break;
			case 10:
				enterOuterAlt(_localctx, 10);
				{
				setState(128);
				retorno();
				}
				break;
			case 11:
				enterOuterAlt(_localctx, 11);
				{
				setState(129);
				bloque();
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SentenciaIfContext extends ParserRuleContext {
		public TerminalNode IF() { return getToken(MiLenguajeParser.IF, 0); }
		public TerminalNode PA() { return getToken(MiLenguajeParser.PA, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public TerminalNode PC() { return getToken(MiLenguajeParser.PC, 0); }
		public List<BloqueContext> bloque() {
			return getRuleContexts(BloqueContext.class);
		}
		public BloqueContext bloque(int i) {
			return getRuleContext(BloqueContext.class,i);
		}
		public TerminalNode ELSE() { return getToken(MiLenguajeParser.ELSE, 0); }
		public SentenciaIfContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sentenciaIf; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitSentenciaIf(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SentenciaIfContext sentenciaIf() throws RecognitionException {
		SentenciaIfContext _localctx = new SentenciaIfContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_sentenciaIf);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(132);
			match(IF);
			setState(133);
			match(PA);
			setState(134);
			expresion(0);
			setState(135);
			match(PC);
			setState(136);
			bloque();
			setState(139);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ELSE) {
				{
				setState(137);
				match(ELSE);
				setState(138);
				bloque();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SentenciaWhileContext extends ParserRuleContext {
		public TerminalNode WHILE() { return getToken(MiLenguajeParser.WHILE, 0); }
		public TerminalNode PA() { return getToken(MiLenguajeParser.PA, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public TerminalNode PC() { return getToken(MiLenguajeParser.PC, 0); }
		public BloqueContext bloque() {
			return getRuleContext(BloqueContext.class,0);
		}
		public SentenciaWhileContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sentenciaWhile; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitSentenciaWhile(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SentenciaWhileContext sentenciaWhile() throws RecognitionException {
		SentenciaWhileContext _localctx = new SentenciaWhileContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_sentenciaWhile);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			match(WHILE);
			setState(142);
			match(PA);
			setState(143);
			expresion(0);
			setState(144);
			match(PC);
			setState(145);
			bloque();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SentenciaForContext extends ParserRuleContext {
		public TerminalNode FOR() { return getToken(MiLenguajeParser.FOR, 0); }
		public TerminalNode PA() { return getToken(MiLenguajeParser.PA, 0); }
		public List<TerminalNode> PYC() { return getTokens(MiLenguajeParser.PYC); }
		public TerminalNode PYC(int i) {
			return getToken(MiLenguajeParser.PYC, i);
		}
		public TerminalNode PC() { return getToken(MiLenguajeParser.PC, 0); }
		public BloqueContext bloque() {
			return getRuleContext(BloqueContext.class,0);
		}
		public InicializacionForContext inicializacionFor() {
			return getRuleContext(InicializacionForContext.class,0);
		}
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public ActualizacionForContext actualizacionFor() {
			return getRuleContext(ActualizacionForContext.class,0);
		}
		public SentenciaForContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sentenciaFor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitSentenciaFor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SentenciaForContext sentenciaFor() throws RecognitionException {
		SentenciaForContext _localctx = new SentenciaForContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_sentenciaFor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(147);
			match(FOR);
			setState(148);
			match(PA);
			setState(150);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << INT) | (1L << DOUBLE) | (1L << CHAR) | (1L << STRING) | (1L << BOOL) | (1L << VOID) | (1L << ID))) != 0)) {
				{
				setState(149);
				inicializacionFor();
				}
			}

			setState(152);
			match(PYC);
			setState(154);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PA) | (1L << RES) | (1L << NOT) | (1L << TRUE) | (1L << FALSE) | (1L << ID) | (1L << DECIMAL) | (1L << NUM) | (1L << CHARACTER) | (1L << CADENA))) != 0)) {
				{
				setState(153);
				expresion(0);
				}
			}

			setState(156);
			match(PYC);
			setState(158);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==ID) {
				{
				setState(157);
				actualizacionFor();
				}
			}

			setState(160);
			match(PC);
			setState(161);
			bloque();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class InicializacionForContext extends ParserRuleContext {
		public DeclaracionForContext declaracionFor() {
			return getRuleContext(DeclaracionForContext.class,0);
		}
		public AsignacionForContext asignacionFor() {
			return getRuleContext(AsignacionForContext.class,0);
		}
		public InicializacionForContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_inicializacionFor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitInicializacionFor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final InicializacionForContext inicializacionFor() throws RecognitionException {
		InicializacionForContext _localctx = new InicializacionForContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_inicializacionFor);
		try {
			setState(165);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case INT:
			case DOUBLE:
			case CHAR:
			case STRING:
			case BOOL:
			case VOID:
				enterOuterAlt(_localctx, 1);
				{
				setState(163);
				declaracionFor();
				}
				break;
			case ID:
				enterOuterAlt(_localctx, 2);
				{
				setState(164);
				asignacionFor();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DeclaracionForContext extends ParserRuleContext {
		public TipoContext tipo() {
			return getRuleContext(TipoContext.class,0);
		}
		public TerminalNode ID() { return getToken(MiLenguajeParser.ID, 0); }
		public TerminalNode IGUAL() { return getToken(MiLenguajeParser.IGUAL, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public DeclaracionForContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_declaracionFor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitDeclaracionFor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DeclaracionForContext declaracionFor() throws RecognitionException {
		DeclaracionForContext _localctx = new DeclaracionForContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_declaracionFor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(167);
			tipo();
			setState(168);
			match(ID);
			setState(169);
			match(IGUAL);
			setState(170);
			expresion(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AsignacionForContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(MiLenguajeParser.ID, 0); }
		public TerminalNode IGUAL() { return getToken(MiLenguajeParser.IGUAL, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public AsignacionForContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asignacionFor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitAsignacionFor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AsignacionForContext asignacionFor() throws RecognitionException {
		AsignacionForContext _localctx = new AsignacionForContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_asignacionFor);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(172);
			match(ID);
			setState(173);
			match(IGUAL);
			setState(174);
			expresion(0);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ActualizacionForContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(MiLenguajeParser.ID, 0); }
		public TerminalNode INC() { return getToken(MiLenguajeParser.INC, 0); }
		public TerminalNode DEC() { return getToken(MiLenguajeParser.DEC, 0); }
		public TerminalNode IGUAL() { return getToken(MiLenguajeParser.IGUAL, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public ActualizacionForContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_actualizacionFor; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitActualizacionFor(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ActualizacionForContext actualizacionFor() throws RecognitionException {
		ActualizacionForContext _localctx = new ActualizacionForContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_actualizacionFor);
		try {
			setState(183);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,14,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(176);
				match(ID);
				setState(177);
				match(INC);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(178);
				match(ID);
				setState(179);
				match(DEC);
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(180);
				match(ID);
				setState(181);
				match(IGUAL);
				setState(182);
				expresion(0);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SentenciaBreakContext extends ParserRuleContext {
		public TerminalNode BREAK() { return getToken(MiLenguajeParser.BREAK, 0); }
		public TerminalNode PYC() { return getToken(MiLenguajeParser.PYC, 0); }
		public SentenciaBreakContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sentenciaBreak; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitSentenciaBreak(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SentenciaBreakContext sentenciaBreak() throws RecognitionException {
		SentenciaBreakContext _localctx = new SentenciaBreakContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_sentenciaBreak);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(185);
			match(BREAK);
			setState(186);
			match(PYC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SentenciaContinueContext extends ParserRuleContext {
		public TerminalNode CONTINUE() { return getToken(MiLenguajeParser.CONTINUE, 0); }
		public TerminalNode PYC() { return getToken(MiLenguajeParser.PYC, 0); }
		public SentenciaContinueContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_sentenciaContinue; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitSentenciaContinue(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SentenciaContinueContext sentenciaContinue() throws RecognitionException {
		SentenciaContinueContext _localctx = new SentenciaContinueContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_sentenciaContinue);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(188);
			match(CONTINUE);
			setState(189);
			match(PYC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RetornoContext extends ParserRuleContext {
		public TerminalNode RETURN() { return getToken(MiLenguajeParser.RETURN, 0); }
		public TerminalNode PYC() { return getToken(MiLenguajeParser.PYC, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public RetornoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_retorno; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitRetorno(this);
			else return visitor.visitChildren(this);
		}
	}

	public final RetornoContext retorno() throws RecognitionException {
		RetornoContext _localctx = new RetornoContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_retorno);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(191);
			match(RETURN);
			setState(193);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PA) | (1L << RES) | (1L << NOT) | (1L << TRUE) | (1L << FALSE) | (1L << ID) | (1L << DECIMAL) | (1L << NUM) | (1L << CHARACTER) | (1L << CADENA))) != 0)) {
				{
				setState(192);
				expresion(0);
				}
			}

			setState(195);
			match(PYC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AsignacionContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(MiLenguajeParser.ID, 0); }
		public TerminalNode IGUAL() { return getToken(MiLenguajeParser.IGUAL, 0); }
		public List<ExpresionContext> expresion() {
			return getRuleContexts(ExpresionContext.class);
		}
		public ExpresionContext expresion(int i) {
			return getRuleContext(ExpresionContext.class,i);
		}
		public TerminalNode PYC() { return getToken(MiLenguajeParser.PYC, 0); }
		public TerminalNode CA() { return getToken(MiLenguajeParser.CA, 0); }
		public TerminalNode CC() { return getToken(MiLenguajeParser.CC, 0); }
		public AsignacionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_asignacion; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitAsignacion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final AsignacionContext asignacion() throws RecognitionException {
		AsignacionContext _localctx = new AsignacionContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_asignacion);
		try {
			setState(210);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(197);
				match(ID);
				setState(198);
				match(IGUAL);
				setState(199);
				expresion(0);
				setState(200);
				match(PYC);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(202);
				match(ID);
				setState(203);
				match(CA);
				setState(204);
				expresion(0);
				setState(205);
				match(CC);
				setState(206);
				match(IGUAL);
				setState(207);
				expresion(0);
				setState(208);
				match(PYC);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class LlamadaFuncionContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(MiLenguajeParser.ID, 0); }
		public TerminalNode PA() { return getToken(MiLenguajeParser.PA, 0); }
		public TerminalNode PC() { return getToken(MiLenguajeParser.PC, 0); }
		public ArgumentosContext argumentos() {
			return getRuleContext(ArgumentosContext.class,0);
		}
		public LlamadaFuncionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_llamadaFuncion; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitLlamadaFuncion(this);
			else return visitor.visitChildren(this);
		}
	}

	public final LlamadaFuncionContext llamadaFuncion() throws RecognitionException {
		LlamadaFuncionContext _localctx = new LlamadaFuncionContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_llamadaFuncion);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(212);
			match(ID);
			setState(213);
			match(PA);
			setState(215);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PA) | (1L << RES) | (1L << NOT) | (1L << TRUE) | (1L << FALSE) | (1L << ID) | (1L << DECIMAL) | (1L << NUM) | (1L << CHARACTER) | (1L << CADENA))) != 0)) {
				{
				setState(214);
				argumentos();
				}
			}

			setState(217);
			match(PC);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArgumentosContext extends ParserRuleContext {
		public List<ExpresionContext> expresion() {
			return getRuleContexts(ExpresionContext.class);
		}
		public ExpresionContext expresion(int i) {
			return getRuleContext(ExpresionContext.class,i);
		}
		public List<TerminalNode> COMA() { return getTokens(MiLenguajeParser.COMA); }
		public TerminalNode COMA(int i) {
			return getToken(MiLenguajeParser.COMA, i);
		}
		public ArgumentosContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_argumentos; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitArgumentos(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ArgumentosContext argumentos() throws RecognitionException {
		ArgumentosContext _localctx = new ArgumentosContext(_ctx, getState());
		enterRule(_localctx, 42, RULE_argumentos);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219);
			expresion(0);
			setState(224);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==COMA) {
				{
				{
				setState(220);
				match(COMA);
				setState(221);
				expresion(0);
				}
				}
				setState(226);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IncrementoContext extends ParserRuleContext {
		public TerminalNode ID() { return getToken(MiLenguajeParser.ID, 0); }
		public TerminalNode INC() { return getToken(MiLenguajeParser.INC, 0); }
		public TerminalNode DEC() { return getToken(MiLenguajeParser.DEC, 0); }
		public IncrementoContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_incremento; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitIncremento(this);
			else return visitor.visitChildren(this);
		}
	}

	public final IncrementoContext incremento() throws RecognitionException {
		IncrementoContext _localctx = new IncrementoContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_incremento);
		try {
			setState(231);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(227);
				match(ID);
				setState(228);
				match(INC);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(229);
				match(ID);
				setState(230);
				match(DEC);
				}
				break;
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ExpresionContext extends ParserRuleContext {
		public ExpresionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_expresion; }
	 
		public ExpresionContext() { }
		public void copyFrom(ExpresionContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class ExprAgrupadaContext extends ExpresionContext {
		public TerminalNode PA() { return getToken(MiLenguajeParser.PA, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public TerminalNode PC() { return getToken(MiLenguajeParser.PC, 0); }
		public ExprAgrupadaContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprAgrupada(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprEnteroContext extends ExpresionContext {
		public TerminalNode NUM() { return getToken(MiLenguajeParser.NUM, 0); }
		public ExprEnteroContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprEntero(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprIgualdadContext extends ExpresionContext {
		public List<ExpresionContext> expresion() {
			return getRuleContexts(ExpresionContext.class);
		}
		public ExpresionContext expresion(int i) {
			return getRuleContext(ExpresionContext.class,i);
		}
		public TerminalNode EQL() { return getToken(MiLenguajeParser.EQL, 0); }
		public TerminalNode DISTINTO() { return getToken(MiLenguajeParser.DISTINTO, 0); }
		public ExprIgualdadContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprIgualdad(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprRelacionalContext extends ExpresionContext {
		public List<ExpresionContext> expresion() {
			return getRuleContexts(ExpresionContext.class);
		}
		public ExpresionContext expresion(int i) {
			return getRuleContext(ExpresionContext.class,i);
		}
		public TerminalNode MAYOR() { return getToken(MiLenguajeParser.MAYOR, 0); }
		public TerminalNode MENOR() { return getToken(MiLenguajeParser.MENOR, 0); }
		public TerminalNode MAYOR_IGUAL() { return getToken(MiLenguajeParser.MAYOR_IGUAL, 0); }
		public TerminalNode MENOR_IGUAL() { return getToken(MiLenguajeParser.MENOR_IGUAL, 0); }
		public ExprRelacionalContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprRelacional(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprCadenaContext extends ExpresionContext {
		public TerminalNode CADENA() { return getToken(MiLenguajeParser.CADENA, 0); }
		public ExprCadenaContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprCadena(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprIdentificadorContext extends ExpresionContext {
		public TerminalNode ID() { return getToken(MiLenguajeParser.ID, 0); }
		public ExprIdentificadorContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprIdentificador(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprNegativoContext extends ExpresionContext {
		public TerminalNode RES() { return getToken(MiLenguajeParser.RES, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public ExprNegativoContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprNegativo(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprFalseContext extends ExpresionContext {
		public TerminalNode FALSE() { return getToken(MiLenguajeParser.FALSE, 0); }
		public ExprFalseContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprFalse(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprCaracterContext extends ExpresionContext {
		public TerminalNode CHARACTER() { return getToken(MiLenguajeParser.CHARACTER, 0); }
		public ExprCaracterContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprCaracter(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprTrueContext extends ExpresionContext {
		public TerminalNode TRUE() { return getToken(MiLenguajeParser.TRUE, 0); }
		public ExprTrueContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprTrue(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprLlamadaContext extends ExpresionContext {
		public LlamadaFuncionContext llamadaFuncion() {
			return getRuleContext(LlamadaFuncionContext.class,0);
		}
		public ExprLlamadaContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprLlamada(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprNotContext extends ExpresionContext {
		public TerminalNode NOT() { return getToken(MiLenguajeParser.NOT, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public ExprNotContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprNot(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprArrayContext extends ExpresionContext {
		public TerminalNode ID() { return getToken(MiLenguajeParser.ID, 0); }
		public TerminalNode CA() { return getToken(MiLenguajeParser.CA, 0); }
		public ExpresionContext expresion() {
			return getRuleContext(ExpresionContext.class,0);
		}
		public TerminalNode CC() { return getToken(MiLenguajeParser.CC, 0); }
		public ExprArrayContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprArray(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprAditivaContext extends ExpresionContext {
		public List<ExpresionContext> expresion() {
			return getRuleContexts(ExpresionContext.class);
		}
		public ExpresionContext expresion(int i) {
			return getRuleContext(ExpresionContext.class,i);
		}
		public TerminalNode SUM() { return getToken(MiLenguajeParser.SUM, 0); }
		public TerminalNode RES() { return getToken(MiLenguajeParser.RES, 0); }
		public ExprAditivaContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprAditiva(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprAndContext extends ExpresionContext {
		public List<ExpresionContext> expresion() {
			return getRuleContexts(ExpresionContext.class);
		}
		public ExpresionContext expresion(int i) {
			return getRuleContext(ExpresionContext.class,i);
		}
		public TerminalNode AND() { return getToken(MiLenguajeParser.AND, 0); }
		public ExprAndContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprAnd(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprDecimalContext extends ExpresionContext {
		public TerminalNode DECIMAL() { return getToken(MiLenguajeParser.DECIMAL, 0); }
		public ExprDecimalContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprDecimal(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprOrContext extends ExpresionContext {
		public List<ExpresionContext> expresion() {
			return getRuleContexts(ExpresionContext.class);
		}
		public ExpresionContext expresion(int i) {
			return getRuleContext(ExpresionContext.class,i);
		}
		public TerminalNode OR() { return getToken(MiLenguajeParser.OR, 0); }
		public ExprOrContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprOr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class ExprMultiplicativaContext extends ExpresionContext {
		public List<ExpresionContext> expresion() {
			return getRuleContexts(ExpresionContext.class);
		}
		public ExpresionContext expresion(int i) {
			return getRuleContext(ExpresionContext.class,i);
		}
		public TerminalNode MUL() { return getToken(MiLenguajeParser.MUL, 0); }
		public TerminalNode DIV() { return getToken(MiLenguajeParser.DIV, 0); }
		public TerminalNode MOD() { return getToken(MiLenguajeParser.MOD, 0); }
		public ExprMultiplicativaContext(ExpresionContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof MiLenguajeVisitor ) return ((MiLenguajeVisitor<? extends T>)visitor).visitExprMultiplicativa(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ExpresionContext expresion() throws RecognitionException {
		return expresion(0);
	}

	private ExpresionContext expresion(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		ExpresionContext _localctx = new ExpresionContext(_ctx, _parentState);
		ExpresionContext _prevctx = _localctx;
		int _startState = 46;
		enterRecursionRule(_localctx, 46, RULE_expresion, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(255);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				_localctx = new ExprNotContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(234);
				match(NOT);
				setState(235);
				expresion(12);
				}
				break;
			case 2:
				{
				_localctx = new ExprNegativoContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(236);
				match(RES);
				setState(237);
				expresion(11);
				}
				break;
			case 3:
				{
				_localctx = new ExprAgrupadaContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(238);
				match(PA);
				setState(239);
				expresion(0);
				setState(240);
				match(PC);
				}
				break;
			case 4:
				{
				_localctx = new ExprLlamadaContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(242);
				llamadaFuncion();
				}
				break;
			case 5:
				{
				_localctx = new ExprArrayContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(243);
				match(ID);
				setState(244);
				match(CA);
				setState(245);
				expresion(0);
				setState(246);
				match(CC);
				}
				break;
			case 6:
				{
				_localctx = new ExprIdentificadorContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(248);
				match(ID);
				}
				break;
			case 7:
				{
				_localctx = new ExprEnteroContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(249);
				match(NUM);
				}
				break;
			case 8:
				{
				_localctx = new ExprDecimalContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(250);
				match(DECIMAL);
				}
				break;
			case 9:
				{
				_localctx = new ExprCaracterContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(251);
				match(CHARACTER);
				}
				break;
			case 10:
				{
				_localctx = new ExprCadenaContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(252);
				match(CADENA);
				}
				break;
			case 11:
				{
				_localctx = new ExprTrueContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(253);
				match(TRUE);
				}
				break;
			case 12:
				{
				_localctx = new ExprFalseContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(254);
				match(FALSE);
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(277);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(275);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
					case 1:
						{
						_localctx = new ExprOrContext(new ExpresionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expresion);
						setState(257);
						if (!(precpred(_ctx, 18))) throw new FailedPredicateException(this, "precpred(_ctx, 18)");
						setState(258);
						match(OR);
						setState(259);
						expresion(19);
						}
						break;
					case 2:
						{
						_localctx = new ExprAndContext(new ExpresionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expresion);
						setState(260);
						if (!(precpred(_ctx, 17))) throw new FailedPredicateException(this, "precpred(_ctx, 17)");
						setState(261);
						match(AND);
						setState(262);
						expresion(18);
						}
						break;
					case 3:
						{
						_localctx = new ExprIgualdadContext(new ExpresionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expresion);
						setState(263);
						if (!(precpred(_ctx, 16))) throw new FailedPredicateException(this, "precpred(_ctx, 16)");
						setState(264);
						_la = _input.LA(1);
						if ( !(_la==EQL || _la==DISTINTO) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(265);
						expresion(17);
						}
						break;
					case 4:
						{
						_localctx = new ExprRelacionalContext(new ExpresionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expresion);
						setState(266);
						if (!(precpred(_ctx, 15))) throw new FailedPredicateException(this, "precpred(_ctx, 15)");
						setState(267);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MAYOR_IGUAL) | (1L << MENOR_IGUAL) | (1L << MAYOR) | (1L << MENOR))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(268);
						expresion(16);
						}
						break;
					case 5:
						{
						_localctx = new ExprAditivaContext(new ExpresionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expresion);
						setState(269);
						if (!(precpred(_ctx, 14))) throw new FailedPredicateException(this, "precpred(_ctx, 14)");
						setState(270);
						_la = _input.LA(1);
						if ( !(_la==SUM || _la==RES) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(271);
						expresion(15);
						}
						break;
					case 6:
						{
						_localctx = new ExprMultiplicativaContext(new ExpresionContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_expresion);
						setState(272);
						if (!(precpred(_ctx, 13))) throw new FailedPredicateException(this, "precpred(_ctx, 13)");
						setState(273);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << MUL) | (1L << DIV) | (1L << MOD))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(274);
						expresion(14);
						}
						break;
					}
					} 
				}
				setState(279);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,22,_ctx);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			unrollRecursionContexts(_parentctx);
		}
		return _localctx;
	}

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 23:
			return expresion_sempred((ExpresionContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean expresion_sempred(ExpresionContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 18);
		case 1:
			return precpred(_ctx, 17);
		case 2:
			return precpred(_ctx, 16);
		case 3:
			return precpred(_ctx, 15);
		case 4:
			return precpred(_ctx, 14);
		case 5:
			return precpred(_ctx, 13);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3\63\u011b\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\3\2\7\2\64\n\2\f\2\16\2\67\13\2\3\2\3\2\3\3\3\3\5\3=\n\3\3\4\3\4\3\4"+
		"\3\4\5\4C\n\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\3\4\5\4N\n\4\3\5\3\5\3\6"+
		"\3\6\3\6\3\6\5\6V\n\6\3\6\3\6\3\6\3\7\3\7\3\7\7\7^\n\7\f\7\16\7a\13\7"+
		"\3\b\3\b\3\b\3\b\3\b\3\b\3\b\3\b\5\bk\n\b\3\t\3\t\7\to\n\t\f\t\16\tr\13"+
		"\t\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\n"+
		"\5\n\u0085\n\n\3\13\3\13\3\13\3\13\3\13\3\13\3\13\5\13\u008e\n\13\3\f"+
		"\3\f\3\f\3\f\3\f\3\f\3\r\3\r\3\r\5\r\u0099\n\r\3\r\3\r\5\r\u009d\n\r\3"+
		"\r\3\r\5\r\u00a1\n\r\3\r\3\r\3\r\3\16\3\16\5\16\u00a8\n\16\3\17\3\17\3"+
		"\17\3\17\3\17\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\3\21\3\21\3\21\5"+
		"\21\u00ba\n\21\3\22\3\22\3\22\3\23\3\23\3\23\3\24\3\24\5\24\u00c4\n\24"+
		"\3\24\3\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\5\25\u00d5\n\25\3\26\3\26\3\26\5\26\u00da\n\26\3\26\3\26\3\27\3"+
		"\27\3\27\7\27\u00e1\n\27\f\27\16\27\u00e4\13\27\3\30\3\30\3\30\3\30\5"+
		"\30\u00ea\n\30\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\5\31\u0102\n\31"+
		"\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\31\7\31\u0116\n\31\f\31\16\31\u0119\13\31\3\31\2\3\60"+
		"\32\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\2\7\3\2#(\3\2\f"+
		"\r\3\2\16\21\3\2\24\25\3\2\26\30\2\u0131\2\65\3\2\2\2\4<\3\2\2\2\6M\3"+
		"\2\2\2\bO\3\2\2\2\nQ\3\2\2\2\fZ\3\2\2\2\16j\3\2\2\2\20l\3\2\2\2\22\u0084"+
		"\3\2\2\2\24\u0086\3\2\2\2\26\u008f\3\2\2\2\30\u0095\3\2\2\2\32\u00a7\3"+
		"\2\2\2\34\u00a9\3\2\2\2\36\u00ae\3\2\2\2 \u00b9\3\2\2\2\"\u00bb\3\2\2"+
		"\2$\u00be\3\2\2\2&\u00c1\3\2\2\2(\u00d4\3\2\2\2*\u00d6\3\2\2\2,\u00dd"+
		"\3\2\2\2.\u00e9\3\2\2\2\60\u0101\3\2\2\2\62\64\5\4\3\2\63\62\3\2\2\2\64"+
		"\67\3\2\2\2\65\63\3\2\2\2\65\66\3\2\2\2\668\3\2\2\2\67\65\3\2\2\289\7"+
		"\2\2\39\3\3\2\2\2:=\5\6\4\2;=\5\n\6\2<:\3\2\2\2<;\3\2\2\2=\5\3\2\2\2>"+
		"?\5\b\5\2?B\7+\2\2@A\7\13\2\2AC\5\60\31\2B@\3\2\2\2BC\3\2\2\2CD\3\2\2"+
		"\2DE\7\t\2\2EN\3\2\2\2FG\5\b\5\2GH\7+\2\2HI\7\5\2\2IJ\7-\2\2JK\7\6\2\2"+
		"KL\7\t\2\2LN\3\2\2\2M>\3\2\2\2MF\3\2\2\2N\7\3\2\2\2OP\t\2\2\2P\t\3\2\2"+
		"\2QR\5\b\5\2RS\7+\2\2SU\7\3\2\2TV\5\f\7\2UT\3\2\2\2UV\3\2\2\2VW\3\2\2"+
		"\2WX\7\4\2\2XY\5\20\t\2Y\13\3\2\2\2Z_\5\16\b\2[\\\7\n\2\2\\^\5\16\b\2"+
		"][\3\2\2\2^a\3\2\2\2_]\3\2\2\2_`\3\2\2\2`\r\3\2\2\2a_\3\2\2\2bc\5\b\5"+
		"\2cd\7+\2\2dk\3\2\2\2ef\5\b\5\2fg\7+\2\2gh\7\5\2\2hi\7\6\2\2ik\3\2\2\2"+
		"jb\3\2\2\2je\3\2\2\2k\17\3\2\2\2lp\7\7\2\2mo\5\22\n\2nm\3\2\2\2or\3\2"+
		"\2\2pn\3\2\2\2pq\3\2\2\2qs\3\2\2\2rp\3\2\2\2st\7\b\2\2t\21\3\2\2\2u\u0085"+
		"\5\6\4\2v\u0085\5(\25\2wx\5.\30\2xy\7\t\2\2y\u0085\3\2\2\2z{\5*\26\2{"+
		"|\7\t\2\2|\u0085\3\2\2\2}\u0085\5\24\13\2~\u0085\5\26\f\2\177\u0085\5"+
		"\30\r\2\u0080\u0085\5\"\22\2\u0081\u0085\5$\23\2\u0082\u0085\5&\24\2\u0083"+
		"\u0085\5\20\t\2\u0084u\3\2\2\2\u0084v\3\2\2\2\u0084w\3\2\2\2\u0084z\3"+
		"\2\2\2\u0084}\3\2\2\2\u0084~\3\2\2\2\u0084\177\3\2\2\2\u0084\u0080\3\2"+
		"\2\2\u0084\u0081\3\2\2\2\u0084\u0082\3\2\2\2\u0084\u0083\3\2\2\2\u0085"+
		"\23\3\2\2\2\u0086\u0087\7\34\2\2\u0087\u0088\7\3\2\2\u0088\u0089\5\60"+
		"\31\2\u0089\u008a\7\4\2\2\u008a\u008d\5\20\t\2\u008b\u008c\7\35\2\2\u008c"+
		"\u008e\5\20\t\2\u008d\u008b\3\2\2\2\u008d\u008e\3\2\2\2\u008e\25\3\2\2"+
		"\2\u008f\u0090\7\36\2\2\u0090\u0091\7\3\2\2\u0091\u0092\5\60\31\2\u0092"+
		"\u0093\7\4\2\2\u0093\u0094\5\20\t\2\u0094\27\3\2\2\2\u0095\u0096\7\37"+
		"\2\2\u0096\u0098\7\3\2\2\u0097\u0099\5\32\16\2\u0098\u0097\3\2\2\2\u0098"+
		"\u0099\3\2\2\2\u0099\u009a\3\2\2\2\u009a\u009c\7\t\2\2\u009b\u009d\5\60"+
		"\31\2\u009c\u009b\3\2\2\2\u009c\u009d\3\2\2\2\u009d\u009e\3\2\2\2\u009e"+
		"\u00a0\7\t\2\2\u009f\u00a1\5 \21\2\u00a0\u009f\3\2\2\2\u00a0\u00a1\3\2"+
		"\2\2\u00a1\u00a2\3\2\2\2\u00a2\u00a3\7\4\2\2\u00a3\u00a4\5\20\t\2\u00a4"+
		"\31\3\2\2\2\u00a5\u00a8\5\34\17\2\u00a6\u00a8\5\36\20\2\u00a7\u00a5\3"+
		"\2\2\2\u00a7\u00a6\3\2\2\2\u00a8\33\3\2\2\2\u00a9\u00aa\5\b\5\2\u00aa"+
		"\u00ab\7+\2\2\u00ab\u00ac\7\13\2\2\u00ac\u00ad\5\60\31\2\u00ad\35\3\2"+
		"\2\2\u00ae\u00af\7+\2\2\u00af\u00b0\7\13\2\2\u00b0\u00b1\5\60\31\2\u00b1"+
		"\37\3\2\2\2\u00b2\u00b3\7+\2\2\u00b3\u00ba\7\22\2\2\u00b4\u00b5\7+\2\2"+
		"\u00b5\u00ba\7\23\2\2\u00b6\u00b7\7+\2\2\u00b7\u00b8\7\13\2\2\u00b8\u00ba"+
		"\5\60\31\2\u00b9\u00b2\3\2\2\2\u00b9\u00b4\3\2\2\2\u00b9\u00b6\3\2\2\2"+
		"\u00ba!\3\2\2\2\u00bb\u00bc\7 \2\2\u00bc\u00bd\7\t\2\2\u00bd#\3\2\2\2"+
		"\u00be\u00bf\7!\2\2\u00bf\u00c0\7\t\2\2\u00c0%\3\2\2\2\u00c1\u00c3\7\""+
		"\2\2\u00c2\u00c4\5\60\31\2\u00c3\u00c2\3\2\2\2\u00c3\u00c4\3\2\2\2\u00c4"+
		"\u00c5\3\2\2\2\u00c5\u00c6\7\t\2\2\u00c6\'\3\2\2\2\u00c7\u00c8\7+\2\2"+
		"\u00c8\u00c9\7\13\2\2\u00c9\u00ca\5\60\31\2\u00ca\u00cb\7\t\2\2\u00cb"+
		"\u00d5\3\2\2\2\u00cc\u00cd\7+\2\2\u00cd\u00ce\7\5\2\2\u00ce\u00cf\5\60"+
		"\31\2\u00cf\u00d0\7\6\2\2\u00d0\u00d1\7\13\2\2\u00d1\u00d2\5\60\31\2\u00d2"+
		"\u00d3\7\t\2\2\u00d3\u00d5\3\2\2\2\u00d4\u00c7\3\2\2\2\u00d4\u00cc\3\2"+
		"\2\2\u00d5)\3\2\2\2\u00d6\u00d7\7+\2\2\u00d7\u00d9\7\3\2\2\u00d8\u00da"+
		"\5,\27\2\u00d9\u00d8\3\2\2\2\u00d9\u00da\3\2\2\2\u00da\u00db\3\2\2\2\u00db"+
		"\u00dc\7\4\2\2\u00dc+\3\2\2\2\u00dd\u00e2\5\60\31\2\u00de\u00df\7\n\2"+
		"\2\u00df\u00e1\5\60\31\2\u00e0\u00de\3\2\2\2\u00e1\u00e4\3\2\2\2\u00e2"+
		"\u00e0\3\2\2\2\u00e2\u00e3\3\2\2\2\u00e3-\3\2\2\2\u00e4\u00e2\3\2\2\2"+
		"\u00e5\u00e6\7+\2\2\u00e6\u00ea\7\22\2\2\u00e7\u00e8\7+\2\2\u00e8\u00ea"+
		"\7\23\2\2\u00e9\u00e5\3\2\2\2\u00e9\u00e7\3\2\2\2\u00ea/\3\2\2\2\u00eb"+
		"\u00ec\b\31\1\2\u00ec\u00ed\7\33\2\2\u00ed\u0102\5\60\31\16\u00ee\u00ef"+
		"\7\25\2\2\u00ef\u0102\5\60\31\r\u00f0\u00f1\7\3\2\2\u00f1\u00f2\5\60\31"+
		"\2\u00f2\u00f3\7\4\2\2\u00f3\u0102\3\2\2\2\u00f4\u0102\5*\26\2\u00f5\u00f6"+
		"\7+\2\2\u00f6\u00f7\7\5\2\2\u00f7\u00f8\5\60\31\2\u00f8\u00f9\7\6\2\2"+
		"\u00f9\u0102\3\2\2\2\u00fa\u0102\7+\2\2\u00fb\u0102\7-\2\2\u00fc\u0102"+
		"\7,\2\2\u00fd\u0102\7.\2\2\u00fe\u0102\7/\2\2\u00ff\u0102\7)\2\2\u0100"+
		"\u0102\7*\2\2\u0101\u00eb\3\2\2\2\u0101\u00ee\3\2\2\2\u0101\u00f0\3\2"+
		"\2\2\u0101\u00f4\3\2\2\2\u0101\u00f5\3\2\2\2\u0101\u00fa\3\2\2\2\u0101"+
		"\u00fb\3\2\2\2\u0101\u00fc\3\2\2\2\u0101\u00fd\3\2\2\2\u0101\u00fe\3\2"+
		"\2\2\u0101\u00ff\3\2\2\2\u0101\u0100\3\2\2\2\u0102\u0117\3\2\2\2\u0103"+
		"\u0104\f\24\2\2\u0104\u0105\7\31\2\2\u0105\u0116\5\60\31\25\u0106\u0107"+
		"\f\23\2\2\u0107\u0108\7\32\2\2\u0108\u0116\5\60\31\24\u0109\u010a\f\22"+
		"\2\2\u010a\u010b\t\3\2\2\u010b\u0116\5\60\31\23\u010c\u010d\f\21\2\2\u010d"+
		"\u010e\t\4\2\2\u010e\u0116\5\60\31\22\u010f\u0110\f\20\2\2\u0110\u0111"+
		"\t\5\2\2\u0111\u0116\5\60\31\21\u0112\u0113\f\17\2\2\u0113\u0114\t\6\2"+
		"\2\u0114\u0116\5\60\31\20\u0115\u0103\3\2\2\2\u0115\u0106\3\2\2\2\u0115"+
		"\u0109\3\2\2\2\u0115\u010c\3\2\2\2\u0115\u010f\3\2\2\2\u0115\u0112\3\2"+
		"\2\2\u0116\u0119\3\2\2\2\u0117\u0115\3\2\2\2\u0117\u0118\3\2\2\2\u0118"+
		"\61\3\2\2\2\u0119\u0117\3\2\2\2\31\65<BMU_jp\u0084\u008d\u0098\u009c\u00a0"+
		"\u00a7\u00b9\u00c3\u00d4\u00d9\u00e2\u00e9\u0101\u0115\u0117";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}