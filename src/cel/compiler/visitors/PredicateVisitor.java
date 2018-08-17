package cel.compiler.visitors;

import cel.compiler.errors.TypeError;
import cel.compiler.errors.UnknownStatementError;
import cel.compiler.errors.ValueError;
import cel.event.Label;
import cel.parser.CELBaseVisitor;
import cel.parser.CELParser;
import cel.parser.utils.StringCleaner;
import cel.predicate.*;
import cel.values.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Collection;
import java.util.stream.Collectors;

class PredicateVisitor extends CELBaseVisitor<AtomicPredicate> {

    private Label label;

    PredicateVisitor(Label label) {
        this.label = label;
    }

    private void ensureValidity(AtomicPredicate atomicPredicate, ParserRuleContext context) {
        if (atomicPredicate.isConstant()) {
            throw new ValueError("Expression must have at least one attribute", context);
        }

//        if (atomicPredicate.notApplicable()) {
//            throw new TypeError("Filter is always false", context);
//        }
    }

    @Override
    public AtomicPredicate visitNot_expr(CELParser.Not_exprContext ctx) {
        AtomicPredicate innerPredicate = ctx.bool_expr().accept(this);
        return innerPredicate.negate();
    }


    @Override
    public AtomicPredicate visitAnd_expr(CELParser.And_exprContext ctx) {
        AtomicPredicate left = ctx.bool_expr(0).accept(this);
        AtomicPredicate right = ctx.bool_expr(1).accept(this);
        return new AndPredicate(left, right);
    }

    @Override
    public AtomicPredicate visitPar_bool_expr(CELParser.Par_bool_exprContext ctx) {
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
    public AtomicPredicate visitContainment_expr(CELParser.Containment_exprContext ctx) {
        boolean negated = ctx.K_NOT() != null;
        Attribute attribute = getAttributeForName(ctx.attribute_name());

        AtomicPredicate predicate;

        if (ctx.value_seq().number_seq() != null) {

            if (!attribute.getTypes().contains(ValueType.NUMERIC)) {
                throw new TypeError("Attribute `" + attribute.getName() +
                        "` is not comparable with numeric values", ctx);
            }

            predicate = new ContainmentPredicate(
                    attribute,
                    parseNumberSeq(ctx.value_seq().number_seq()))
                    .toAtomicPredicate();

        } else if (ctx.value_seq().string_seq() != null) {
            if (!attribute.getTypes().contains(ValueType.STRING)) {
                throw new TypeError("Attribute `" + attribute.getName() +
                        "` is not comparable with string values", ctx);
            }
            predicate = new ContainmentPredicate(
                    attribute,
                    parseStringSeq(ctx.value_seq().string_seq()))
                    .toAtomicPredicate();

        } else {
            throw new UnknownStatementError("Unknown sequence type", ctx);
        }

        if (negated){
            predicate = predicate.negate();
        }

        ensureValidity(predicate, ctx);
        return predicate;
    }


    @Override
    public AtomicPredicate visitInequality_expr(CELParser.Inequality_exprContext ctx) {
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
        AtomicPredicate predicate = new InequalityPredicate(lhs, logicalOperation, rhs);
        ensureValidity(predicate, ctx);
        return predicate;
    }


    @Override
    public AtomicPredicate visitOr_expr(CELParser.Or_exprContext ctx) {
        AtomicPredicate left = ctx.bool_expr(0).accept(this);
        AtomicPredicate right = ctx.bool_expr(1).accept(this);
        return new OrPredicate(left, right);
    }


    @Override
    public AtomicPredicate visitEquality_math_expr(CELParser.Equality_math_exprContext ctx) {

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
        AtomicPredicate predicate = new EqualityPredicate(left, logicalOperation, right);
        ;
        ensureValidity(predicate, ctx);
        return predicate;
    }

    @Override
    public AtomicPredicate visitEquality_string_expr(CELParser.Equality_string_exprContext ctx) {
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

        AtomicPredicate predicate = new EqualityPredicate(attribute, logicalOperation, stringLiteral);

        ensureValidity(predicate, ctx);
        return predicate;
    }

    @Override
    public AtomicPredicate visitRegex_expr(CELParser.Regex_exprContext ctx) {
        Attribute attribute = getAttributeForName(ctx.attribute_name());
        StringLiteral regex = new StringLiteral(ctx.string().getText());
        return new LikePredicate(attribute, regex);
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
