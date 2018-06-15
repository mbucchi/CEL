package cepl.values;

public abstract class Literal extends Value {
    Literal(ValueType valueType) {
        super();
        valueTypes.add(valueType);
    }

    Literal(){
        super();
    }
}
