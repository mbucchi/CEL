package cepl.filter;

import cepl.cea.utils.Label;
import cepl.values.Value;

public class EqualityEventFilter extends SimpleEventFilter {
    public EqualityEventFilter(Label label, Value lhs, LogicalOperation logicalOperation, Value rhs) {
        super(label, lhs, logicalOperation, rhs);
        if (logicalOperation != LogicalOperation.EQUALS && logicalOperation != LogicalOperation.NOT_EQUALS){
            throw new Error("Equality filters must be of type EQUALS or NOT EQUALS");
        }
    }

    @Override
    public boolean equivalentTo(FilterComparable filter) {
        if (this == filter) return true;
        if (filter instanceof EqualityEventFilter){
            return logicalOperation.equals(((EqualityEventFilter) filter).logicalOperation) &&
                    lhs.equals(((EqualityEventFilter) filter).lhs) &&
                    rhs.equals(((EqualityEventFilter) filter).rhs);
        }
//        if (filter instanceof InequalityEventFilter) return false;

        // compound filters already have their comparisons implemented
        if (filter instanceof CompoundEventFilter) return filter.equivalentTo(this);
        return false;
    }

    @Override
    public boolean dominates(FilterComparable filter) {
        if (equivalentTo(filter)) return true;
        if (filter instanceof InequalityEventFilter) {
            if (!lhs.equals(((InequalityEventFilter) filter).lhs)){
                return false;
            }
            if (logicalOperation == LogicalOperation.EQUALS){
                switch (((InequalityEventFilter) filter).logicalOperation){
                    case LESS_EQUALS:
                        if (rhs.equals(((InequalityEventFilter) filter).rhs)) return true;
                    case LESS:
                        return rhs.lessThan(((InequalityEventFilter) filter).rhs);
                    case GREATER_EQUALS:
                        if (rhs.equals(((InequalityEventFilter) filter).rhs)) return true;
                    case GREATER:
                        return rhs.greaterThan(((InequalityEventFilter) filter).rhs);
                }
            }
            else { // NOT_EQUALS can't dominate any inequality
                return false;
            }
        }
        if (filter instanceof CompoundEventFilter) {
            return ((CompoundEventFilter) filter).dominatedBy(this);
        }

        // incomparable ?
        return false;
    }

    @Override
    public EventFilter negate() {
        return new EqualityEventFilter(label, lhs, logicalOperation.negate(), rhs);
    }
}
