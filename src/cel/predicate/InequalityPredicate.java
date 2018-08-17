package cel.predicate;

import cel.values.Attribute;
import cel.values.Value;

public class InequalityPredicate extends AtomicPredicate{
    private LogicalOperation logicalOperation;
    private Value left, right;

    public InequalityPredicate(Value left, LogicalOperation logicalOperation, Value right) {
        if (!logicalOperation.isInequalityOperation())
            throw new Error("Unexpected logical operation " + logicalOperation.toString());
        this.logicalOperation = logicalOperation;
        this.left = left;
        this.right = right;
    }

    @Override
    public AtomicPredicate negate() {
        return new EqualityPredicate(left, logicalOperation.negate(), right);
    }

    @Override
    public boolean isConstant() {
        return !(left instanceof Attribute || right instanceof Attribute);
    }

    @Override
    public String toString() {
        return left.toString() + " " + logicalOperation.toString() + " " + right.toString();
    }
}
