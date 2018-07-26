package cel.values;

import cel.parser.utils.StringCleaner;

public class StringLiteral extends Literal {

    private String value;

    public StringLiteral(String value) {
        super(ValueType.STRING);
        if (StringCleaner.hasQuotes(value)) {
            value = StringCleaner.removeQuotes(value);
        }
        this.value = value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof StringLiteral)) return false;
        return ((StringLiteral) obj).value.equals(value);
    }

    @Override
    public boolean lessThan(Value otherValue) {
        if (this == otherValue) return false;
        if (!(otherValue instanceof StringLiteral)) return false;
        return value.compareTo(((StringLiteral) otherValue).value) < 0;
    }

    @Override
    public boolean greaterThan(Value otherValue) {
        if (this == otherValue) return false;
        if (!(otherValue instanceof StringLiteral)) return false;
        return value.compareTo(((StringLiteral) otherValue).value) > 0;
    }

    @Override
    public String toString() {
        return "\"" + value + "\"";
    }
}
