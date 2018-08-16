package cel.compiler.visitors;

import cel.compiler.errors.NameError;
import cel.compiler.errors.UnknownStatementError;
import cel.compiler.errors.ValueError;
import cel.event.Label;
import cel.parser.CELBaseVisitor;
import cel.parser.CELParser;
import cel.parser.utils.StringCleaner;
import cel.values.Attribute;
import cel.values.NumberLiteral;
import cel.values.Value;
import cel.values.ValueType;
import cel.values.operations.*;
import org.antlr.v4.runtime.ParserRuleContext;

import java.util.Set;

class MathExprVisitor extends CELBaseVisitor<Value> {

    private Label label;

    MathExprVisitor(Label label) {
        this.label = label;
    }

    private void ensureTypes(Value value, ParserRuleContext context) {
        if (!value.interoperableWith(ValueType.NUMERIC)) {
            throw new ValueError("Can only perform math operations over numeric values", context);
        }
        ensureValidity(value, context);
    }

    private void ensureValidity(Value value, ParserRuleContext context) {
        for (Attribute attribute : value.getAttributes()) {
            Set<String> attributeNames = label.getAttributes().keySet();
            if (!attributeNames.contains(attribute.getName())) {
                throw new NameError("Attribute `" + attribute.getName() +
                        "` is undefined for label " + label.getName(), context);
            }
        }
    }

    @Override
    public Value visitMul_math_expr(CELParser.Mul_math_exprContext ctx) {
        Value leftValue = ctx.math_expr(0).accept(this);
        if (leftValue == null) return null;
        ensureTypes(leftValue, ctx.math_expr(0));

        Value rightValue = ctx.math_expr(1).accept(this);
        if (rightValue == null) return null;
        ensureTypes(rightValue, ctx.math_expr(1));

        try {
            if (ctx.STAR() != null) {  // multiplication
                return new Multiplication(leftValue, rightValue);
            } else if (ctx.SLASH() != null) {  // division
                return new Division(leftValue, rightValue);
            } else if (ctx.PERCENT() != null) {  // modulo
                return new Modulo(leftValue, rightValue);
            }
            throw new UnknownStatementError("Unknown math operation", ctx);
        } catch (IncompatibleValueType err) {
            return null;
        }
    }


    @Override
    public Value visitSum_math_expr(CELParser.Sum_math_exprContext ctx) {
        Value leftValue = ctx.math_expr(0).accept(this);
        if (leftValue == null) return null;
        ensureTypes(leftValue, ctx.math_expr(0));

        Value rightValue = ctx.math_expr(1).accept(this);
        if (rightValue == null) return null;
        ensureTypes(rightValue, ctx.math_expr(1));

        try {
            if (ctx.MINUS() != null) {  // multiplication
                return new Subtraction(leftValue, rightValue);
            } else if (ctx.PLUS() != null) {  // division
                return new Addition(leftValue, rightValue);
            }
            throw new UnknownStatementError("Unknown math operation", ctx);
        } catch (IncompatibleValueType err) {
            return null;
        }
    }


    @Override
    public Value visitUnary_math_expr(CELParser.Unary_math_exprContext ctx) {
        Value value = ctx.math_expr().accept(this);
        if (value == null) return null;
        ensureTypes(value, ctx.math_expr());

        try {
            if (ctx.MINUS() != null) {
                return new Negation(value);
            }
        } catch (IncompatibleValueType err) {
            return null;
        }
        return value;
    }


    @Override
    public Value visitAttribute_math_expr(CELParser.Attribute_math_exprContext ctx) {
        String attributeName = StringCleaner.tryRemoveQuotes(ctx.attribute_name().getText());
        if (!label.getAttributes().containsKey(attributeName)) {
            throw new NameError("Label " + label.getName() + " has no attribute of name `" +
                    attributeName + "`", ctx);
        }
        Value value = new Attribute(attributeName, label);
        ensureValidity(value, ctx);
        return value;
    }


    @Override
    public Value visitPar_math_expr(CELParser.Par_math_exprContext ctx) {

        // Just ignore the parenthesis

        return ctx.math_expr().accept(this);
    }

    @Override
    public Value visitNumber_math_expr(CELParser.Number_math_exprContext ctx) {
        return new NumberLiteral(ctx.number().getText());
    }
}
