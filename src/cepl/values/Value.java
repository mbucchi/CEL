package cepl.values;

import java.util.EnumSet;

public abstract class Value {
    protected EnumSet<ValueType> valueTypes;

    public EnumSet<ValueType> getTypes() {
        return valueTypes;
    }

    public abstract boolean equals(Object obj);

    public abstract boolean lessThan(Value otherValue);

    public abstract boolean greaterThan(Value otherValue);

    public boolean isOfType(ValueType valueType){
        return this.valueTypes.contains(valueType);
    }
}
