// Generated from CEPL.g4 by ANTLR 4.7.1
package cepl.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CEPLParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CEPLVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CEPLParser#parse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParse(CEPLParser.ParseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#error}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError(CEPLParser.ErrorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#cel_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCel_stmt(CEPLParser.Cel_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#cel_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCel_declaration(CEPLParser.Cel_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#event_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_declaration(CEPLParser.Event_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#attribute_dec_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_dec_list(CEPLParser.Attribute_dec_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#attribute_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_declaration(CEPLParser.Attribute_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#datatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatatype(CEPLParser.DatatypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#stream_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStream_declaration(CEPLParser.Stream_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#event_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_list(CEPLParser.Event_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#cel_query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCel_query(CEPLParser.Cel_queryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ss_all}
	 * labeled alternative in {@link CEPLParser#selection_strategy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSs_all(CEPLParser.Ss_allContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ss_any}
	 * labeled alternative in {@link CEPLParser#selection_strategy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSs_any(CEPLParser.Ss_anyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ss_max}
	 * labeled alternative in {@link CEPLParser#selection_strategy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSs_max(CEPLParser.Ss_maxContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ss_next}
	 * labeled alternative in {@link CEPLParser#selection_strategy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSs_next(CEPLParser.Ss_nextContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ss_strict}
	 * labeled alternative in {@link CEPLParser#selection_strategy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSs_strict(CEPLParser.Ss_strictContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#result_values}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResult_values(CEPLParser.Result_valuesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code par_cel_pattern}
	 * labeled alternative in {@link CEPLParser#cel_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPar_cel_pattern(CEPLParser.Par_cel_patternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binary_cel_pattern}
	 * labeled alternative in {@link CEPLParser#cel_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinary_cel_pattern(CEPLParser.Binary_cel_patternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assign_cel_pattern}
	 * labeled alternative in {@link CEPLParser#cel_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign_cel_pattern(CEPLParser.Assign_cel_patternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code kleene_cel_pattern}
	 * labeled alternative in {@link CEPLParser#cel_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKleene_cel_pattern(CEPLParser.Kleene_cel_patternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code event_cel_pattern}
	 * labeled alternative in {@link CEPLParser#cel_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_cel_pattern(CEPLParser.Event_cel_patternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code filter_cel_pattern}
	 * labeled alternative in {@link CEPLParser#cel_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilter_cel_pattern(CEPLParser.Filter_cel_patternContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#partition_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_list(CEPLParser.Partition_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#attribute_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_list(CEPLParser.Attribute_listContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cp_any}
	 * labeled alternative in {@link CEPLParser#consumption_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCp_any(CEPLParser.Cp_anyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cp_partition}
	 * labeled alternative in {@link CEPLParser#consumption_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCp_partition(CEPLParser.Cp_partitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cp_none}
	 * labeled alternative in {@link CEPLParser#consumption_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCp_none(CEPLParser.Cp_noneContext ctx);
	/**
	 * Visit a parse tree produced by the {@code par_filter}
	 * labeled alternative in {@link CEPLParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPar_filter(CEPLParser.Par_filterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code and_filter}
	 * labeled alternative in {@link CEPLParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd_filter(CEPLParser.And_filterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code event_filter}
	 * labeled alternative in {@link CEPLParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_filter(CEPLParser.Event_filterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code or_filter}
	 * labeled alternative in {@link CEPLParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr_filter(CEPLParser.Or_filterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code not_expr}
	 * labeled alternative in {@link CEPLParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot_expr(CEPLParser.Not_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code equality_string_expr}
	 * labeled alternative in {@link CEPLParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquality_string_expr(CEPLParser.Equality_string_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code and_expr}
	 * labeled alternative in {@link CEPLParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd_expr(CEPLParser.And_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code par_bool_expr}
	 * labeled alternative in {@link CEPLParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPar_bool_expr(CEPLParser.Par_bool_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code containment_expr}
	 * labeled alternative in {@link CEPLParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainment_expr(CEPLParser.Containment_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code inequality_expr}
	 * labeled alternative in {@link CEPLParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInequality_expr(CEPLParser.Inequality_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code or_expr}
	 * labeled alternative in {@link CEPLParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr_expr(CEPLParser.Or_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code equality_math_expr}
	 * labeled alternative in {@link CEPLParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquality_math_expr(CEPLParser.Equality_math_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code regex_expr}
	 * labeled alternative in {@link CEPLParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegex_expr(CEPLParser.Regex_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#string_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_literal(CEPLParser.String_literalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mul_math_expr}
	 * labeled alternative in {@link CEPLParser#math_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMul_math_expr(CEPLParser.Mul_math_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sum_math_expr}
	 * labeled alternative in {@link CEPLParser#math_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSum_math_expr(CEPLParser.Sum_math_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code number_math_expr}
	 * labeled alternative in {@link CEPLParser#math_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber_math_expr(CEPLParser.Number_math_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unary_math_expr}
	 * labeled alternative in {@link CEPLParser#math_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_math_expr(CEPLParser.Unary_math_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code attribute_math_expr}
	 * labeled alternative in {@link CEPLParser#math_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_math_expr(CEPLParser.Attribute_math_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code par_math_expr}
	 * labeled alternative in {@link CEPLParser#math_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPar_math_expr(CEPLParser.Par_math_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#value_seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_seq(CEPLParser.Value_seqContext ctx);
	/**
	 * Visit a parse tree produced by the {@code number_list}
	 * labeled alternative in {@link CEPLParser#number_seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber_list(CEPLParser.Number_listContext ctx);
	/**
	 * Visit a parse tree produced by the {@code number_range}
	 * labeled alternative in {@link CEPLParser#number_seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber_range(CEPLParser.Number_rangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#string_seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_seq(CEPLParser.String_seqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#time_window}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTime_window(CEPLParser.Time_windowContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#event_span}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_span(CEPLParser.Event_spanContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#time_span}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTime_span(CEPLParser.Time_spanContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#named_event}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNamed_event(CEPLParser.Named_eventContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#s_event_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitS_event_name(CEPLParser.S_event_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#event_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_name(CEPLParser.Event_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#stream_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStream_name(CEPLParser.Stream_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#attribute_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_name(CEPLParser.Attribute_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(CEPLParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#integer}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInteger(CEPLParser.IntegerContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(CEPLParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#any_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny_name(CEPLParser.Any_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CEPLParser#keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyword(CEPLParser.KeywordContext ctx);
}