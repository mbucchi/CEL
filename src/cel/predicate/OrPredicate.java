package cel.predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class OrPredicate extends AtomicPredicate {
    private Collection<AtomicPredicate> predicates;

    private OrPredicate(){
        predicates = new ArrayList<>();
    }

    public OrPredicate(AtomicPredicate left, AtomicPredicate right) {
        this();
        predicates.add(left);
        predicates.add(right);
    }

    public OrPredicate(Collection<AtomicPredicate> predicates) {
        this();
        this.predicates.addAll(predicates);
    }

    @Override
    public AtomicPredicate negate() {
        Collection<AtomicPredicate> negated = predicates.stream().map(AtomicPredicate::negate).collect(Collectors.toList());
        return new AndPredicate(negated);
    }

    @Override
    public boolean isConstant() {
        // TODO: this check should be better. It is also constant if any one is constant and always true
        return predicates.stream().allMatch(AtomicPredicate::isConstant);
    }

    @Override
    public String toString() {
        return String.join(" or ", predicates.stream()
                .map(predicate -> "(" + predicate.toString() + ")")
                .collect(Collectors.toList()));
    }
}
