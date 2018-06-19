// Generated from CEPL.g4 by ANTLR 4.7.1
package cepl.parser;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class CEPLParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.7.1", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, K_ALL=12, K_AND=13, K_ANY=14, K_AS=15, K_BY=16, K_CONSUME=17, 
		K_DECLARE=18, K_DISTINCT=19, K_EVENT=20, K_EVENTS=21, K_FILTER=22, K_FROM=23, 
		K_HOURS=24, K_IN=25, K_LAST=26, K_LIKE=27, K_MAX=28, K_MINUTES=29, K_NEXT=30, 
		K_NONE=31, K_NOT=32, K_OR=33, K_PARTITION=34, K_SECONDS=35, K_SELECT=36, 
		K_STREAM=37, K_STRICT=38, K_UNLESS=39, K_WHERE=40, K_WITHIN=41, PERCENT=42, 
		PLUS=43, MINUS=44, STAR=45, SLASH=46, LE=47, LEQ=48, GE=49, GEQ=50, EQ=51, 
		NEQ=52, IDENTIFIER=53, NUMERIC_LITERAL=54, INTEGER_LITERAL=55, NUMERICAL_EXPONENT=56, 
		STRING_LITERAL=57, SINGLE_LINE_COMMENT=58, MULTILINE_COMMENT=59, SPACES=60, 
		UNEXPECTED_CHAR=61;
	public static final int
		RULE_parse = 0, RULE_error = 1, RULE_cel_stmt = 2, RULE_cel_declaration = 3, 
		RULE_event_declaration = 4, RULE_attribute_dec_list = 5, RULE_attribute_declaration = 6, 
		RULE_datatype = 7, RULE_stream_declaration = 8, RULE_event_list = 9, RULE_cel_query = 10, 
		RULE_selection_strategy = 11, RULE_stream_list = 12, RULE_result_values = 13, 
		RULE_cel_pattern = 14, RULE_partition_list = 15, RULE_attribute_list = 16, 
		RULE_consumption_policy = 17, RULE_filter = 18, RULE_bool_expr = 19, RULE_string_literal = 20, 
		RULE_math_expr = 21, RULE_value_seq = 22, RULE_number_seq = 23, RULE_string_seq = 24, 
		RULE_time_window = 25, RULE_event_span = 26, RULE_time_span = 27, RULE_hours = 28, 
		RULE_minutes = 29, RULE_seconds = 30, RULE_named_event = 31, RULE_s_event_name = 32, 
		RULE_event_name = 33, RULE_stream_name = 34, RULE_attribute_name = 35, 
		RULE_number = 36, RULE_string = 37, RULE_any_name = 38, RULE_keyword = 39;
	public static final String[] ruleNames = {
		"parse", "error", "cel_stmt", "cel_declaration", "event_declaration", 
		"attribute_dec_list", "attribute_declaration", "datatype", "stream_declaration", 
		"event_list", "cel_query", "selection_strategy", "stream_list", "result_values", 
		"cel_pattern", "partition_list", "attribute_list", "consumption_policy", 
		"filter", "bool_expr", "string_literal", "math_expr", "value_seq", "number_seq", 
		"string_seq", "time_window", "event_span", "time_span", "hours", "minutes", 
		"seconds", "named_event", "s_event_name", "event_name", "stream_name", 
		"attribute_name", "number", "string", "any_name", "keyword"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'('", "')'", "','", "'int'", "'string'", "'double'", "'long'", 
		"';'", "'['", "']'", "'..'", null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, "'%'", 
		"'+'", "'-'", "'*'", "'/'", "'<'", "'<='", "'>'", "'>='"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		"K_ALL", "K_AND", "K_ANY", "K_AS", "K_BY", "K_CONSUME", "K_DECLARE", "K_DISTINCT", 
		"K_EVENT", "K_EVENTS", "K_FILTER", "K_FROM", "K_HOURS", "K_IN", "K_LAST", 
		"K_LIKE", "K_MAX", "K_MINUTES", "K_NEXT", "K_NONE", "K_NOT", "K_OR", "K_PARTITION", 
		"K_SECONDS", "K_SELECT", "K_STREAM", "K_STRICT", "K_UNLESS", "K_WHERE", 
		"K_WITHIN", "PERCENT", "PLUS", "MINUS", "STAR", "SLASH", "LE", "LEQ", 
		"GE", "GEQ", "EQ", "NEQ", "IDENTIFIER", "NUMERIC_LITERAL", "INTEGER_LITERAL", 
		"NUMERICAL_EXPONENT", "STRING_LITERAL", "SINGLE_LINE_COMMENT", "MULTILINE_COMMENT", 
		"SPACES", "UNEXPECTED_CHAR"
	};
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
	public String getGrammarFileName() { return "CEPL.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public CEPLParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class ParseContext extends ParserRuleContext {
		public TerminalNode EOF() { return getToken(CEPLParser.EOF, 0); }
		public List<Cel_stmtContext> cel_stmt() {
			return getRuleContexts(Cel_stmtContext.class);
		}
		public Cel_stmtContext cel_stmt(int i) {
			return getRuleContext(Cel_stmtContext.class,i);
		}
		public List<ErrorContext> error() {
			return getRuleContexts(ErrorContext.class);
		}
		public ErrorContext error(int i) {
			return getRuleContext(ErrorContext.class,i);
		}
		public ParseContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parse; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitParse(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ParseContext parse() throws RecognitionException {
		ParseContext _localctx = new ParseContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_parse);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(84);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << K_DECLARE) | (1L << K_SELECT) | (1L << UNEXPECTED_CHAR))) != 0)) {
				{
				setState(82);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case K_DECLARE:
				case K_SELECT:
					{
					setState(80);
					cel_stmt();
					}
					break;
				case UNEXPECTED_CHAR:
					{
					setState(81);
					error();
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(86);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(87);
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

	public static class ErrorContext extends ParserRuleContext {
		public Token UNEXPECTED_CHAR;
		public TerminalNode UNEXPECTED_CHAR() { return getToken(CEPLParser.UNEXPECTED_CHAR, 0); }
		public ErrorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_error; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitError(this);
			else return visitor.visitChildren(this);
		}
	}

	public final ErrorContext error() throws RecognitionException {
		ErrorContext _localctx = new ErrorContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_error);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(89);
			((ErrorContext)_localctx).UNEXPECTED_CHAR = match(UNEXPECTED_CHAR);

			     throw new RuntimeException("UNEXPECTED_CHAR=" + (((ErrorContext)_localctx).UNEXPECTED_CHAR!=null?((ErrorContext)_localctx).UNEXPECTED_CHAR.getText():null));
			   
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

	public static class Cel_stmtContext extends ParserRuleContext {
		public Cel_queryContext cel_query() {
			return getRuleContext(Cel_queryContext.class,0);
		}
		public Cel_declarationContext cel_declaration() {
			return getRuleContext(Cel_declarationContext.class,0);
		}
		public Cel_stmtContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cel_stmt; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitCel_stmt(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cel_stmtContext cel_stmt() throws RecognitionException {
		Cel_stmtContext _localctx = new Cel_stmtContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_cel_stmt);
		try {
			setState(94);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_SELECT:
				enterOuterAlt(_localctx, 1);
				{
				setState(92);
				cel_query();
				}
				break;
			case K_DECLARE:
				enterOuterAlt(_localctx, 2);
				{
				setState(93);
				cel_declaration();
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

	public static class Cel_declarationContext extends ParserRuleContext {
		public Event_declarationContext event_declaration() {
			return getRuleContext(Event_declarationContext.class,0);
		}
		public Stream_declarationContext stream_declaration() {
			return getRuleContext(Stream_declarationContext.class,0);
		}
		public Cel_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cel_declaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitCel_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cel_declarationContext cel_declaration() throws RecognitionException {
		Cel_declarationContext _localctx = new Cel_declarationContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_cel_declaration);
		try {
			setState(98);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,3,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(96);
				event_declaration();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(97);
				stream_declaration();
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

	public static class Event_declarationContext extends ParserRuleContext {
		public TerminalNode K_DECLARE() { return getToken(CEPLParser.K_DECLARE, 0); }
		public TerminalNode K_EVENT() { return getToken(CEPLParser.K_EVENT, 0); }
		public Event_nameContext event_name() {
			return getRuleContext(Event_nameContext.class,0);
		}
		public Attribute_dec_listContext attribute_dec_list() {
			return getRuleContext(Attribute_dec_listContext.class,0);
		}
		public Event_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_event_declaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitEvent_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Event_declarationContext event_declaration() throws RecognitionException {
		Event_declarationContext _localctx = new Event_declarationContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_event_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			match(K_DECLARE);
			setState(101);
			match(K_EVENT);
			setState(102);
			event_name();
			setState(103);
			match(T__0);
			setState(104);
			attribute_dec_list();
			setState(105);
			match(T__1);
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

	public static class Attribute_dec_listContext extends ParserRuleContext {
		public List<Attribute_declarationContext> attribute_declaration() {
			return getRuleContexts(Attribute_declarationContext.class);
		}
		public Attribute_declarationContext attribute_declaration(int i) {
			return getRuleContext(Attribute_declarationContext.class,i);
		}
		public Attribute_dec_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute_dec_list; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitAttribute_dec_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Attribute_dec_listContext attribute_dec_list() throws RecognitionException {
		Attribute_dec_listContext _localctx = new Attribute_dec_listContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_attribute_dec_list);
		int _la;
		try {
			setState(116);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__1:
				enterOuterAlt(_localctx, 1);
				{
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(108);
				attribute_declaration();
				setState(113);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(109);
					match(T__2);
					setState(110);
					attribute_declaration();
					}
					}
					setState(115);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
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

	public static class Attribute_declarationContext extends ParserRuleContext {
		public Attribute_nameContext attribute_name() {
			return getRuleContext(Attribute_nameContext.class,0);
		}
		public DatatypeContext datatype() {
			return getRuleContext(DatatypeContext.class,0);
		}
		public Attribute_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute_declaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitAttribute_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Attribute_declarationContext attribute_declaration() throws RecognitionException {
		Attribute_declarationContext _localctx = new Attribute_declarationContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_attribute_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(118);
			attribute_name();
			setState(119);
			datatype();
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

	public static class DatatypeContext extends ParserRuleContext {
		public DatatypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_datatype; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitDatatype(this);
			else return visitor.visitChildren(this);
		}
	}

	public final DatatypeContext datatype() throws RecognitionException {
		DatatypeContext _localctx = new DatatypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_datatype);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(121);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__3) | (1L << T__4) | (1L << T__5) | (1L << T__6))) != 0)) ) {
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

	public static class Stream_declarationContext extends ParserRuleContext {
		public TerminalNode K_DECLARE() { return getToken(CEPLParser.K_DECLARE, 0); }
		public TerminalNode K_STREAM() { return getToken(CEPLParser.K_STREAM, 0); }
		public Stream_nameContext stream_name() {
			return getRuleContext(Stream_nameContext.class,0);
		}
		public Event_listContext event_list() {
			return getRuleContext(Event_listContext.class,0);
		}
		public Stream_declarationContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stream_declaration; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitStream_declaration(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Stream_declarationContext stream_declaration() throws RecognitionException {
		Stream_declarationContext _localctx = new Stream_declarationContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_stream_declaration);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			match(K_DECLARE);
			setState(124);
			match(K_STREAM);
			setState(125);
			stream_name();
			setState(126);
			match(T__0);
			setState(127);
			event_list();
			setState(128);
			match(T__1);
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

	public static class Event_listContext extends ParserRuleContext {
		public List<Event_nameContext> event_name() {
			return getRuleContexts(Event_nameContext.class);
		}
		public Event_nameContext event_name(int i) {
			return getRuleContext(Event_nameContext.class,i);
		}
		public Event_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_event_list; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitEvent_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Event_listContext event_list() throws RecognitionException {
		Event_listContext _localctx = new Event_listContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_event_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			event_name();
			setState(135);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(131);
				match(T__2);
				setState(132);
				event_name();
				}
				}
				setState(137);
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

	public static class Cel_queryContext extends ParserRuleContext {
		public TerminalNode K_SELECT() { return getToken(CEPLParser.K_SELECT, 0); }
		public Result_valuesContext result_values() {
			return getRuleContext(Result_valuesContext.class,0);
		}
		public TerminalNode K_WHERE() { return getToken(CEPLParser.K_WHERE, 0); }
		public Cel_patternContext cel_pattern() {
			return getRuleContext(Cel_patternContext.class,0);
		}
		public Selection_strategyContext selection_strategy() {
			return getRuleContext(Selection_strategyContext.class,0);
		}
		public TerminalNode K_FROM() { return getToken(CEPLParser.K_FROM, 0); }
		public Stream_listContext stream_list() {
			return getRuleContext(Stream_listContext.class,0);
		}
		public TerminalNode K_PARTITION() { return getToken(CEPLParser.K_PARTITION, 0); }
		public List<TerminalNode> K_BY() { return getTokens(CEPLParser.K_BY); }
		public TerminalNode K_BY(int i) {
			return getToken(CEPLParser.K_BY, i);
		}
		public Partition_listContext partition_list() {
			return getRuleContext(Partition_listContext.class,0);
		}
		public TerminalNode K_WITHIN() { return getToken(CEPLParser.K_WITHIN, 0); }
		public Time_windowContext time_window() {
			return getRuleContext(Time_windowContext.class,0);
		}
		public TerminalNode K_CONSUME() { return getToken(CEPLParser.K_CONSUME, 0); }
		public Consumption_policyContext consumption_policy() {
			return getRuleContext(Consumption_policyContext.class,0);
		}
		public Cel_queryContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cel_query; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitCel_query(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cel_queryContext cel_query() throws RecognitionException {
		Cel_queryContext _localctx = new Cel_queryContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_cel_query);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(138);
			match(K_SELECT);
			setState(140);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << K_ALL) | (1L << K_LAST) | (1L << K_MAX) | (1L << K_NEXT) | (1L << K_STRICT))) != 0)) {
				{
				setState(139);
				selection_strategy();
				}
			}

			setState(142);
			result_values();
			setState(145);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_FROM) {
				{
				setState(143);
				match(K_FROM);
				setState(144);
				stream_list();
				}
			}

			setState(147);
			match(K_WHERE);
			setState(148);
			cel_pattern(0);
			setState(152);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_PARTITION) {
				{
				setState(149);
				match(K_PARTITION);
				setState(150);
				match(K_BY);
				setState(151);
				partition_list();
				}
			}

			setState(156);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_WITHIN) {
				{
				setState(154);
				match(K_WITHIN);
				setState(155);
				time_window();
				}
			}

			setState(161);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_CONSUME) {
				{
				setState(158);
				match(K_CONSUME);
				setState(159);
				match(K_BY);
				setState(160);
				consumption_policy();
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

	public static class Selection_strategyContext extends ParserRuleContext {
		public Selection_strategyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_selection_strategy; }
	 
		public Selection_strategyContext() { }
		public void copyFrom(Selection_strategyContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Ss_lastContext extends Selection_strategyContext {
		public TerminalNode K_LAST() { return getToken(CEPLParser.K_LAST, 0); }
		public Ss_lastContext(Selection_strategyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitSs_last(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Ss_strictContext extends Selection_strategyContext {
		public TerminalNode K_STRICT() { return getToken(CEPLParser.K_STRICT, 0); }
		public Ss_strictContext(Selection_strategyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitSs_strict(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Ss_nextContext extends Selection_strategyContext {
		public TerminalNode K_NEXT() { return getToken(CEPLParser.K_NEXT, 0); }
		public Ss_nextContext(Selection_strategyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitSs_next(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Ss_maxContext extends Selection_strategyContext {
		public TerminalNode K_MAX() { return getToken(CEPLParser.K_MAX, 0); }
		public Ss_maxContext(Selection_strategyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitSs_max(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Ss_allContext extends Selection_strategyContext {
		public TerminalNode K_ALL() { return getToken(CEPLParser.K_ALL, 0); }
		public Ss_allContext(Selection_strategyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitSs_all(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Selection_strategyContext selection_strategy() throws RecognitionException {
		Selection_strategyContext _localctx = new Selection_strategyContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_selection_strategy);
		try {
			setState(168);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_ALL:
				_localctx = new Ss_allContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(163);
				match(K_ALL);
				}
				break;
			case K_LAST:
				_localctx = new Ss_lastContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(164);
				match(K_LAST);
				}
				break;
			case K_MAX:
				_localctx = new Ss_maxContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(165);
				match(K_MAX);
				}
				break;
			case K_NEXT:
				_localctx = new Ss_nextContext(_localctx);
				enterOuterAlt(_localctx, 4);
				{
				setState(166);
				match(K_NEXT);
				}
				break;
			case K_STRICT:
				_localctx = new Ss_strictContext(_localctx);
				enterOuterAlt(_localctx, 5);
				{
				setState(167);
				match(K_STRICT);
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

	public static class Stream_listContext extends ParserRuleContext {
		public List<Stream_nameContext> stream_name() {
			return getRuleContexts(Stream_nameContext.class);
		}
		public Stream_nameContext stream_name(int i) {
			return getRuleContext(Stream_nameContext.class,i);
		}
		public Stream_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stream_list; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitStream_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Stream_listContext stream_list() throws RecognitionException {
		Stream_listContext _localctx = new Stream_listContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_stream_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(170);
			stream_name();
			setState(175);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(171);
				match(T__2);
				setState(172);
				stream_name();
				}
				}
				setState(177);
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

	public static class Result_valuesContext extends ParserRuleContext {
		public TerminalNode STAR() { return getToken(CEPLParser.STAR, 0); }
		public List<Event_nameContext> event_name() {
			return getRuleContexts(Event_nameContext.class);
		}
		public Event_nameContext event_name(int i) {
			return getRuleContext(Event_nameContext.class,i);
		}
		public Result_valuesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_result_values; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitResult_values(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Result_valuesContext result_values() throws RecognitionException {
		Result_valuesContext _localctx = new Result_valuesContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_result_values);
		int _la;
		try {
			setState(187);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STAR:
				enterOuterAlt(_localctx, 1);
				{
				setState(178);
				match(STAR);
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(179);
				event_name();
				setState(184);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(180);
					match(T__2);
					setState(181);
					event_name();
					}
					}
					setState(186);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
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

	public static class Cel_patternContext extends ParserRuleContext {
		public Cel_patternContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_cel_pattern; }
	 
		public Cel_patternContext() { }
		public void copyFrom(Cel_patternContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Par_cel_patternContext extends Cel_patternContext {
		public Cel_patternContext cel_pattern() {
			return getRuleContext(Cel_patternContext.class,0);
		}
		public Par_cel_patternContext(Cel_patternContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitPar_cel_pattern(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Binary_cel_patternContext extends Cel_patternContext {
		public List<Cel_patternContext> cel_pattern() {
			return getRuleContexts(Cel_patternContext.class);
		}
		public Cel_patternContext cel_pattern(int i) {
			return getRuleContext(Cel_patternContext.class,i);
		}
		public TerminalNode K_OR() { return getToken(CEPLParser.K_OR, 0); }
		public Binary_cel_patternContext(Cel_patternContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitBinary_cel_pattern(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Assign_cel_patternContext extends Cel_patternContext {
		public Cel_patternContext cel_pattern() {
			return getRuleContext(Cel_patternContext.class,0);
		}
		public TerminalNode K_AS() { return getToken(CEPLParser.K_AS, 0); }
		public Event_nameContext event_name() {
			return getRuleContext(Event_nameContext.class,0);
		}
		public Assign_cel_patternContext(Cel_patternContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitAssign_cel_pattern(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Kleene_cel_patternContext extends Cel_patternContext {
		public Cel_patternContext cel_pattern() {
			return getRuleContext(Cel_patternContext.class,0);
		}
		public Kleene_cel_patternContext(Cel_patternContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitKleene_cel_pattern(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Event_cel_patternContext extends Cel_patternContext {
		public S_event_nameContext s_event_name() {
			return getRuleContext(S_event_nameContext.class,0);
		}
		public Event_cel_patternContext(Cel_patternContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitEvent_cel_pattern(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Filter_cel_patternContext extends Cel_patternContext {
		public Cel_patternContext cel_pattern() {
			return getRuleContext(Cel_patternContext.class,0);
		}
		public TerminalNode K_FILTER() { return getToken(CEPLParser.K_FILTER, 0); }
		public FilterContext filter() {
			return getRuleContext(FilterContext.class,0);
		}
		public Filter_cel_patternContext(Cel_patternContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitFilter_cel_pattern(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Cel_patternContext cel_pattern() throws RecognitionException {
		return cel_pattern(0);
	}

	private Cel_patternContext cel_pattern(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Cel_patternContext _localctx = new Cel_patternContext(_ctx, _parentState);
		Cel_patternContext _prevctx = _localctx;
		int _startState = 28;
		enterRecursionRule(_localctx, 28, RULE_cel_pattern, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				{
				_localctx = new Par_cel_patternContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(190);
				match(T__0);
				setState(191);
				cel_pattern(0);
				setState(192);
				match(T__1);
				}
				break;
			case IDENTIFIER:
				{
				_localctx = new Event_cel_patternContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(194);
				s_event_name();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(210);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(208);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
					case 1:
						{
						_localctx = new Binary_cel_patternContext(new Cel_patternContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_cel_pattern);
						setState(197);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(198);
						_la = _input.LA(1);
						if ( !(_la==T__7 || _la==K_OR) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(199);
						cel_pattern(3);
						}
						break;
					case 2:
						{
						_localctx = new Assign_cel_patternContext(new Cel_patternContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_cel_pattern);
						setState(200);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(201);
						match(K_AS);
						setState(202);
						event_name();
						}
						break;
					case 3:
						{
						_localctx = new Kleene_cel_patternContext(new Cel_patternContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_cel_pattern);
						setState(203);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(204);
						match(PLUS);
						}
						break;
					case 4:
						{
						_localctx = new Filter_cel_patternContext(new Cel_patternContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_cel_pattern);
						setState(205);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(206);
						match(K_FILTER);
						setState(207);
						filter(0);
						}
						break;
					}
					} 
				}
				setState(212);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
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

	public static class Partition_listContext extends ParserRuleContext {
		public List<Attribute_listContext> attribute_list() {
			return getRuleContexts(Attribute_listContext.class);
		}
		public Attribute_listContext attribute_list(int i) {
			return getRuleContext(Attribute_listContext.class,i);
		}
		public Partition_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_partition_list; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitPartition_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Partition_listContext partition_list() throws RecognitionException {
		Partition_listContext _localctx = new Partition_listContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_partition_list);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(213);
			match(T__8);
			setState(214);
			attribute_list();
			setState(215);
			match(T__9);
			setState(220);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(216);
				match(T__2);
				setState(217);
				attribute_list();
				}
				}
				setState(222);
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

	public static class Attribute_listContext extends ParserRuleContext {
		public List<Attribute_nameContext> attribute_name() {
			return getRuleContexts(Attribute_nameContext.class);
		}
		public Attribute_nameContext attribute_name(int i) {
			return getRuleContext(Attribute_nameContext.class,i);
		}
		public Attribute_listContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute_list; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitAttribute_list(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Attribute_listContext attribute_list() throws RecognitionException {
		Attribute_listContext _localctx = new Attribute_listContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_attribute_list);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(223);
			attribute_name();
			setState(228);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					{
					{
					setState(224);
					match(T__2);
					setState(225);
					attribute_name();
					}
					} 
				}
				setState(230);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
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

	public static class Consumption_policyContext extends ParserRuleContext {
		public Consumption_policyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_consumption_policy; }
	 
		public Consumption_policyContext() { }
		public void copyFrom(Consumption_policyContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Cp_partitionContext extends Consumption_policyContext {
		public TerminalNode K_PARTITION() { return getToken(CEPLParser.K_PARTITION, 0); }
		public Cp_partitionContext(Consumption_policyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitCp_partition(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Cp_anyContext extends Consumption_policyContext {
		public TerminalNode K_ANY() { return getToken(CEPLParser.K_ANY, 0); }
		public Cp_anyContext(Consumption_policyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitCp_any(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Cp_noneContext extends Consumption_policyContext {
		public TerminalNode K_NONE() { return getToken(CEPLParser.K_NONE, 0); }
		public Cp_noneContext(Consumption_policyContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitCp_none(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Consumption_policyContext consumption_policy() throws RecognitionException {
		Consumption_policyContext _localctx = new Consumption_policyContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_consumption_policy);
		try {
			setState(234);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case K_ANY:
				_localctx = new Cp_anyContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(231);
				match(K_ANY);
				}
				break;
			case K_PARTITION:
				_localctx = new Cp_partitionContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(232);
				match(K_PARTITION);
				}
				break;
			case K_NONE:
				_localctx = new Cp_noneContext(_localctx);
				enterOuterAlt(_localctx, 3);
				{
				setState(233);
				match(K_NONE);
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

	public static class FilterContext extends ParserRuleContext {
		public FilterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_filter; }
	 
		public FilterContext() { }
		public void copyFrom(FilterContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Par_filterContext extends FilterContext {
		public FilterContext filter() {
			return getRuleContext(FilterContext.class,0);
		}
		public Par_filterContext(FilterContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitPar_filter(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class And_filterContext extends FilterContext {
		public List<FilterContext> filter() {
			return getRuleContexts(FilterContext.class);
		}
		public FilterContext filter(int i) {
			return getRuleContext(FilterContext.class,i);
		}
		public TerminalNode K_AND() { return getToken(CEPLParser.K_AND, 0); }
		public And_filterContext(FilterContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitAnd_filter(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Event_filterContext extends FilterContext {
		public Event_nameContext event_name() {
			return getRuleContext(Event_nameContext.class,0);
		}
		public Bool_exprContext bool_expr() {
			return getRuleContext(Bool_exprContext.class,0);
		}
		public Event_filterContext(FilterContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitEvent_filter(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Or_filterContext extends FilterContext {
		public List<FilterContext> filter() {
			return getRuleContexts(FilterContext.class);
		}
		public FilterContext filter(int i) {
			return getRuleContext(FilterContext.class,i);
		}
		public TerminalNode K_OR() { return getToken(CEPLParser.K_OR, 0); }
		public Or_filterContext(FilterContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitOr_filter(this);
			else return visitor.visitChildren(this);
		}
	}

	public final FilterContext filter() throws RecognitionException {
		return filter(0);
	}

	private FilterContext filter(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		FilterContext _localctx = new FilterContext(_ctx, _parentState);
		FilterContext _prevctx = _localctx;
		int _startState = 36;
		enterRecursionRule(_localctx, 36, RULE_filter, _p);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				{
				_localctx = new Par_filterContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(237);
				match(T__0);
				setState(238);
				filter(0);
				setState(239);
				match(T__1);
				}
				break;
			case IDENTIFIER:
				{
				_localctx = new Event_filterContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(241);
				event_name();
				setState(242);
				match(T__8);
				setState(243);
				bool_expr(0);
				setState(244);
				match(T__9);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(256);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(254);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,23,_ctx) ) {
					case 1:
						{
						_localctx = new And_filterContext(new FilterContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_filter);
						setState(248);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(249);
						match(K_AND);
						setState(250);
						filter(3);
						}
						break;
					case 2:
						{
						_localctx = new Or_filterContext(new FilterContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_filter);
						setState(251);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(252);
						match(K_OR);
						setState(253);
						filter(2);
						}
						break;
					}
					} 
				}
				setState(258);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,24,_ctx);
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

	public static class Bool_exprContext extends ParserRuleContext {
		public Bool_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_bool_expr; }
	 
		public Bool_exprContext() { }
		public void copyFrom(Bool_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Not_exprContext extends Bool_exprContext {
		public TerminalNode K_NOT() { return getToken(CEPLParser.K_NOT, 0); }
		public Bool_exprContext bool_expr() {
			return getRuleContext(Bool_exprContext.class,0);
		}
		public Not_exprContext(Bool_exprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitNot_expr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Equality_string_exprContext extends Bool_exprContext {
		public List<String_literalContext> string_literal() {
			return getRuleContexts(String_literalContext.class);
		}
		public String_literalContext string_literal(int i) {
			return getRuleContext(String_literalContext.class,i);
		}
		public TerminalNode EQ() { return getToken(CEPLParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(CEPLParser.NEQ, 0); }
		public Equality_string_exprContext(Bool_exprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitEquality_string_expr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class And_exprContext extends Bool_exprContext {
		public List<Bool_exprContext> bool_expr() {
			return getRuleContexts(Bool_exprContext.class);
		}
		public Bool_exprContext bool_expr(int i) {
			return getRuleContext(Bool_exprContext.class,i);
		}
		public TerminalNode K_AND() { return getToken(CEPLParser.K_AND, 0); }
		public And_exprContext(Bool_exprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitAnd_expr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Par_bool_exprContext extends Bool_exprContext {
		public Bool_exprContext bool_expr() {
			return getRuleContext(Bool_exprContext.class,0);
		}
		public Par_bool_exprContext(Bool_exprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitPar_bool_expr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Containment_exprContext extends Bool_exprContext {
		public Attribute_nameContext attribute_name() {
			return getRuleContext(Attribute_nameContext.class,0);
		}
		public Value_seqContext value_seq() {
			return getRuleContext(Value_seqContext.class,0);
		}
		public TerminalNode K_IN() { return getToken(CEPLParser.K_IN, 0); }
		public TerminalNode K_NOT() { return getToken(CEPLParser.K_NOT, 0); }
		public Containment_exprContext(Bool_exprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitContainment_expr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Inequality_exprContext extends Bool_exprContext {
		public List<Math_exprContext> math_expr() {
			return getRuleContexts(Math_exprContext.class);
		}
		public Math_exprContext math_expr(int i) {
			return getRuleContext(Math_exprContext.class,i);
		}
		public TerminalNode LE() { return getToken(CEPLParser.LE, 0); }
		public TerminalNode LEQ() { return getToken(CEPLParser.LEQ, 0); }
		public TerminalNode GE() { return getToken(CEPLParser.GE, 0); }
		public TerminalNode GEQ() { return getToken(CEPLParser.GEQ, 0); }
		public Inequality_exprContext(Bool_exprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitInequality_expr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Or_exprContext extends Bool_exprContext {
		public List<Bool_exprContext> bool_expr() {
			return getRuleContexts(Bool_exprContext.class);
		}
		public Bool_exprContext bool_expr(int i) {
			return getRuleContext(Bool_exprContext.class,i);
		}
		public TerminalNode K_OR() { return getToken(CEPLParser.K_OR, 0); }
		public Or_exprContext(Bool_exprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitOr_expr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Equality_math_exprContext extends Bool_exprContext {
		public List<Math_exprContext> math_expr() {
			return getRuleContexts(Math_exprContext.class);
		}
		public Math_exprContext math_expr(int i) {
			return getRuleContext(Math_exprContext.class,i);
		}
		public TerminalNode EQ() { return getToken(CEPLParser.EQ, 0); }
		public TerminalNode NEQ() { return getToken(CEPLParser.NEQ, 0); }
		public Equality_math_exprContext(Bool_exprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitEquality_math_expr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Regex_exprContext extends Bool_exprContext {
		public Attribute_nameContext attribute_name() {
			return getRuleContext(Attribute_nameContext.class,0);
		}
		public TerminalNode K_LIKE() { return getToken(CEPLParser.K_LIKE, 0); }
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public Regex_exprContext(Bool_exprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitRegex_expr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Bool_exprContext bool_expr() throws RecognitionException {
		return bool_expr(0);
	}

	private Bool_exprContext bool_expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Bool_exprContext _localctx = new Bool_exprContext(_ctx, _parentState);
		Bool_exprContext _prevctx = _localctx;
		int _startState = 38;
		enterRecursionRule(_localctx, 38, RULE_bool_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(290);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,26,_ctx) ) {
			case 1:
				{
				_localctx = new Par_bool_exprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(260);
				match(T__0);
				setState(261);
				bool_expr(0);
				setState(262);
				match(T__1);
				}
				break;
			case 2:
				{
				_localctx = new Not_exprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(264);
				match(K_NOT);
				setState(265);
				bool_expr(8);
				}
				break;
			case 3:
				{
				_localctx = new Inequality_exprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(266);
				math_expr(0);
				setState(267);
				_la = _input.LA(1);
				if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << LE) | (1L << LEQ) | (1L << GE) | (1L << GEQ))) != 0)) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(268);
				math_expr(0);
				}
				break;
			case 4:
				{
				_localctx = new Equality_math_exprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(270);
				math_expr(0);
				setState(271);
				_la = _input.LA(1);
				if ( !(_la==EQ || _la==NEQ) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(272);
				math_expr(0);
				}
				break;
			case 5:
				{
				_localctx = new Equality_string_exprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(274);
				string_literal();
				setState(275);
				_la = _input.LA(1);
				if ( !(_la==EQ || _la==NEQ) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(276);
				string_literal();
				}
				break;
			case 6:
				{
				_localctx = new Regex_exprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(278);
				attribute_name();
				setState(279);
				match(K_LIKE);
				setState(280);
				string();
				}
				break;
			case 7:
				{
				_localctx = new Containment_exprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(282);
				attribute_name();
				setState(286);
				_errHandler.sync(this);
				switch (_input.LA(1)) {
				case K_IN:
					{
					setState(283);
					match(K_IN);
					}
					break;
				case K_NOT:
					{
					setState(284);
					match(K_NOT);
					setState(285);
					match(K_IN);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(288);
				value_seq();
				}
				break;
			}
			_ctx.stop = _input.LT(-1);
			setState(300);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(298);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,27,_ctx) ) {
					case 1:
						{
						_localctx = new And_exprContext(new Bool_exprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_bool_expr);
						setState(292);
						if (!(precpred(_ctx, 4))) throw new FailedPredicateException(this, "precpred(_ctx, 4)");
						setState(293);
						match(K_AND);
						setState(294);
						bool_expr(5);
						}
						break;
					case 2:
						{
						_localctx = new Or_exprContext(new Bool_exprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_bool_expr);
						setState(295);
						if (!(precpred(_ctx, 3))) throw new FailedPredicateException(this, "precpred(_ctx, 3)");
						setState(296);
						match(K_OR);
						setState(297);
						bool_expr(4);
						}
						break;
					}
					} 
				}
				setState(302);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,28,_ctx);
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

	public static class String_literalContext extends ParserRuleContext {
		public StringContext string() {
			return getRuleContext(StringContext.class,0);
		}
		public Attribute_nameContext attribute_name() {
			return getRuleContext(Attribute_nameContext.class,0);
		}
		public String_literalContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string_literal; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitString_literal(this);
			else return visitor.visitChildren(this);
		}
	}

	public final String_literalContext string_literal() throws RecognitionException {
		String_literalContext _localctx = new String_literalContext(_ctx, getState());
		enterRule(_localctx, 40, RULE_string_literal);
		try {
			setState(305);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case STRING_LITERAL:
				enterOuterAlt(_localctx, 1);
				{
				setState(303);
				string();
				}
				break;
			case IDENTIFIER:
				enterOuterAlt(_localctx, 2);
				{
				setState(304);
				attribute_name();
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

	public static class Math_exprContext extends ParserRuleContext {
		public Math_exprContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_math_expr; }
	 
		public Math_exprContext() { }
		public void copyFrom(Math_exprContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Mul_math_exprContext extends Math_exprContext {
		public List<Math_exprContext> math_expr() {
			return getRuleContexts(Math_exprContext.class);
		}
		public Math_exprContext math_expr(int i) {
			return getRuleContext(Math_exprContext.class,i);
		}
		public TerminalNode STAR() { return getToken(CEPLParser.STAR, 0); }
		public TerminalNode SLASH() { return getToken(CEPLParser.SLASH, 0); }
		public TerminalNode PERCENT() { return getToken(CEPLParser.PERCENT, 0); }
		public Mul_math_exprContext(Math_exprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitMul_math_expr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Sum_math_exprContext extends Math_exprContext {
		public List<Math_exprContext> math_expr() {
			return getRuleContexts(Math_exprContext.class);
		}
		public Math_exprContext math_expr(int i) {
			return getRuleContext(Math_exprContext.class,i);
		}
		public TerminalNode PLUS() { return getToken(CEPLParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(CEPLParser.MINUS, 0); }
		public Sum_math_exprContext(Math_exprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitSum_math_expr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Number_math_exprContext extends Math_exprContext {
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public Number_math_exprContext(Math_exprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitNumber_math_expr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Unary_math_exprContext extends Math_exprContext {
		public Math_exprContext math_expr() {
			return getRuleContext(Math_exprContext.class,0);
		}
		public TerminalNode PLUS() { return getToken(CEPLParser.PLUS, 0); }
		public TerminalNode MINUS() { return getToken(CEPLParser.MINUS, 0); }
		public Unary_math_exprContext(Math_exprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitUnary_math_expr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Attribute_math_exprContext extends Math_exprContext {
		public Attribute_nameContext attribute_name() {
			return getRuleContext(Attribute_nameContext.class,0);
		}
		public Attribute_math_exprContext(Math_exprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitAttribute_math_expr(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Par_math_exprContext extends Math_exprContext {
		public Math_exprContext math_expr() {
			return getRuleContext(Math_exprContext.class,0);
		}
		public Par_math_exprContext(Math_exprContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitPar_math_expr(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Math_exprContext math_expr() throws RecognitionException {
		return math_expr(0);
	}

	private Math_exprContext math_expr(int _p) throws RecognitionException {
		ParserRuleContext _parentctx = _ctx;
		int _parentState = getState();
		Math_exprContext _localctx = new Math_exprContext(_ctx, _parentState);
		Math_exprContext _prevctx = _localctx;
		int _startState = 42;
		enterRecursionRule(_localctx, 42, RULE_math_expr, _p);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(316);
			_errHandler.sync(this);
			switch (_input.LA(1)) {
			case T__0:
				{
				_localctx = new Par_math_exprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;

				setState(308);
				match(T__0);
				setState(309);
				math_expr(0);
				setState(310);
				match(T__1);
				}
				break;
			case NUMERIC_LITERAL:
				{
				_localctx = new Number_math_exprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(312);
				number();
				}
				break;
			case IDENTIFIER:
				{
				_localctx = new Attribute_math_exprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(313);
				attribute_name();
				}
				break;
			case PLUS:
			case MINUS:
				{
				_localctx = new Unary_math_exprContext(_localctx);
				_ctx = _localctx;
				_prevctx = _localctx;
				setState(314);
				_la = _input.LA(1);
				if ( !(_la==PLUS || _la==MINUS) ) {
				_errHandler.recoverInline(this);
				}
				else {
					if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
					_errHandler.reportMatch(this);
					consume();
				}
				setState(315);
				math_expr(3);
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			_ctx.stop = _input.LT(-1);
			setState(326);
			_errHandler.sync(this);
			_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
			while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
				if ( _alt==1 ) {
					if ( _parseListeners!=null ) triggerExitRuleEvent();
					_prevctx = _localctx;
					{
					setState(324);
					_errHandler.sync(this);
					switch ( getInterpreter().adaptivePredict(_input,31,_ctx) ) {
					case 1:
						{
						_localctx = new Mul_math_exprContext(new Math_exprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_math_expr);
						setState(318);
						if (!(precpred(_ctx, 2))) throw new FailedPredicateException(this, "precpred(_ctx, 2)");
						setState(319);
						_la = _input.LA(1);
						if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << PERCENT) | (1L << STAR) | (1L << SLASH))) != 0)) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(320);
						math_expr(3);
						}
						break;
					case 2:
						{
						_localctx = new Sum_math_exprContext(new Math_exprContext(_parentctx, _parentState));
						pushNewRecursionContext(_localctx, _startState, RULE_math_expr);
						setState(321);
						if (!(precpred(_ctx, 1))) throw new FailedPredicateException(this, "precpred(_ctx, 1)");
						setState(322);
						_la = _input.LA(1);
						if ( !(_la==PLUS || _la==MINUS) ) {
						_errHandler.recoverInline(this);
						}
						else {
							if ( _input.LA(1)==Token.EOF ) matchedEOF = true;
							_errHandler.reportMatch(this);
							consume();
						}
						setState(323);
						math_expr(2);
						}
						break;
					}
					} 
				}
				setState(328);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,32,_ctx);
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

	public static class Value_seqContext extends ParserRuleContext {
		public Number_seqContext number_seq() {
			return getRuleContext(Number_seqContext.class,0);
		}
		public String_seqContext string_seq() {
			return getRuleContext(String_seqContext.class,0);
		}
		public Value_seqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_value_seq; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitValue_seq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Value_seqContext value_seq() throws RecognitionException {
		Value_seqContext _localctx = new Value_seqContext(_ctx, getState());
		enterRule(_localctx, 44, RULE_value_seq);
		try {
			setState(331);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,33,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(329);
				number_seq();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(330);
				string_seq();
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

	public static class Number_seqContext extends ParserRuleContext {
		public Number_seqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number_seq; }
	 
		public Number_seqContext() { }
		public void copyFrom(Number_seqContext ctx) {
			super.copyFrom(ctx);
		}
	}
	public static class Number_listContext extends Number_seqContext {
		public List<NumberContext> number() {
			return getRuleContexts(NumberContext.class);
		}
		public NumberContext number(int i) {
			return getRuleContext(NumberContext.class,i);
		}
		public Number_listContext(Number_seqContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitNumber_list(this);
			else return visitor.visitChildren(this);
		}
	}
	public static class Number_rangeContext extends Number_seqContext {
		public List<NumberContext> number() {
			return getRuleContexts(NumberContext.class);
		}
		public NumberContext number(int i) {
			return getRuleContext(NumberContext.class,i);
		}
		public Number_rangeContext(Number_seqContext ctx) { copyFrom(ctx); }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitNumber_range(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Number_seqContext number_seq() throws RecognitionException {
		Number_seqContext _localctx = new Number_seqContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_number_seq);
		int _la;
		try {
			setState(350);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,35,_ctx) ) {
			case 1:
				_localctx = new Number_listContext(_localctx);
				enterOuterAlt(_localctx, 1);
				{
				setState(333);
				match(T__8);
				setState(334);
				number();
				setState(339);
				_errHandler.sync(this);
				_la = _input.LA(1);
				while (_la==T__2) {
					{
					{
					setState(335);
					match(T__2);
					setState(336);
					number();
					}
					}
					setState(341);
					_errHandler.sync(this);
					_la = _input.LA(1);
				}
				setState(342);
				match(T__9);
				}
				break;
			case 2:
				_localctx = new Number_rangeContext(_localctx);
				enterOuterAlt(_localctx, 2);
				{
				setState(344);
				match(T__8);
				setState(345);
				number();
				setState(346);
				match(T__10);
				setState(347);
				number();
				setState(348);
				match(T__9);
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

	public static class String_seqContext extends ParserRuleContext {
		public List<StringContext> string() {
			return getRuleContexts(StringContext.class);
		}
		public StringContext string(int i) {
			return getRuleContext(StringContext.class,i);
		}
		public String_seqContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string_seq; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitString_seq(this);
			else return visitor.visitChildren(this);
		}
	}

	public final String_seqContext string_seq() throws RecognitionException {
		String_seqContext _localctx = new String_seqContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_string_seq);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(352);
			match(T__8);
			setState(353);
			string();
			setState(358);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__2) {
				{
				{
				setState(354);
				match(T__2);
				setState(355);
				string();
				}
				}
				setState(360);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(361);
			match(T__9);
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

	public static class Time_windowContext extends ParserRuleContext {
		public Event_spanContext event_span() {
			return getRuleContext(Event_spanContext.class,0);
		}
		public Time_spanContext time_span() {
			return getRuleContext(Time_spanContext.class,0);
		}
		public Time_windowContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_time_window; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitTime_window(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Time_windowContext time_window() throws RecognitionException {
		Time_windowContext _localctx = new Time_windowContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_time_window);
		try {
			setState(365);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,37,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(363);
				event_span();
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(364);
				time_span();
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

	public static class Event_spanContext extends ParserRuleContext {
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public TerminalNode K_EVENTS() { return getToken(CEPLParser.K_EVENTS, 0); }
		public Event_spanContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_event_span; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitEvent_span(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Event_spanContext event_span() throws RecognitionException {
		Event_spanContext _localctx = new Event_spanContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_event_span);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(367);
			number();
			setState(368);
			match(K_EVENTS);
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

	public static class Time_spanContext extends ParserRuleContext {
		public HoursContext hours() {
			return getRuleContext(HoursContext.class,0);
		}
		public MinutesContext minutes() {
			return getRuleContext(MinutesContext.class,0);
		}
		public SecondsContext seconds() {
			return getRuleContext(SecondsContext.class,0);
		}
		public Time_spanContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_time_span; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitTime_span(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Time_spanContext time_span() throws RecognitionException {
		Time_spanContext _localctx = new Time_spanContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_time_span);
		int _la;
		try {
			setState(382);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,41,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(370);
				hours();
				setState(372);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,38,_ctx) ) {
				case 1:
					{
					setState(371);
					minutes();
					}
					break;
				}
				setState(375);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NUMERIC_LITERAL) {
					{
					setState(374);
					seconds();
					}
				}

				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(377);
				minutes();
				setState(379);
				_errHandler.sync(this);
				_la = _input.LA(1);
				if (_la==NUMERIC_LITERAL) {
					{
					setState(378);
					seconds();
					}
				}

				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(381);
				seconds();
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

	public static class HoursContext extends ParserRuleContext {
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public TerminalNode K_HOURS() { return getToken(CEPLParser.K_HOURS, 0); }
		public HoursContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_hours; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitHours(this);
			else return visitor.visitChildren(this);
		}
	}

	public final HoursContext hours() throws RecognitionException {
		HoursContext _localctx = new HoursContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_hours);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(384);
			number();
			setState(385);
			match(K_HOURS);
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

	public static class MinutesContext extends ParserRuleContext {
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public TerminalNode K_MINUTES() { return getToken(CEPLParser.K_MINUTES, 0); }
		public MinutesContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_minutes; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitMinutes(this);
			else return visitor.visitChildren(this);
		}
	}

	public final MinutesContext minutes() throws RecognitionException {
		MinutesContext _localctx = new MinutesContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_minutes);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(387);
			number();
			setState(388);
			match(K_MINUTES);
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

	public static class SecondsContext extends ParserRuleContext {
		public NumberContext number() {
			return getRuleContext(NumberContext.class,0);
		}
		public TerminalNode K_SECONDS() { return getToken(CEPLParser.K_SECONDS, 0); }
		public SecondsContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_seconds; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitSeconds(this);
			else return visitor.visitChildren(this);
		}
	}

	public final SecondsContext seconds() throws RecognitionException {
		SecondsContext _localctx = new SecondsContext(_ctx, getState());
		enterRule(_localctx, 60, RULE_seconds);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(390);
			number();
			setState(391);
			match(K_SECONDS);
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

	public static class Named_eventContext extends ParserRuleContext {
		public S_event_nameContext s_event_name() {
			return getRuleContext(S_event_nameContext.class,0);
		}
		public TerminalNode K_AS() { return getToken(CEPLParser.K_AS, 0); }
		public Event_nameContext event_name() {
			return getRuleContext(Event_nameContext.class,0);
		}
		public Named_eventContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_named_event; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitNamed_event(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Named_eventContext named_event() throws RecognitionException {
		Named_eventContext _localctx = new Named_eventContext(_ctx, getState());
		enterRule(_localctx, 62, RULE_named_event);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(393);
			s_event_name();
			setState(396);
			_errHandler.sync(this);
			_la = _input.LA(1);
			if (_la==K_AS) {
				{
				setState(394);
				match(K_AS);
				setState(395);
				event_name();
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

	public static class S_event_nameContext extends ParserRuleContext {
		public Event_nameContext event_name() {
			return getRuleContext(Event_nameContext.class,0);
		}
		public Stream_nameContext stream_name() {
			return getRuleContext(Stream_nameContext.class,0);
		}
		public S_event_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_s_event_name; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitS_event_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final S_event_nameContext s_event_name() throws RecognitionException {
		S_event_nameContext _localctx = new S_event_nameContext(_ctx, getState());
		enterRule(_localctx, 64, RULE_s_event_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(401);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,43,_ctx) ) {
			case 1:
				{
				setState(398);
				stream_name();
				setState(399);
				match(GE);
				}
				break;
			}
			setState(403);
			event_name();
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

	public static class Event_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Event_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_event_name; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitEvent_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Event_nameContext event_name() throws RecognitionException {
		Event_nameContext _localctx = new Event_nameContext(_ctx, getState());
		enterRule(_localctx, 66, RULE_event_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(405);
			any_name();
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

	public static class Stream_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Stream_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_stream_name; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitStream_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Stream_nameContext stream_name() throws RecognitionException {
		Stream_nameContext _localctx = new Stream_nameContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_stream_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(407);
			any_name();
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

	public static class Attribute_nameContext extends ParserRuleContext {
		public Any_nameContext any_name() {
			return getRuleContext(Any_nameContext.class,0);
		}
		public Attribute_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_attribute_name; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitAttribute_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Attribute_nameContext attribute_name() throws RecognitionException {
		Attribute_nameContext _localctx = new Attribute_nameContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_attribute_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(409);
			any_name();
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

	public static class NumberContext extends ParserRuleContext {
		public TerminalNode NUMERIC_LITERAL() { return getToken(CEPLParser.NUMERIC_LITERAL, 0); }
		public NumberContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_number; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitNumber(this);
			else return visitor.visitChildren(this);
		}
	}

	public final NumberContext number() throws RecognitionException {
		NumberContext _localctx = new NumberContext(_ctx, getState());
		enterRule(_localctx, 72, RULE_number);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(411);
			match(NUMERIC_LITERAL);
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

	public static class StringContext extends ParserRuleContext {
		public TerminalNode STRING_LITERAL() { return getToken(CEPLParser.STRING_LITERAL, 0); }
		public StringContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_string; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitString(this);
			else return visitor.visitChildren(this);
		}
	}

	public final StringContext string() throws RecognitionException {
		StringContext _localctx = new StringContext(_ctx, getState());
		enterRule(_localctx, 74, RULE_string);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(413);
			match(STRING_LITERAL);
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

	public static class Any_nameContext extends ParserRuleContext {
		public TerminalNode IDENTIFIER() { return getToken(CEPLParser.IDENTIFIER, 0); }
		public Any_nameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_any_name; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitAny_name(this);
			else return visitor.visitChildren(this);
		}
	}

	public final Any_nameContext any_name() throws RecognitionException {
		Any_nameContext _localctx = new Any_nameContext(_ctx, getState());
		enterRule(_localctx, 76, RULE_any_name);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(415);
			match(IDENTIFIER);
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

	public static class KeywordContext extends ParserRuleContext {
		public TerminalNode K_ALL() { return getToken(CEPLParser.K_ALL, 0); }
		public TerminalNode K_AND() { return getToken(CEPLParser.K_AND, 0); }
		public TerminalNode K_ANY() { return getToken(CEPLParser.K_ANY, 0); }
		public TerminalNode K_AS() { return getToken(CEPLParser.K_AS, 0); }
		public TerminalNode K_BY() { return getToken(CEPLParser.K_BY, 0); }
		public TerminalNode K_CONSUME() { return getToken(CEPLParser.K_CONSUME, 0); }
		public TerminalNode K_DECLARE() { return getToken(CEPLParser.K_DECLARE, 0); }
		public TerminalNode K_DISTINCT() { return getToken(CEPLParser.K_DISTINCT, 0); }
		public TerminalNode K_EVENT() { return getToken(CEPLParser.K_EVENT, 0); }
		public TerminalNode K_EVENTS() { return getToken(CEPLParser.K_EVENTS, 0); }
		public TerminalNode K_FILTER() { return getToken(CEPLParser.K_FILTER, 0); }
		public TerminalNode K_FROM() { return getToken(CEPLParser.K_FROM, 0); }
		public TerminalNode K_HOURS() { return getToken(CEPLParser.K_HOURS, 0); }
		public TerminalNode K_IN() { return getToken(CEPLParser.K_IN, 0); }
		public TerminalNode K_LAST() { return getToken(CEPLParser.K_LAST, 0); }
		public TerminalNode K_LIKE() { return getToken(CEPLParser.K_LIKE, 0); }
		public TerminalNode K_MAX() { return getToken(CEPLParser.K_MAX, 0); }
		public TerminalNode K_MINUTES() { return getToken(CEPLParser.K_MINUTES, 0); }
		public TerminalNode K_NEXT() { return getToken(CEPLParser.K_NEXT, 0); }
		public TerminalNode K_NONE() { return getToken(CEPLParser.K_NONE, 0); }
		public TerminalNode K_NOT() { return getToken(CEPLParser.K_NOT, 0); }
		public TerminalNode K_OR() { return getToken(CEPLParser.K_OR, 0); }
		public TerminalNode K_PARTITION() { return getToken(CEPLParser.K_PARTITION, 0); }
		public TerminalNode K_SECONDS() { return getToken(CEPLParser.K_SECONDS, 0); }
		public TerminalNode K_SELECT() { return getToken(CEPLParser.K_SELECT, 0); }
		public TerminalNode K_STREAM() { return getToken(CEPLParser.K_STREAM, 0); }
		public TerminalNode K_STRICT() { return getToken(CEPLParser.K_STRICT, 0); }
		public TerminalNode K_UNLESS() { return getToken(CEPLParser.K_UNLESS, 0); }
		public TerminalNode K_WHERE() { return getToken(CEPLParser.K_WHERE, 0); }
		public TerminalNode K_WITHIN() { return getToken(CEPLParser.K_WITHIN, 0); }
		public KeywordContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_keyword; }
		@Override
		public <T> T accept(ParseTreeVisitor<? extends T> visitor) {
			if ( visitor instanceof CEPLVisitor ) return ((CEPLVisitor<? extends T>)visitor).visitKeyword(this);
			else return visitor.visitChildren(this);
		}
	}

	public final KeywordContext keyword() throws RecognitionException {
		KeywordContext _localctx = new KeywordContext(_ctx, getState());
		enterRule(_localctx, 78, RULE_keyword);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(417);
			_la = _input.LA(1);
			if ( !((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << K_ALL) | (1L << K_AND) | (1L << K_ANY) | (1L << K_AS) | (1L << K_BY) | (1L << K_CONSUME) | (1L << K_DECLARE) | (1L << K_DISTINCT) | (1L << K_EVENT) | (1L << K_EVENTS) | (1L << K_FILTER) | (1L << K_FROM) | (1L << K_HOURS) | (1L << K_IN) | (1L << K_LAST) | (1L << K_LIKE) | (1L << K_MAX) | (1L << K_MINUTES) | (1L << K_NEXT) | (1L << K_NONE) | (1L << K_NOT) | (1L << K_OR) | (1L << K_PARTITION) | (1L << K_SECONDS) | (1L << K_SELECT) | (1L << K_STREAM) | (1L << K_STRICT) | (1L << K_UNLESS) | (1L << K_WHERE) | (1L << K_WITHIN))) != 0)) ) {
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

	public boolean sempred(RuleContext _localctx, int ruleIndex, int predIndex) {
		switch (ruleIndex) {
		case 14:
			return cel_pattern_sempred((Cel_patternContext)_localctx, predIndex);
		case 18:
			return filter_sempred((FilterContext)_localctx, predIndex);
		case 19:
			return bool_expr_sempred((Bool_exprContext)_localctx, predIndex);
		case 21:
			return math_expr_sempred((Math_exprContext)_localctx, predIndex);
		}
		return true;
	}
	private boolean cel_pattern_sempred(Cel_patternContext _localctx, int predIndex) {
		switch (predIndex) {
		case 0:
			return precpred(_ctx, 2);
		case 1:
			return precpred(_ctx, 4);
		case 2:
			return precpred(_ctx, 3);
		case 3:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean filter_sempred(FilterContext _localctx, int predIndex) {
		switch (predIndex) {
		case 4:
			return precpred(_ctx, 2);
		case 5:
			return precpred(_ctx, 1);
		}
		return true;
	}
	private boolean bool_expr_sempred(Bool_exprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 6:
			return precpred(_ctx, 4);
		case 7:
			return precpred(_ctx, 3);
		}
		return true;
	}
	private boolean math_expr_sempred(Math_exprContext _localctx, int predIndex) {
		switch (predIndex) {
		case 8:
			return precpred(_ctx, 2);
		case 9:
			return precpred(_ctx, 1);
		}
		return true;
	}

	public static final String _serializedATN =
		"\3\u608b\ua72a\u8133\ub9ed\u417c\u3be7\u7786\u5964\3?\u01a6\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\4(\t(\4)\t)\3\2\3\2\7\2U\n"+
		"\2\f\2\16\2X\13\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\5\4a\n\4\3\5\3\5\5\5e\n"+
		"\5\3\6\3\6\3\6\3\6\3\6\3\6\3\6\3\7\3\7\3\7\3\7\7\7r\n\7\f\7\16\7u\13\7"+
		"\5\7w\n\7\3\b\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\n\3\n\3\n\3\n\3\13\3\13\3"+
		"\13\7\13\u0088\n\13\f\13\16\13\u008b\13\13\3\f\3\f\5\f\u008f\n\f\3\f\3"+
		"\f\3\f\5\f\u0094\n\f\3\f\3\f\3\f\3\f\3\f\5\f\u009b\n\f\3\f\3\f\5\f\u009f"+
		"\n\f\3\f\3\f\3\f\5\f\u00a4\n\f\3\r\3\r\3\r\3\r\3\r\5\r\u00ab\n\r\3\16"+
		"\3\16\3\16\7\16\u00b0\n\16\f\16\16\16\u00b3\13\16\3\17\3\17\3\17\3\17"+
		"\7\17\u00b9\n\17\f\17\16\17\u00bc\13\17\5\17\u00be\n\17\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\5\20\u00c6\n\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20\3\20"+
		"\3\20\3\20\3\20\7\20\u00d3\n\20\f\20\16\20\u00d6\13\20\3\21\3\21\3\21"+
		"\3\21\3\21\7\21\u00dd\n\21\f\21\16\21\u00e0\13\21\3\22\3\22\3\22\7\22"+
		"\u00e5\n\22\f\22\16\22\u00e8\13\22\3\23\3\23\3\23\5\23\u00ed\n\23\3\24"+
		"\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\24\5\24\u00f9\n\24\3\24\3\24"+
		"\3\24\3\24\3\24\3\24\7\24\u0101\n\24\f\24\16\24\u0104\13\24\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\5\25\u0121\n\25"+
		"\3\25\3\25\5\25\u0125\n\25\3\25\3\25\3\25\3\25\3\25\3\25\7\25\u012d\n"+
		"\25\f\25\16\25\u0130\13\25\3\26\3\26\5\26\u0134\n\26\3\27\3\27\3\27\3"+
		"\27\3\27\3\27\3\27\3\27\3\27\5\27\u013f\n\27\3\27\3\27\3\27\3\27\3\27"+
		"\3\27\7\27\u0147\n\27\f\27\16\27\u014a\13\27\3\30\3\30\5\30\u014e\n\30"+
		"\3\31\3\31\3\31\3\31\7\31\u0154\n\31\f\31\16\31\u0157\13\31\3\31\3\31"+
		"\3\31\3\31\3\31\3\31\3\31\3\31\5\31\u0161\n\31\3\32\3\32\3\32\3\32\7\32"+
		"\u0167\n\32\f\32\16\32\u016a\13\32\3\32\3\32\3\33\3\33\5\33\u0170\n\33"+
		"\3\34\3\34\3\34\3\35\3\35\5\35\u0177\n\35\3\35\5\35\u017a\n\35\3\35\3"+
		"\35\5\35\u017e\n\35\3\35\5\35\u0181\n\35\3\36\3\36\3\36\3\37\3\37\3\37"+
		"\3 \3 \3 \3!\3!\3!\5!\u018f\n!\3\"\3\"\3\"\5\"\u0194\n\"\3\"\3\"\3#\3"+
		"#\3$\3$\3%\3%\3&\3&\3\'\3\'\3(\3(\3)\3)\3)\2\6\36&(,*\2\4\6\b\n\f\16\20"+
		"\22\24\26\30\32\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJLNP\2\t\3\2\6\t\4"+
		"\2\n\n##\3\2\61\64\3\2\65\66\3\2-.\4\2,,/\60\3\2\16+\2\u01b7\2V\3\2\2"+
		"\2\4[\3\2\2\2\6`\3\2\2\2\bd\3\2\2\2\nf\3\2\2\2\fv\3\2\2\2\16x\3\2\2\2"+
		"\20{\3\2\2\2\22}\3\2\2\2\24\u0084\3\2\2\2\26\u008c\3\2\2\2\30\u00aa\3"+
		"\2\2\2\32\u00ac\3\2\2\2\34\u00bd\3\2\2\2\36\u00c5\3\2\2\2 \u00d7\3\2\2"+
		"\2\"\u00e1\3\2\2\2$\u00ec\3\2\2\2&\u00f8\3\2\2\2(\u0124\3\2\2\2*\u0133"+
		"\3\2\2\2,\u013e\3\2\2\2.\u014d\3\2\2\2\60\u0160\3\2\2\2\62\u0162\3\2\2"+
		"\2\64\u016f\3\2\2\2\66\u0171\3\2\2\28\u0180\3\2\2\2:\u0182\3\2\2\2<\u0185"+
		"\3\2\2\2>\u0188\3\2\2\2@\u018b\3\2\2\2B\u0193\3\2\2\2D\u0197\3\2\2\2F"+
		"\u0199\3\2\2\2H\u019b\3\2\2\2J\u019d\3\2\2\2L\u019f\3\2\2\2N\u01a1\3\2"+
		"\2\2P\u01a3\3\2\2\2RU\5\6\4\2SU\5\4\3\2TR\3\2\2\2TS\3\2\2\2UX\3\2\2\2"+
		"VT\3\2\2\2VW\3\2\2\2WY\3\2\2\2XV\3\2\2\2YZ\7\2\2\3Z\3\3\2\2\2[\\\7?\2"+
		"\2\\]\b\3\1\2]\5\3\2\2\2^a\5\26\f\2_a\5\b\5\2`^\3\2\2\2`_\3\2\2\2a\7\3"+
		"\2\2\2be\5\n\6\2ce\5\22\n\2db\3\2\2\2dc\3\2\2\2e\t\3\2\2\2fg\7\24\2\2"+
		"gh\7\26\2\2hi\5D#\2ij\7\3\2\2jk\5\f\7\2kl\7\4\2\2l\13\3\2\2\2mw\3\2\2"+
		"\2ns\5\16\b\2op\7\5\2\2pr\5\16\b\2qo\3\2\2\2ru\3\2\2\2sq\3\2\2\2st\3\2"+
		"\2\2tw\3\2\2\2us\3\2\2\2vm\3\2\2\2vn\3\2\2\2w\r\3\2\2\2xy\5H%\2yz\5\20"+
		"\t\2z\17\3\2\2\2{|\t\2\2\2|\21\3\2\2\2}~\7\24\2\2~\177\7\'\2\2\177\u0080"+
		"\5F$\2\u0080\u0081\7\3\2\2\u0081\u0082\5\24\13\2\u0082\u0083\7\4\2\2\u0083"+
		"\23\3\2\2\2\u0084\u0089\5D#\2\u0085\u0086\7\5\2\2\u0086\u0088\5D#\2\u0087"+
		"\u0085\3\2\2\2\u0088\u008b\3\2\2\2\u0089\u0087\3\2\2\2\u0089\u008a\3\2"+
		"\2\2\u008a\25\3\2\2\2\u008b\u0089\3\2\2\2\u008c\u008e\7&\2\2\u008d\u008f"+
		"\5\30\r\2\u008e\u008d\3\2\2\2\u008e\u008f\3\2\2\2\u008f\u0090\3\2\2\2"+
		"\u0090\u0093\5\34\17\2\u0091\u0092\7\31\2\2\u0092\u0094\5\32\16\2\u0093"+
		"\u0091\3\2\2\2\u0093\u0094\3\2\2\2\u0094\u0095\3\2\2\2\u0095\u0096\7*"+
		"\2\2\u0096\u009a\5\36\20\2\u0097\u0098\7$\2\2\u0098\u0099\7\22\2\2\u0099"+
		"\u009b\5 \21\2\u009a\u0097\3\2\2\2\u009a\u009b\3\2\2\2\u009b\u009e\3\2"+
		"\2\2\u009c\u009d\7+\2\2\u009d\u009f\5\64\33\2\u009e\u009c\3\2\2\2\u009e"+
		"\u009f\3\2\2\2\u009f\u00a3\3\2\2\2\u00a0\u00a1\7\23\2\2\u00a1\u00a2\7"+
		"\22\2\2\u00a2\u00a4\5$\23\2\u00a3\u00a0\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4"+
		"\27\3\2\2\2\u00a5\u00ab\7\16\2\2\u00a6\u00ab\7\34\2\2\u00a7\u00ab\7\36"+
		"\2\2\u00a8\u00ab\7 \2\2\u00a9\u00ab\7(\2\2\u00aa\u00a5\3\2\2\2\u00aa\u00a6"+
		"\3\2\2\2\u00aa\u00a7\3\2\2\2\u00aa\u00a8\3\2\2\2\u00aa\u00a9\3\2\2\2\u00ab"+
		"\31\3\2\2\2\u00ac\u00b1\5F$\2\u00ad\u00ae\7\5\2\2\u00ae\u00b0\5F$\2\u00af"+
		"\u00ad\3\2\2\2\u00b0\u00b3\3\2\2\2\u00b1\u00af\3\2\2\2\u00b1\u00b2\3\2"+
		"\2\2\u00b2\33\3\2\2\2\u00b3\u00b1\3\2\2\2\u00b4\u00be\7/\2\2\u00b5\u00ba"+
		"\5D#\2\u00b6\u00b7\7\5\2\2\u00b7\u00b9\5D#\2\u00b8\u00b6\3\2\2\2\u00b9"+
		"\u00bc\3\2\2\2\u00ba\u00b8\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00be\3\2"+
		"\2\2\u00bc\u00ba\3\2\2\2\u00bd\u00b4\3\2\2\2\u00bd\u00b5\3\2\2\2\u00be"+
		"\35\3\2\2\2\u00bf\u00c0\b\20\1\2\u00c0\u00c1\7\3\2\2\u00c1\u00c2\5\36"+
		"\20\2\u00c2\u00c3\7\4\2\2\u00c3\u00c6\3\2\2\2\u00c4\u00c6\5B\"\2\u00c5"+
		"\u00bf\3\2\2\2\u00c5\u00c4\3\2\2\2\u00c6\u00d4\3\2\2\2\u00c7\u00c8\f\4"+
		"\2\2\u00c8\u00c9\t\3\2\2\u00c9\u00d3\5\36\20\5\u00ca\u00cb\f\6\2\2\u00cb"+
		"\u00cc\7\21\2\2\u00cc\u00d3\5D#\2\u00cd\u00ce\f\5\2\2\u00ce\u00d3\7-\2"+
		"\2\u00cf\u00d0\f\3\2\2\u00d0\u00d1\7\30\2\2\u00d1\u00d3\5&\24\2\u00d2"+
		"\u00c7\3\2\2\2\u00d2\u00ca\3\2\2\2\u00d2\u00cd\3\2\2\2\u00d2\u00cf\3\2"+
		"\2\2\u00d3\u00d6\3\2\2\2\u00d4\u00d2\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5"+
		"\37\3\2\2\2\u00d6\u00d4\3\2\2\2\u00d7\u00d8\7\13\2\2\u00d8\u00d9\5\"\22"+
		"\2\u00d9\u00de\7\f\2\2\u00da\u00db\7\5\2\2\u00db\u00dd\5\"\22\2\u00dc"+
		"\u00da\3\2\2\2\u00dd\u00e0\3\2\2\2\u00de\u00dc\3\2\2\2\u00de\u00df\3\2"+
		"\2\2\u00df!\3\2\2\2\u00e0\u00de\3\2\2\2\u00e1\u00e6\5H%\2\u00e2\u00e3"+
		"\7\5\2\2\u00e3\u00e5\5H%\2\u00e4\u00e2\3\2\2\2\u00e5\u00e8\3\2\2\2\u00e6"+
		"\u00e4\3\2\2\2\u00e6\u00e7\3\2\2\2\u00e7#\3\2\2\2\u00e8\u00e6\3\2\2\2"+
		"\u00e9\u00ed\7\20\2\2\u00ea\u00ed\7$\2\2\u00eb\u00ed\7!\2\2\u00ec\u00e9"+
		"\3\2\2\2\u00ec\u00ea\3\2\2\2\u00ec\u00eb\3\2\2\2\u00ed%\3\2\2\2\u00ee"+
		"\u00ef\b\24\1\2\u00ef\u00f0\7\3\2\2\u00f0\u00f1\5&\24\2\u00f1\u00f2\7"+
		"\4\2\2\u00f2\u00f9\3\2\2\2\u00f3\u00f4\5D#\2\u00f4\u00f5\7\13\2\2\u00f5"+
		"\u00f6\5(\25\2\u00f6\u00f7\7\f\2\2\u00f7\u00f9\3\2\2\2\u00f8\u00ee\3\2"+
		"\2\2\u00f8\u00f3\3\2\2\2\u00f9\u0102\3\2\2\2\u00fa\u00fb\f\4\2\2\u00fb"+
		"\u00fc\7\17\2\2\u00fc\u0101\5&\24\5\u00fd\u00fe\f\3\2\2\u00fe\u00ff\7"+
		"#\2\2\u00ff\u0101\5&\24\4\u0100\u00fa\3\2\2\2\u0100\u00fd\3\2\2\2\u0101"+
		"\u0104\3\2\2\2\u0102\u0100\3\2\2\2\u0102\u0103\3\2\2\2\u0103\'\3\2\2\2"+
		"\u0104\u0102\3\2\2\2\u0105\u0106\b\25\1\2\u0106\u0107\7\3\2\2\u0107\u0108"+
		"\5(\25\2\u0108\u0109\7\4\2\2\u0109\u0125\3\2\2\2\u010a\u010b\7\"\2\2\u010b"+
		"\u0125\5(\25\n\u010c\u010d\5,\27\2\u010d\u010e\t\4\2\2\u010e\u010f\5,"+
		"\27\2\u010f\u0125\3\2\2\2\u0110\u0111\5,\27\2\u0111\u0112\t\5\2\2\u0112"+
		"\u0113\5,\27\2\u0113\u0125\3\2\2\2\u0114\u0115\5*\26\2\u0115\u0116\t\5"+
		"\2\2\u0116\u0117\5*\26\2\u0117\u0125\3\2\2\2\u0118\u0119\5H%\2\u0119\u011a"+
		"\7\35\2\2\u011a\u011b\5L\'\2\u011b\u0125\3\2\2\2\u011c\u0120\5H%\2\u011d"+
		"\u0121\7\33\2\2\u011e\u011f\7\"\2\2\u011f\u0121\7\33\2\2\u0120\u011d\3"+
		"\2\2\2\u0120\u011e\3\2\2\2\u0121\u0122\3\2\2\2\u0122\u0123\5.\30\2\u0123"+
		"\u0125\3\2\2\2\u0124\u0105\3\2\2\2\u0124\u010a\3\2\2\2\u0124\u010c\3\2"+
		"\2\2\u0124\u0110\3\2\2\2\u0124\u0114\3\2\2\2\u0124\u0118\3\2\2\2\u0124"+
		"\u011c\3\2\2\2\u0125\u012e\3\2\2\2\u0126\u0127\f\6\2\2\u0127\u0128\7\17"+
		"\2\2\u0128\u012d\5(\25\7\u0129\u012a\f\5\2\2\u012a\u012b\7#\2\2\u012b"+
		"\u012d\5(\25\6\u012c\u0126\3\2\2\2\u012c\u0129\3\2\2\2\u012d\u0130\3\2"+
		"\2\2\u012e\u012c\3\2\2\2\u012e\u012f\3\2\2\2\u012f)\3\2\2\2\u0130\u012e"+
		"\3\2\2\2\u0131\u0134\5L\'\2\u0132\u0134\5H%\2\u0133\u0131\3\2\2\2\u0133"+
		"\u0132\3\2\2\2\u0134+\3\2\2\2\u0135\u0136\b\27\1\2\u0136\u0137\7\3\2\2"+
		"\u0137\u0138\5,\27\2\u0138\u0139\7\4\2\2\u0139\u013f\3\2\2\2\u013a\u013f"+
		"\5J&\2\u013b\u013f\5H%\2\u013c\u013d\t\6\2\2\u013d\u013f\5,\27\5\u013e"+
		"\u0135\3\2\2\2\u013e\u013a\3\2\2\2\u013e\u013b\3\2\2\2\u013e\u013c\3\2"+
		"\2\2\u013f\u0148\3\2\2\2\u0140\u0141\f\4\2\2\u0141\u0142\t\7\2\2\u0142"+
		"\u0147\5,\27\5\u0143\u0144\f\3\2\2\u0144\u0145\t\6\2\2\u0145\u0147\5,"+
		"\27\4\u0146\u0140\3\2\2\2\u0146\u0143\3\2\2\2\u0147\u014a\3\2\2\2\u0148"+
		"\u0146\3\2\2\2\u0148\u0149\3\2\2\2\u0149-\3\2\2\2\u014a\u0148\3\2\2\2"+
		"\u014b\u014e\5\60\31\2\u014c\u014e\5\62\32\2\u014d\u014b\3\2\2\2\u014d"+
		"\u014c\3\2\2\2\u014e/\3\2\2\2\u014f\u0150\7\13\2\2\u0150\u0155\5J&\2\u0151"+
		"\u0152\7\5\2\2\u0152\u0154\5J&\2\u0153\u0151\3\2\2\2\u0154\u0157\3\2\2"+
		"\2\u0155\u0153\3\2\2\2\u0155\u0156\3\2\2\2\u0156\u0158\3\2\2\2\u0157\u0155"+
		"\3\2\2\2\u0158\u0159\7\f\2\2\u0159\u0161\3\2\2\2\u015a\u015b\7\13\2\2"+
		"\u015b\u015c\5J&\2\u015c\u015d\7\r\2\2\u015d\u015e\5J&\2\u015e\u015f\7"+
		"\f\2\2\u015f\u0161\3\2\2\2\u0160\u014f\3\2\2\2\u0160\u015a\3\2\2\2\u0161"+
		"\61\3\2\2\2\u0162\u0163\7\13\2\2\u0163\u0168\5L\'\2\u0164\u0165\7\5\2"+
		"\2\u0165\u0167\5L\'\2\u0166\u0164\3\2\2\2\u0167\u016a\3\2\2\2\u0168\u0166"+
		"\3\2\2\2\u0168\u0169\3\2\2\2\u0169\u016b\3\2\2\2\u016a\u0168\3\2\2\2\u016b"+
		"\u016c\7\f\2\2\u016c\63\3\2\2\2\u016d\u0170\5\66\34\2\u016e\u0170\58\35"+
		"\2\u016f\u016d\3\2\2\2\u016f\u016e\3\2\2\2\u0170\65\3\2\2\2\u0171\u0172"+
		"\5J&\2\u0172\u0173\7\27\2\2\u0173\67\3\2\2\2\u0174\u0176\5:\36\2\u0175"+
		"\u0177\5<\37\2\u0176\u0175\3\2\2\2\u0176\u0177\3\2\2\2\u0177\u0179\3\2"+
		"\2\2\u0178\u017a\5> \2\u0179\u0178\3\2\2\2\u0179\u017a\3\2\2\2\u017a\u0181"+
		"\3\2\2\2\u017b\u017d\5<\37\2\u017c\u017e\5> \2\u017d\u017c\3\2\2\2\u017d"+
		"\u017e\3\2\2\2\u017e\u0181\3\2\2\2\u017f\u0181\5> \2\u0180\u0174\3\2\2"+
		"\2\u0180\u017b\3\2\2\2\u0180\u017f\3\2\2\2\u01819\3\2\2\2\u0182\u0183"+
		"\5J&\2\u0183\u0184\7\32\2\2\u0184;\3\2\2\2\u0185\u0186\5J&\2\u0186\u0187"+
		"\7\37\2\2\u0187=\3\2\2\2\u0188\u0189\5J&\2\u0189\u018a\7%\2\2\u018a?\3"+
		"\2\2\2\u018b\u018e\5B\"\2\u018c\u018d\7\21\2\2\u018d\u018f\5D#\2\u018e"+
		"\u018c\3\2\2\2\u018e\u018f\3\2\2\2\u018fA\3\2\2\2\u0190\u0191\5F$\2\u0191"+
		"\u0192\7\63\2\2\u0192\u0194\3\2\2\2\u0193\u0190\3\2\2\2\u0193\u0194\3"+
		"\2\2\2\u0194\u0195\3\2\2\2\u0195\u0196\5D#\2\u0196C\3\2\2\2\u0197\u0198"+
		"\5N(\2\u0198E\3\2\2\2\u0199\u019a\5N(\2\u019aG\3\2\2\2\u019b\u019c\5N"+
		"(\2\u019cI\3\2\2\2\u019d\u019e\78\2\2\u019eK\3\2\2\2\u019f\u01a0\7;\2"+
		"\2\u01a0M\3\2\2\2\u01a1\u01a2\7\67\2\2\u01a2O\3\2\2\2\u01a3\u01a4\t\b"+
		"\2\2\u01a4Q\3\2\2\2.TV`dsv\u0089\u008e\u0093\u009a\u009e\u00a3\u00aa\u00b1"+
		"\u00ba\u00bd\u00c5\u00d2\u00d4\u00de\u00e6\u00ec\u00f8\u0100\u0102\u0120"+
		"\u0124\u012c\u012e\u0133\u013e\u0146\u0148\u014d\u0155\u0160\u0168\u016f"+
		"\u0176\u0179\u017d\u0180\u018e\u0193";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}