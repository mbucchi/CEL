package cepl.values.operations;

import cepl.values.Value;

abstract class BinaryOperation extends Operation {

    Value lhs, rhs;

    BinaryOperation(Value lhs, Value rhs) {
        super(lhs, rhs);
        this.lhs = lhs;
        this.rhs = rhs;
    }
}
