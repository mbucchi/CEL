package cel.values.operations;

import cel.values.Value;

public abstract class UnaryOperation extends Operation {

    Value inner;

    UnaryOperation(Value inner) {
        super(inner);
        this.inner = inner;
    }

    public Value getInner() {
        return inner;
    }
}
