package cepl.values;

import java.util.EnumSet;

public class NumberLiteral extends Literal {

    private double value;

    public NumberLiteral(double value){
        valueTypes = EnumSet.of(ValueType.NUMERIC);
        this.value = value;
    }

    public NumberLiteral(String number){
        this(Double.parseDouble(number));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof NumberLiteral)) return false;
        return ((NumberLiteral) obj).value == value;
    }

    @Override
    public boolean lessThan(Value otherValue) {
        if (this == otherValue) return false;
        if (!(otherValue instanceof NumberLiteral)) return false;
        return ((NumberLiteral) otherValue).value < value;
    }

    @Override
    public boolean greaterThan(Value otherValue) {
        if (this == otherValue) return false;
        if (!(otherValue instanceof NumberLiteral)) return false;
        return ((NumberLiteral) otherValue).value > value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }

    public double getValue() {
        return value;
    }
}
