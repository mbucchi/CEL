package cel.values.operations;

import cel.values.Value;
import cel.values.ValueType;

public class Addition extends BinaryOperation {

    public Addition(Value lhs, Value rhs) throws IncompatibleValueType {
        super(lhs, rhs);
        // additions is compatible with strings and numbers
        if (!interoperableWith(ValueType.NUMERIC) && !interoperableWith(ValueType.STRING)){
            throw new IncompatibleValueType();
        }
    }

    @Override
    public String toString() {
        return "("  + lhs.toString() + " + " + rhs.toString() + ")";
    }
}
