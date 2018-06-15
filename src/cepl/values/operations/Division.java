package cepl.values.operations;

import cepl.values.Value;
import cepl.values.ValueType;

public class Division extends BinaryOperation {

    public Division(Value lhs, Value rhs) throws IncompatibleValueType {
        super(lhs, rhs);
        // division is only valid over numeric types
        if (!valueTypes.contains(ValueType.NUMERIC)){
            throw new IncompatibleValueType();
        }
    }

    @Override
    public String toString() {
        return "("  + lhs.toString() + " / " + rhs.toString() + ")";
    }
}
