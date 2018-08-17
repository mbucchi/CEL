package cel.predicate;

abstract class CompoundPredicate extends Predicate {
    public abstract AtomicPredicate toAtomicPredicate();
}
