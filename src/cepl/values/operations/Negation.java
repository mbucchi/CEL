package cepl.values.operations;

import cepl.values.Value;
import cepl.values.ValueType;

public class Negation extends Operation {
    private Value inner;

    public Negation(Value inner) throws IncompatibleValueType {
        this.inner = inner;
        valueTypes = inner.getTypes();

        // negation is only valid over numeric types
        if (!valueTypes.contains(ValueType.NUMERIC)){
            throw new IncompatibleValueType();
        }
    }


    @Override
    public String toString() {
        return "(-" + inner.toString() + ")";
    }
}
