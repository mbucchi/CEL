package cel.predicate;

import cel.values.Attribute;
import cel.values.Literal;
import cel.values.Value;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;

public class ContainmentPredicate extends CompoundPredicate {

    private Value value;
    private Collection<Literal> valueCollection;

    private ContainmentPredicate() {
        valueCollection = new ArrayList<>();
    }

    public ContainmentPredicate(Value value, Collection<Literal> values){
        this();
        this.value = value;
        valueCollection.addAll(values);
    }

    @Override
    public AtomicPredicate toAtomicPredicate() {
        Collection<AtomicPredicate> predicates = valueCollection.stream()
                .map(v -> new EqualityPredicate(value, LogicalOperation.EQUALS, v))
                .collect(Collectors.toList());
        return new OrPredicate(predicates);
    }
}
