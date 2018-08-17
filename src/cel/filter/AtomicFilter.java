package cel.filter;

import cel.cea.CEA;
import cel.event.Label;
import cel.predicate.BitPredicateFactory;
import cel.predicate.Predicate;

public class AtomicFilter extends Filter {

    private Label label;
    private Predicate predicate;

    public AtomicFilter(Label label, Predicate predicate){
        this.label = label;
        this.predicate = predicate;
    }

    public Label getLabel() {
        return label;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    @Override
    public CEA applyToCEA(CEA cea) {
        return cea.addPredicate(BitPredicateFactory.getInstance().from(predicate), label);
    }
}
