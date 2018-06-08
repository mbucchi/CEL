package cepl.values;

import java.util.EnumSet;

public class Attribute extends Literal {

    private String name;

    public Attribute(String name, ValueType valueType) {
        this.name = name;
        this.valueTypes = EnumSet.of(valueType);
    }

    public Attribute(String name, EnumSet<ValueType> valueType) {
        this.name = name;
        this.valueTypes = valueType;
    }

    public Attribute(String name) {
        this.name = name;
        valueTypes = ValueType.ANY();
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Attribute)) return false;
        return name.equals(((Attribute) obj).name);
    }

    @Override
    public boolean lessThan(Value otherValue) {
        return false;
    }

    @Override
    public boolean greaterThan(Value otherValue) {
        return false;
    }
}
