package cel.predicate;

import cel.event.EventSchema;
import cel.event.Label;
import cel.stream.StreamSchema;

import java.util.Collection;
import java.util.Set;

class FalsePredicate extends Predicate {

    static final FalsePredicate instance = new FalsePredicate(EventSchema.ANY(), StreamSchema.ANY());

    private FalsePredicate(EventSchema eventSchema, StreamSchema streamSchema){
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
        return getTruePredicate();
    }

    @Override
    public boolean isSatisfiable() {
        return false;
    }

    @Override
    public boolean inconsistentWith(Predicate other) {
        return true;
    }

    @Override
    boolean consistentWith(Collection<Predicate> predicates) {
        return false;
    }

    @Override
    public boolean overLabel(Label label) {
        return true;
    }

    @Override
    public String innerToString() {
        return "FALSE";
    }
}