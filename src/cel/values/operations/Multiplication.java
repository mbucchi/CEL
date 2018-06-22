package cel.values.operations;

import cel.values.Value;
import cel.values.ValueType;

public class Multiplication extends BinaryOperation {

    public Multiplication(Value lhs, Value rhs) throws IncompatibleValueType {
        super(lhs, rhs);
        // multiplication is only valid over numeric types
        if (!interoperableWith(ValueType.NUMERIC)){
            throw new IncompatibleValueType();
        }
    }
    @Override
    public String toString() {
        return "("  + lhs.toString() + " * " + rhs.toString() + ")";
    }
}
