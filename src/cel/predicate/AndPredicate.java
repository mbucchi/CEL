package cel.predicate;


import cel.event.EventSchema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class AndPredicate extends AtomicPredicate {

    private Collection<AtomicPredicate> predicates;

    private AndPredicate(){
        predicates = new ArrayList<>();
    }

    public AndPredicate(AtomicPredicate left, AtomicPredicate right) {
        this();
        predicates.add(left);
        predicates.add(right);
    }

    public AndPredicate(Collection<AtomicPredicate> predicates) {
        this();
        this.predicates.addAll(predicates);
    }

    @Override
    public AtomicPredicate negate() {
        Collection<AtomicPredicate> negated = predicates.stream().map(AtomicPredicate::negate).collect(Collectors.toList());
        return new OrPredicate(negated);
    }

    @Override
    public boolean isConstant() {
        return predicates.stream().allMatch(AtomicPredicate::isConstant);
    }

    @Override
    public String toString() {
        return String.join(" and ", predicates.stream()
                .map(predicate -> "(" + predicate.toString() + ")")
                .collect(Collectors.toList()));    }

}
