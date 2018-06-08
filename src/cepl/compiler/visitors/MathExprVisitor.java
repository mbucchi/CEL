package cepl.compiler.visitors;

import cepl.values.Attribute;
import cepl.values.NumberLiteral;
import cepl.values.Value;
import cepl.parser.CEPLBaseVisitor;
import cepl.parser.CEPLParser;
import cepl.values.ValueType;
import cepl.values.operations.*;

class MathExprVisitor extends CEPLBaseVisitor<Value> {

    private void ensureTypes(Value value){
        if (!value.isOfType(ValueType.NUMERIC)){
            throw new Error("ValueError: Can only perform math operations over numeric values");
        }
    }

    @Override
    public Value visitMul_math_expr(CEPLParser.Mul_math_exprContext ctx) {
        Value leftValue = ctx.math_expr(0).accept(this);
        if (leftValue == null) return null;
        ensureTypes(leftValue);

        Value rightValue = ctx.math_expr(1).accept(this);
        if (rightValue == null) return null;
        ensureTypes(rightValue);

        try {
            if (ctx.STAR() != null){  // multiplication
                return new Multiplication(leftValue, rightValue);
            }
            else if (ctx.SLASH() != null){  // division
                return new Division(leftValue, rightValue);
            }
            else if (ctx.PERCENT() != null){  // modulo
                return new Modulo(leftValue, rightValue);
            }
            throw new Error("Unknown operation");
        }
        catch (IncompatibleValueType err) {
            return null;
        }
    }


    @Override
    public Value visitSum_math_expr(CEPLParser.Sum_math_exprContext ctx) {
        Value leftValue = ctx.math_expr(0).accept(this);
        if (leftValue == null) return null;
        ensureTypes(leftValue);

        Value rightValue = ctx.math_expr(1).accept(this);
        if (rightValue == null) return null;
        ensureTypes(rightValue);

        try {
            if (ctx.MINUS() != null){  // multiplication
                return new Subtraction(leftValue, rightValue);
            }
            else if (ctx.PLUS() != null){  // division
                return new Addition(leftValue, rightValue);
            }
            throw new Error("Unknown operation");
        }
        catch (IncompatibleValueType err) {
            return null;
        }
    }


    @Override
    public Value visitUnary_math_expr(CEPLParser.Unary_math_exprContext ctx) {
        Value value = ctx.math_expr().accept(this);
        if (value == null) return null;
        ensureTypes(value);

        try {
            if (ctx.MINUS() != null) {
                return new Negation(value);
            }
        }
        catch (IncompatibleValueType err) {
            return null;
        }
        return value;
    }


    @Override
    public Value visitAttribute_math_expr(CEPLParser.Attribute_math_exprContext ctx) {
        return new Attribute(ctx.attribute_name().getText());
    }


    @Override
    public Value visitPar_math_expr(CEPLParser.Par_math_exprContext ctx) {

        // Just ignore the parenthesis

        return ctx.math_expr().accept(this);
    }

    @Override
    public Value visitNumber_math_expr(CEPLParser.Number_math_exprContext ctx) {
        return new NumberLiteral(ctx.number().getText());
    }
}
