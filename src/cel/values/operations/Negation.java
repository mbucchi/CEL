package cel.values.operations;

import cel.values.Value;
import cel.values.ValueType;

public class Negation extends UnaryOperation {

    public Negation(Value inner) throws IncompatibleValueType {
        super(inner);
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
