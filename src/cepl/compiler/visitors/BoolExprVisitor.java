package cepl.compiler.visitors;

import cepl.event.Label;
import cepl.values.*;
import cepl.filter.*;
import cepl.parser.CEPLBaseVisitor;
import cepl.parser.CEPLParser;

import java.util.Collection;
import java.util.stream.Collectors;

class BoolExprVisitor extends CEPLBaseVisitor<EventFilter> {

    private Label label;

    BoolExprVisitor(Label label){
        this.label = label;
    }

    @Override
    public EventFilter visitNot_expr(CEPLParser.Not_exprContext ctx) {
        EventFilter innerFilter = ctx.bool_expr().accept(this);
        return innerFilter.negate();
    }


    @Override
    public EventFilter visitAnd_expr(CEPLParser.And_exprContext ctx) {
        EventFilter left = ctx.bool_expr(0).accept(this);
        EventFilter right = ctx.bool_expr(1).accept(this);
        return new AndEventFilter(label, left, right);
    }

    @Override
    public EventFilter visitPar_bool_expr(CEPLParser.Par_bool_exprContext ctx) {
        // just ignore parenthesis
        return ctx.bool_expr().accept(this);
    }


    private Collection<Literal> parseNumberSeq(CEPLParser.Number_seqContext ctx){
        if (ctx instanceof CEPLParser.Number_listContext){
            // parse all numbers as number constants
            return ((CEPLParser.Number_listContext) ctx).number()
                    .stream()
                    .map(numberContext -> (Literal)new NumberLiteral(numberContext.getText()))
                    .collect(Collectors.toList());
        }
        // TODO: range containment filters
//        else if (ctx instanceof CEPLParser.Number_rangeContext){
//
//        }
        throw new Error("not implemented yet");
    }

    private Collection<Literal> parseStringSeq(CEPLParser.String_seqContext ctx){
            // parse all numbers as number constants
            return ctx.string()
                    .stream()
                    .map(stringContext -> (Literal)new StringLiteral(stringContext.getText()))
                    .collect(Collectors.toList());
    }

    @Override
    public EventFilter visitContainment_expr(CEPLParser.Containment_exprContext ctx) {
        LogicalOperation logicalOperation = ctx.K_NOT() == null ? LogicalOperation.IN : LogicalOperation.NOT_IN;
        Attribute attribute = new Attribute(ctx.attribute_name().getText());

        if (ctx.value_seq().number_seq() != null) {
            ContainmentEventFilter filter = new ContainmentEventFilter(
                    label,
                    attribute,
                    ValueType.NUMERIC,
                    logicalOperation,
                    parseNumberSeq(ctx.value_seq().number_seq()));

            return filter.translateToEventFilter();
        }
        else if (ctx.value_seq().string_seq() != null) {
            ContainmentEventFilter filter = new ContainmentEventFilter(
                    label,
                    attribute,
                    ValueType.STRING,
                    logicalOperation,
                    parseStringSeq(ctx.value_seq().string_seq()));

            return filter.translateToEventFilter();
        }
        else {
            throw new Error("Unknown sequence type");
        }
    }


    @Override
    public EventFilter visitInequality_expr(CEPLParser.Inequality_exprContext ctx) {
        // TODO: this is all wrong.
        //  1. The side DOES matter
        //  2. It is possible to compare two attributes

        MathExprVisitor mathExprVisitor = new MathExprVisitor();

        Value lhs = ctx.math_expr(0).accept(mathExprVisitor);
        Value rhs = ctx.math_expr(1).accept(mathExprVisitor);

        if (ctx.GE() != null) {
            return new InequalityEventFilter(label, lhs, LogicalOperation.GREATER, rhs);
        }
        else if (ctx.GEQ() != null) {
            return new InequalityEventFilter(label, lhs, LogicalOperation.GREATER_EQUALS, rhs);
        }
        else if (ctx.LEQ() != null) {
            return new InequalityEventFilter(label, lhs, LogicalOperation.LESS_EQUALS, rhs);
        }
        else if (ctx.LE() != null) {
            return new InequalityEventFilter(label, lhs, LogicalOperation.LESS, rhs);
        }
        throw new Error("Unknown inequality type");

    }


    @Override
    public EventFilter visitOr_expr(CEPLParser.Or_exprContext ctx) {
        EventFilter left = ctx.bool_expr(0).accept(this);
        EventFilter right = ctx.bool_expr(1).accept(this);
        return new OrEventFilter(label, left, right);
    }


    @Override
    public EventFilter visitEquality_math_expr(CEPLParser.Equality_math_exprContext ctx) {

        MathExprVisitor visitor = new MathExprVisitor();

        Value left = ctx.math_expr(0).accept(visitor);
        Value right = ctx.math_expr(1).accept(visitor);

        if (ctx.EQ() != null) {
            return new EqualityEventFilter(label, left, LogicalOperation.EQUALS, right);
        }
        else if (ctx.NEQ() != null) {
            return new EqualityEventFilter(label, left, LogicalOperation.NOT_EQUALS, right);
        }
        throw new Error("Unknown inequality type");
    }

    @Override
    public EventFilter visitEquality_string_expr(CEPLParser.Equality_string_exprContext ctx) {
        Attribute attribute;
        StringLiteral stringLiteral;

        CEPLParser.String_literalContext left = ctx.string_literal(0);
        CEPLParser.String_literalContext right = ctx.string_literal(1);

        if (left.attribute_name() != null){
            attribute = new Attribute(left.getText());
            stringLiteral = new StringLiteral(right.getText());
        }
        else {
            stringLiteral = new StringLiteral(left.getText());
            attribute = new Attribute(right.getText());
        }

        if (ctx.EQ() != null) {
            return new EqualityEventFilter(label, attribute, LogicalOperation.EQUALS, stringLiteral);
        }
        else if (ctx.NEQ() != null) {
            return new EqualityEventFilter(label, attribute, LogicalOperation.NOT_EQUALS, stringLiteral);
        }
        throw new Error("Unknown inequality type");
    }

    @Override
    public EventFilter visitRegex_expr(CEPLParser.Regex_exprContext ctx) {
        Attribute attribute = new Attribute(ctx.attribute_name().getText());
        StringLiteral regex = new StringLiteral(ctx.string().getText());
        return new LikeEventFilter(label, attribute, regex);
    }
}
