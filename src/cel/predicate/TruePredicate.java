package cel.predicate;

import cel.event.EventSchema;
import cel.event.Label;
import cel.stream.StreamSchema;

import java.util.Collection;
import java.util.Set;

class TruePredicate extends Predicate {

    static final TruePredicate instance = new TruePredicate(EventSchema.ANY(), StreamSchema.ANY());

    private TruePredicate(EventSchema eventSchema, StreamSchema streamSchema){
        super(eventSchema, streamSchema, Set.of());
    }

    @Override
    public Predicate copy() {
        return this;
    }

    @Override
    protected boolean implies(Predicate other) {
        return true;
    }

    @Override
    public boolean equals(Object obj) {
        return obj == this;
    }

    @Override
    public Predicate flatten() {
        return this;
    }

    @Override
    public Predicate negate() {
        return getFalsePredicate();
    }

    @Override
    public boolean isSatisfiable() {
        return true;
    }

    @Override
    public boolean inconsistentWith(Predicate other) {
        return !other.isSatisfiable();
    }

    @Override
    boolean consistentWith(Collection<Predicate> predicates) {
        return true;
    }

    @Override
    public boolean overLabel(Label label) {
        return true;
    }

    @Override
    public String innerToString() {
        return "TRUE";
    }
}
