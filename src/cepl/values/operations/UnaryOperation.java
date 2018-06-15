package cepl.values.operations;

import cepl.values.Value;

abstract class UnaryOperation extends Operation {

    Value inner;

    UnaryOperation(Value inner){
        super(inner);
        this.inner = inner;
    }
}
