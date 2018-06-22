package cel.values;

import java.util.EnumSet;

public abstract class Literal extends Value {
    Literal(ValueType valueType) {
        super(valueType);
    }

    Literal(EnumSet<ValueType> valueTypes){
        super(valueTypes);
    }
}
