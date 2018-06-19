package cel.values.operations;

import cel.values.Value;
import cel.values.ValueType;

public class Subtraction extends BinaryOperation {

    public Subtraction(Value lhs, Value rhs) throws IncompatibleValueType {
        super(lhs, rhs);
        // subtraction is only valid over numeric types
        if (!valueTypes.contains(ValueType.NUMERIC)){
            throw new IncompatibleValueType();
        }
    }

    @Override
    public String toString() {
        return "("  + lhs.toString() + " - " + rhs.toString() + ")";
    }
}
