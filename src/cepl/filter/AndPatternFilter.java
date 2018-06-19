package cepl.filter;

import cepl.cea.CEA;
import cepl.event.Label;

public class AndPatternFilter implements PatternFilter {

    private PatternFilter left;
    private PatternFilter right;

    public AndPatternFilter(PatternFilter left, PatternFilter right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public CEA applyToCEA(CEA cea) {
        // In case they are over the same label, they can be pushed down together
        if (left instanceof EventFilter && right instanceof EventFilter){
            if (((EventFilter) left).label.equals(((EventFilter) right).label)){
                Label label = ((EventFilter) left).label;
                AndEventFilter newAndFilter = new AndEventFilter(label, (EventFilter) left, (EventFilter) right);
                return newAndFilter.applyToCEA(cea);
            }
        }
        // implement both filters separately over the same cea
        return right.applyToCEA(left.applyToCEA(cea));
    }
}
