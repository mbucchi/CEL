package cel.predicate;

import java.util.Collection;

public class NotPredicate extends Predicate {

    private final Predicate negatedPredicate;

    private NotPredicate(NotPredicate self, Predicate inner){
        this(inner.copy());
    }

    public NotPredicate(Predicate predicate){
        super(predicate);
        negatedPredicate = predicate;
    }

    @Override
    public Predicate copy() {
        return new NotPredicate(this, negatedPredicate);
    }

    @Override
    protected boolean implies(Predicate other) {
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Predicate)) return false;
        // TODO
        return false;
    }

    @Override
    public Predicate flatten() {
        return negatedPredicate.negate().flatten();
    }

    @Override
    public Predicate negate() {
        return negatedPredicate.copy();
    }

    @Override
    public boolean isSatisfiable() {
        // negation of tautology is unsatisfiable
        return negatedPredicate != getTruePredicate();
    }

    @Override
    public boolean inconsistentWith(Predicate other) {
        return negate().flatten().inconsistentWith(other);
    }

    @Override
    boolean consistentWith(Collection<Predicate> predicates) {
        return negatedPredicate.consistentWith(predicates);
    }

    @Override
    String innerToString() {
        return "NOT " + negatedPredicate.innerToString();
    }
}
