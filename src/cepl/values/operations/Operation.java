package cepl.values.operations;
import cepl.values.Value;

public abstract class Operation extends Value {
    public abstract String toString();

    @Override
    public boolean equals(Object obj) {
        return this == obj;
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
