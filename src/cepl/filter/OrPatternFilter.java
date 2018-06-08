package cepl.filter;

import cepl.cea.CEA;
import cepl.cea.utils.Label;

public class OrPatternFilter implements PatternFilter {
    private PatternFilter left;
    private PatternFilter right;

    public OrPatternFilter(PatternFilter left, PatternFilter right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public CEA applyToCEA(CEA cea) {
        // In case they are over the same label, they can be pushed down with no need to split the CEA
//        if (left instanceof EventFilter && right instanceof EventFilter){
//            if (((EventFilter) left).label.equals(((EventFilter) right).label)){
//                Label label = ((EventFilter) left).label;
//                OrEventFilter newOrFilter = new OrEventFilter(label, (EventFilter) left, (EventFilter) right);
//                return newOrFilter.applyToCEA(cea);
//            }
//        }
        // different labels -> split CEA
        // TODO: optimize this. In most cases there is no need to duplicate the whole automata
        CEA leftCea = left.applyToCEA(cea.copy());
        CEA rightCea = right.applyToCEA(cea.copy());
        return CEA.createOrCEA(leftCea, rightCea);
    }
}
