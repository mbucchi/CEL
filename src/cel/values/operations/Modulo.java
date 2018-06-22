package cel.values.operations;

import cel.values.Value;
import cel.values.ValueType;

public class Modulo extends BinaryOperation {

    public Modulo(Value lhs, Value rhs) throws IncompatibleValueType {
        super(lhs, rhs);
        // modulo is only valid over numeric types
        if (!interoperableWith(ValueType.NUMERIC)){
            throw new IncompatibleValueType();
        }
    }

    @Override
    public String toString() {
        return "(" + lhs.toString() + " % " + rhs.toString() + ")";
    }
}
