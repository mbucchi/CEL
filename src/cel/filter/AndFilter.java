package cel.filter;

import cel.cea.CEA;
import cel.event.Label;
import cel.predicate.BitPredicate;
import cel.predicate.BitPredicateFactory;

public class AndFilter extends Filter {

    private Filter left;
    private Filter right;

    public AndFilter(Filter left, Filter right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public CEA applyToCEA(CEA cea) {
        // In case they are over the same label, they can be pushed down together
        if (left instanceof AtomicFilter && right instanceof AtomicFilter) {
            if (((AtomicFilter) left).getLabel().equals(((AtomicFilter) right).getLabel())) {
                Label label = ((AtomicFilter) left).getLabel();

                BitPredicate leftPredicate = BitPredicateFactory.getInstance().from(((AtomicFilter) left).getPredicate());
                BitPredicate rightPredicate = BitPredicateFactory.getInstance().from(((AtomicFilter) right).getPredicate());

                BitPredicate cojoined = leftPredicate.cojoin(rightPredicate);
                return cea.addPredicate(cojoined, label);
            }
        }
        // implement both filters separately over the same cea
        return right.applyToCEA(left.applyToCEA(cea));
    }
}
