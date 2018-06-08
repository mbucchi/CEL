package cepl.values.operations;

import cepl.values.Value;
import cepl.values.ValueType;

public class Addition extends Operation {
    private Value lhs, rhs;

    public Addition(Value lhs, Value rhs) throws IncompatibleValueType {
        // TODO: optimizations
        this.lhs = lhs;
        this.rhs = rhs;

        valueTypes = ValueType.ANY();
        valueTypes.retainAll(lhs.getTypes());
        valueTypes.retainAll(rhs.getTypes());

        if (valueTypes.size() == 0){
            throw new IncompatibleValueType();
        }
    }

    @Override
    public String toString() {
        return "("  + lhs.toString() + " + " + rhs.toString() + ")";
    }
}
