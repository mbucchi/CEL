package cel.predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AndPredicate extends CompoundPredicate {

    private AndPredicate(AndPredicate toCopy) {
        super(toCopy);
    }

    public AndPredicate(Collection<Predicate> predicates) {
        super(new ArrayList<>(predicates));
    }

    @Override
    public Predicate copy() {
        return new AndPredicate(this);
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
                // first flatten all inside predicates
                .map(Predicate::flatten)
                // then flatten predicates that are of type AND
                .flatMap(predicate -> {
                    if (predicate instanceof AndPredicate){
                        return ((AndPredicate)predicate).predicates.stream();
                    }
                    return Stream.of(predicate);
                })
                // Remove all tautologies
                .filter(predicate -> predicate != getTruePredicate())
                .collect(Collectors.toList());

        List<Predicate> useful = getUseful(flattened);

        if (useful.size() == 0){
            return getTruePredicate();
        }
        if (useful.contains(getFalsePredicate())){
            return getFalsePredicate();
        }
        if (useful.size() == 1){
            return useful.get(0);
        }

        Predicate predicate = new AndPredicate(useful);
        if (predicate.isSatisfiable()) {
            return predicate;
        }

        return getFalsePredicate();
    }


    @Override
    public Predicate negate() {
        Collection<Predicate> negatedPredicates = predicates.stream().map(Predicate::negate).collect(Collectors.toList());
        return new OrPredicate(negatedPredicates);
    }

    @Override
    public boolean isSatisfiable() {
        return predicates.stream().allMatch(p1 -> p1.consistentWith(predicates));
    }

    @Override
    public boolean inconsistentWith(Predicate other) {
        return predicates.stream().anyMatch(other::inconsistentWith);
    }

    @Override
    boolean consistentWith(Collection<Predicate> predicates) {
        if (!isSatisfiable()) return false;

        return this.predicates.stream().allMatch(
                p1 -> p1.consistentWith(predicates)
        );
    }

    @Override
    String innerToString() {
        StringBuilder stringBuilder = new StringBuilder("[");
        int n = 0;

        for ( Predicate p : predicates ) {
            if (n > 0) stringBuilder.append(" AND ");
            stringBuilder.append(p.innerToString());
            n += 1;
        }
        return stringBuilder.append("]").toString();
    }

}
