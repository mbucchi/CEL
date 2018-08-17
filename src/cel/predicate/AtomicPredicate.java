package cel.predicate;

import cel.event.EventSchema;

public abstract class AtomicPredicate extends Predicate {

    public abstract AtomicPredicate negate();

    public abstract boolean isConstant();

    public abstract String toString();

//    public abstract String getCodeFormFor(EventSchema eventSchema);
}
