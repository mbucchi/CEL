package cel.values;

public class Attribute extends Literal {

    private String name;

    public Attribute(String name) {
        super();
        this.name = name;
        attributes.add(this);
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
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean lessThan(Value otherValue) {
        return false;
    }

    @Override
    public boolean greaterThan(Value otherValue) {
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
