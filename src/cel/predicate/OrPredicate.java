package cel.predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OrPredicate extends CompoundPredicate {

    private OrPredicate(OrPredicate toCopy) {
        super(toCopy);
    }

    public OrPredicate(Collection<Predicate> predicates) {
        super(new ArrayList<>(predicates));
    }

    @Override
    public Predicate copy() {
        return new OrPredicate(this);
    }

    @Override
    protected boolean implies(Predicate other) {
        // TODO
        return this.equals(other);
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
        Collection<Predicate> flattened = predicates.stream()
                // first flatten inside predicates
                .map(Predicate::flatten)
                // then flatten predicates that are of type OR
                .flatMap(predicate -> {
                    if (predicate instanceof OrPredicate){
                        return ((OrPredicate)predicate).predicates.stream();
                    }
                    return Stream.of(predicate);
                })
                // remove all false predicates
                .filter(predicate -> predicate != getFalsePredicate())
                .collect(Collectors.toList());

        if (flattened.size() == 0){
            return getFalsePredicate();
        }

        if (flattened.contains(getTruePredicate())){
            return getTruePredicate();
        }

        return new OrPredicate(flattened);
    }

    @Override
    public Predicate negate() {
        Collection<Predicate> negatedPredicates = predicates.stream().map(Predicate::negate).collect(Collectors.toList());
        return new AndPredicate(negatedPredicates);
    }

    @Override
    public boolean isSatisfiable() {
        return predicates.stream().anyMatch(Predicate::isSatisfiable);
    }

    @Override
    public boolean inconsistentWith(Predicate other) {
        return predicates.stream().allMatch(other::inconsistentWith);
    }

    @Override
    boolean consistentWith(Collection<Predicate> predicates) {
        if (!isSatisfiable()) return false;
        return this.predicates.stream().anyMatch(p -> p.consistentWith(predicates));
    }

    @Override
    String innerToString() {
        StringBuilder stringBuilder = new StringBuilder("[");
        int n = 0;

        for ( Predicate p : predicates ) {
            if (n > 0) stringBuilder.append(" OR ");
            stringBuilder.append(p.innerToString());
            n += 1;
        }
        return stringBuilder.append("]").toString();
    }
}
