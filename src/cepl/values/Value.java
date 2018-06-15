package cepl.values;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public abstract class Value {
    protected EnumSet<ValueType> valueTypes;
    protected Set<Attribute> attributes;

    public Value() {
        valueTypes = ValueType.ANY();
        attributes = new HashSet<>();
    }

    public EnumSet<ValueType> getTypes() {
        return valueTypes;
    }

    public Set<Attribute> getAttributes() {
        return attributes;
    }

    public abstract boolean equals(Object obj);

    public abstract boolean lessThan(Value otherValue);

    public abstract boolean greaterThan(Value otherValue);

    public boolean isOfType(ValueType valueType){
        return this.valueTypes.contains(valueType);
    }
}
