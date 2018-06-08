package cepl.values.operations;

import cepl.values.Value;
import cepl.values.ValueType;

public class Division extends Operation {
    private Value lhs, rhs;

    public Division(Value lhs, Value rhs) throws IncompatibleValueType {
        // TODO: optimizations
        this.lhs = lhs;
        this.rhs = rhs;

        valueTypes = ValueType.ANY();
        valueTypes.retainAll(lhs.getTypes());
        valueTypes.retainAll(rhs.getTypes());

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
