package cel.compiler.visitors;

import cel.compiler.errors.TypeError;
import cel.compiler.errors.UnknownStatementError;
import cel.compiler.errors.ValueError;
import cel.event.Label;
import cel.filter.*;
import cel.parser.CELBaseVisitor;
import cel.parser.CELParser;
import cel.parser.utils.StringCleaner;
import cel.values.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Collection;
import java.util.stream.Collectors;

class BoolExprVisitor extends CELBaseVisitor<EventFilter> {

    private Label label;

    BoolExprVisitor(Label label) {
        this.label = label;
    }

    private void ensureValidity(EventFilter eventFilter, ParserRuleContext context) {
        if (eventFilter.isConstant()) {
            throw new ValueError("Expression must have at least one attribute", context);
        }

        if (eventFilter.notApplicable()) {
            throw new TypeError("Filter is always false", context);
        }
    }

    @Override
    public EventFilter visitNot_expr(CELParser.Not_exprContext ctx) {
        EventFilter innerFilter = ctx.bool_expr().accept(this);
        return innerFilter.negate();
    }


    @Override
    public EventFilter visitAnd_expr(CELParser.And_exprContext ctx) {
        EventFilter left = ctx.bool_expr(0).accept(this);
        EventFilter right = ctx.bool_expr(1).accept(this);
        return new AndEventFilter(label, left, right);
    }

    @Override
    public EventFilter visitPar_bool_expr(CELParser.Par_bool_exprContext ctx) {
        // just ignore parenthesis
        return ctx.bool_expr().accept(this);
    }


    private Collection<Literal> parseNumberSeq(CELParser.Number_seqContext ctx) {
        if (ctx instanceof CELParser.Number_listContext) {
            // parse all numbers as number constants
            return ((CELParser.Number_listContext) ctx).number()
                    .stream()
                    .map(numberContext -> (Literal) new NumberLiteral(numberContext.getText()))
                    .collect(Collectors.toList());
        }
        // TODO: range containment filters
//        else if (ctx instanceof CELParser.Number_rangeContext){
//
//        }
        throw new UnknownStatementError("This type of number sequence has not been implemented yet", ctx);
    }

    private Collection<Literal> parseStringSeq(CELParser.String_seqContext ctx) {
        // parse all numbers as number constants
        return ctx.string()
                .stream()
                .map(stringContext -> (Literal) new StringLiteral(stringContext.getText()))
                .collect(Collectors.toList());
    }

    @Override
    public EventFilter visitContainment_expr(CELParser.Containment_exprContext ctx) {
        LogicalOperation logicalOperation = ctx.K_NOT() == null ? LogicalOperation.IN : LogicalOperation.NOT_IN;
        Attribute attribute = getAttributeForName(ctx.attribute_name());

        EventFilter eventFilter;

        if (ctx.value_seq().number_seq() != null) {

            if (!attribute.getTypes().contains(ValueType.NUMERIC)) {
                throw new TypeError("Attribute `" + attribute.getName() +
                        "` is not comparable with numeric values", ctx);
            }

            ContainmentEventFilter filter = new ContainmentEventFilter(
                    label,
                    attribute,
                    ValueType.NUMERIC,
                    logicalOperation,
                    parseNumberSeq(ctx.value_seq().number_seq()));

            eventFilter = filter.translateToEventFilter();
        } else if (ctx.value_seq().string_seq() != null) {
            if (!attribute.getTypes().contains(ValueType.STRING)) {
                throw new TypeError("Attribute `" + attribute.getName() +
                        "` is not comparable with string values", ctx);
            }
            ContainmentEventFilter filter = new ContainmentEventFilter(
                    label,
                    attribute,
                    ValueType.STRING,
                    logicalOperation,
                    parseStringSeq(ctx.value_seq().string_seq()));

            eventFilter = filter.translateToEventFilter();
        } else {
            throw new UnknownStatementError("Unknown sequence type", ctx);
        }

        ensureValidity(eventFilter, ctx);
        return eventFilter;
    }


    @Override
    public EventFilter visitInequality_expr(CELParser.Inequality_exprContext ctx) {
        MathExprVisitor mathExprVisitor = new MathExprVisitor(label);

        Value lhs = ctx.math_expr(0).accept(mathExprVisitor);
        Value rhs = ctx.math_expr(1).accept(mathExprVisitor);
        LogicalOperation logicalOperation;

        if (ctx.GE() != null) {
            logicalOperation = LogicalOperation.GREATER;
        } else if (ctx.GEQ() != null) {
            logicalOperation = LogicalOperation.GREATER_EQUALS;
        } else if (ctx.LEQ() != null) {
            logicalOperation = LogicalOperation.LESS_EQUALS;
        } else if (ctx.LE() != null) {
            logicalOperation = LogicalOperation.LESS;
        } else {
            throw new UnknownStatementError("Unknown inequality type", ctx);
        }
        EventFilter eventFilter = new InequalityEventFilter(label, lhs, logicalOperation, rhs);
        ensureValidity(eventFilter, ctx);
        return eventFilter;
    }


    @Override
    public EventFilter visitOr_expr(CELParser.Or_exprContext ctx) {
        EventFilter left = ctx.bool_expr(0).accept(this);
        EventFilter right = ctx.bool_expr(1).accept(this);
        return new OrEventFilter(label, left, right);
    }


    @Override
    public EventFilter visitEquality_math_expr(CELParser.Equality_math_exprContext ctx) {

        MathExprVisitor visitor = new MathExprVisitor(label);

        Value left = ctx.math_expr(0).accept(visitor);
        Value right = ctx.math_expr(1).accept(visitor);
        LogicalOperation logicalOperation;

        if (ctx.EQ() != null) {
            logicalOperation = LogicalOperation.EQUALS;
        } else if (ctx.NEQ() != null) {
            logicalOperation = LogicalOperation.NOT_EQUALS;
        } else {
            throw new UnknownStatementError("Unknown inequality type", ctx);
        }
        EventFilter eventFilter = new EqualityEventFilter(label, left, logicalOperation, right);
        ;
        ensureValidity(eventFilter, ctx);
        return eventFilter;
    }

    @Override
    public EventFilter visitEquality_string_expr(CELParser.Equality_string_exprContext ctx) {
        Attribute attribute;
        StringLiteral stringLiteral;

        CELParser.String_literalContext left = ctx.string_literal(0);
        CELParser.String_literalContext right = ctx.string_literal(1);

        if (left.attribute_name() != null) {
            attribute = getAttributeForName(left.attribute_name());
            stringLiteral = new StringLiteral(right.getText());
        } else {
            stringLiteral = new StringLiteral(left.getText());
            attribute = getAttributeForName(right.attribute_name());
        }

        LogicalOperation logicalOperation;
        if (ctx.EQ() != null) {
            logicalOperation = LogicalOperation.EQUALS;
        } else if (ctx.NEQ() != null) {
            logicalOperation = LogicalOperation.NOT_EQUALS;
        } else {
            throw new UnknownStatementError("Unknown inequality type", ctx);
        }

        EventFilter eventFilter = new EqualityEventFilter(label, attribute, logicalOperation, stringLiteral);
        ;
        ensureValidity(eventFilter, ctx);
        return eventFilter;
    }

    @Override
    public EventFilter visitRegex_expr(CELParser.Regex_exprContext ctx) {
        Attribute attribute = getAttributeForName(ctx.attribute_name());
        StringLiteral regex = new StringLiteral(ctx.string().getText());
        return new LikeEventFilter(label, attribute, regex);
    }

    private Attribute getAttributeForName(CELParser.Attribute_nameContext ctx) {
        String attributeName = StringCleaner.tryRemoveQuotes(ctx.getText());
        if (!label.getAttributes().containsKey(attributeName)) {
            throw new ValueError("Attribute `" + attributeName + "` is not defined on label " + label.getName(),
                    ctx);
        }
        return new Attribute(attributeName, label);
    }
}
