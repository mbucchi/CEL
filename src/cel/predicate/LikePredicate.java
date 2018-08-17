package cel.predicate;

import cel.values.Attribute;
import cel.values.StringLiteral;

public class LikePredicate extends AtomicPredicate {

    private LogicalOperation logicalOperation;
    private Attribute attribute;
    private StringLiteral stringLiteral;

    private LikePredicate(Attribute attribute, LogicalOperation logicalOperation, StringLiteral stringLiteral) {
        this.attribute = attribute;
        this.logicalOperation = logicalOperation;
        this.stringLiteral = stringLiteral;
    }

    public LikePredicate(Attribute attribute, StringLiteral stringLiteral){
        this(attribute, LogicalOperation.LIKE, stringLiteral);
    }

    @Override
    public AtomicPredicate negate() {
        return new LikePredicate(attribute, logicalOperation.negate(), stringLiteral);
    }

    @Override
    public boolean isConstant() {
        return false;
    }

    @Override
    public String toString() {
        return attribute.getName() + " LIKE " + stringLiteral.toString();
    }
}
