package cepl.values.operations;

import cepl.values.Value;
import cepl.values.ValueType;

public class Addition extends BinaryOperation {

    public Addition(Value lhs, Value rhs) throws IncompatibleValueType {
        super(lhs, rhs);
        // additions is compatible with strings and numbers
        if (!valueTypes.contains(ValueType.NUMERIC) && !valueTypes.contains(ValueType.STRING)){
            throw new IncompatibleValueType();
        }
    }

    @Override
    public String toString() {
        return "("  + lhs.toString() + " + " + rhs.toString() + ")";
    }
}
