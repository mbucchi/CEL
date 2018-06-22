// Generated from CEL.g4 by ANTLR 4.7.1
package cel.parser;
import org.antlr.v4.runtime.tree.ParseTreeVisitor;

/**
 * This interface defines a complete generic visitor for a parse tree produced
 * by {@link CELParser}.
 *
 * @param <T> The return type of the visit operation. Use {@link Void} for
 * operations with no return type.
 */
public interface CELVisitor<T> extends ParseTreeVisitor<T> {
	/**
	 * Visit a parse tree produced by {@link CELParser#parse}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitParse(CELParser.ParseContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#error}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitError(CELParser.ErrorContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#cel_stmt}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCel_stmt(CELParser.Cel_stmtContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#cel_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCel_declaration(CELParser.Cel_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#event_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_declaration(CELParser.Event_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#attribute_dec_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_dec_list(CELParser.Attribute_dec_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#attribute_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_declaration(CELParser.Attribute_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#datatype}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitDatatype(CELParser.DatatypeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#stream_declaration}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStream_declaration(CELParser.Stream_declarationContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#event_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_list(CELParser.Event_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#cel_query}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCel_query(CELParser.Cel_queryContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ss_all}
	 * labeled alternative in {@link CELParser#selection_strategy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSs_all(CELParser.Ss_allContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ss_last}
	 * labeled alternative in {@link CELParser#selection_strategy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSs_last(CELParser.Ss_lastContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ss_max}
	 * labeled alternative in {@link CELParser#selection_strategy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSs_max(CELParser.Ss_maxContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ss_next}
	 * labeled alternative in {@link CELParser#selection_strategy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSs_next(CELParser.Ss_nextContext ctx);
	/**
	 * Visit a parse tree produced by the {@code ss_strict}
	 * labeled alternative in {@link CELParser#selection_strategy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSs_strict(CELParser.Ss_strictContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#stream_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStream_list(CELParser.Stream_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#result_values}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitResult_values(CELParser.Result_valuesContext ctx);
	/**
	 * Visit a parse tree produced by the {@code par_cel_pattern}
	 * labeled alternative in {@link CELParser#cel_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPar_cel_pattern(CELParser.Par_cel_patternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code binary_cel_pattern}
	 * labeled alternative in {@link CELParser#cel_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitBinary_cel_pattern(CELParser.Binary_cel_patternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code assign_cel_pattern}
	 * labeled alternative in {@link CELParser#cel_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAssign_cel_pattern(CELParser.Assign_cel_patternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code kleene_cel_pattern}
	 * labeled alternative in {@link CELParser#cel_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKleene_cel_pattern(CELParser.Kleene_cel_patternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code event_cel_pattern}
	 * labeled alternative in {@link CELParser#cel_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_cel_pattern(CELParser.Event_cel_patternContext ctx);
	/**
	 * Visit a parse tree produced by the {@code filter_cel_pattern}
	 * labeled alternative in {@link CELParser#cel_pattern}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitFilter_cel_pattern(CELParser.Filter_cel_patternContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#partition_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPartition_list(CELParser.Partition_listContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#attribute_list}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_list(CELParser.Attribute_listContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cp_any}
	 * labeled alternative in {@link CELParser#consumption_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCp_any(CELParser.Cp_anyContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cp_partition}
	 * labeled alternative in {@link CELParser#consumption_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCp_partition(CELParser.Cp_partitionContext ctx);
	/**
	 * Visit a parse tree produced by the {@code cp_none}
	 * labeled alternative in {@link CELParser#consumption_policy}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitCp_none(CELParser.Cp_noneContext ctx);
	/**
	 * Visit a parse tree produced by the {@code par_filter}
	 * labeled alternative in {@link CELParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPar_filter(CELParser.Par_filterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code and_filter}
	 * labeled alternative in {@link CELParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd_filter(CELParser.And_filterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code event_filter}
	 * labeled alternative in {@link CELParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_filter(CELParser.Event_filterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code or_filter}
	 * labeled alternative in {@link CELParser#filter}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr_filter(CELParser.Or_filterContext ctx);
	/**
	 * Visit a parse tree produced by the {@code not_expr}
	 * labeled alternative in {@link CELParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNot_expr(CELParser.Not_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code equality_string_expr}
	 * labeled alternative in {@link CELParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquality_string_expr(CELParser.Equality_string_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code and_expr}
	 * labeled alternative in {@link CELParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAnd_expr(CELParser.And_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code par_bool_expr}
	 * labeled alternative in {@link CELParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPar_bool_expr(CELParser.Par_bool_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code containment_expr}
	 * labeled alternative in {@link CELParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitContainment_expr(CELParser.Containment_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code inequality_expr}
	 * labeled alternative in {@link CELParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitInequality_expr(CELParser.Inequality_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code or_expr}
	 * labeled alternative in {@link CELParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitOr_expr(CELParser.Or_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code equality_math_expr}
	 * labeled alternative in {@link CELParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEquality_math_expr(CELParser.Equality_math_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code regex_expr}
	 * labeled alternative in {@link CELParser#bool_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitRegex_expr(CELParser.Regex_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#string_literal}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_literal(CELParser.String_literalContext ctx);
	/**
	 * Visit a parse tree produced by the {@code mul_math_expr}
	 * labeled alternative in {@link CELParser#math_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMul_math_expr(CELParser.Mul_math_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code sum_math_expr}
	 * labeled alternative in {@link CELParser#math_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSum_math_expr(CELParser.Sum_math_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code number_math_expr}
	 * labeled alternative in {@link CELParser#math_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber_math_expr(CELParser.Number_math_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code unary_math_expr}
	 * labeled alternative in {@link CELParser#math_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitUnary_math_expr(CELParser.Unary_math_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code attribute_math_expr}
	 * labeled alternative in {@link CELParser#math_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_math_expr(CELParser.Attribute_math_exprContext ctx);
	/**
	 * Visit a parse tree produced by the {@code par_math_expr}
	 * labeled alternative in {@link CELParser#math_expr}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitPar_math_expr(CELParser.Par_math_exprContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#value_seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitValue_seq(CELParser.Value_seqContext ctx);
	/**
	 * Visit a parse tree produced by the {@code number_list}
	 * labeled alternative in {@link CELParser#number_seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber_list(CELParser.Number_listContext ctx);
	/**
	 * Visit a parse tree produced by the {@code number_range}
	 * labeled alternative in {@link CELParser#number_seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber_range(CELParser.Number_rangeContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#string_seq}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString_seq(CELParser.String_seqContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#time_window}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTime_window(CELParser.Time_windowContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#event_span}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_span(CELParser.Event_spanContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#time_span}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitTime_span(CELParser.Time_spanContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#hours}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitHours(CELParser.HoursContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#minutes}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitMinutes(CELParser.MinutesContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#seconds}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitSeconds(CELParser.SecondsContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#s_event_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitS_event_name(CELParser.S_event_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#event_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitEvent_name(CELParser.Event_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#stream_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitStream_name(CELParser.Stream_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#attribute_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAttribute_name(CELParser.Attribute_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#number}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitNumber(CELParser.NumberContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#string}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitString(CELParser.StringContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#any_name}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitAny_name(CELParser.Any_nameContext ctx);
	/**
	 * Visit a parse tree produced by {@link CELParser#keyword}.
	 * @param ctx the parse tree
	 * @return the visitor result
	 */
	T visitKeyword(CELParser.KeywordContext ctx);
}