package cepl.filter;

import cepl.cea.utils.Label;
import cepl.values.Value;

public class InequalityEventFilter extends SimpleEventFilter {

    public InequalityEventFilter(Label label, Value lhs, LogicalOperation logicalOperation, Value rhs) {
        super(label, lhs, logicalOperation, rhs);
        if ((logicalOperation != LogicalOperation.LESS) &&
                (logicalOperation != LogicalOperation.LESS_EQUALS) &&
                (logicalOperation != LogicalOperation.GREATER) &&
                (logicalOperation != LogicalOperation.GREATER_EQUALS)){

            throw new Error("Inequality filters must be of type LESS, LESS_EQUALS, GREATER or GREATER_EQUALS");
        }
    }

    @Override
    public boolean equivalentTo(FilterComparable filter) {
        if (this == filter) return true;
        if (filter instanceof InequalityEventFilter){
            return logicalOperation.equals(((InequalityEventFilter) filter).logicalOperation) &&
                    lhs.equals(((InequalityEventFilter) filter).lhs) &&
                    rhs.equals(((InequalityEventFilter) filter).rhs);
        }
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
            switch (logicalOperation){
                case LESS:
                    switch (((InequalityEventFilter) filter).logicalOperation){
                        case LESS_EQUALS:
                            if (rhs.equals(((InequalityEventFilter) filter).rhs)) return true;
                        case EQUALS:
                            return rhs.lessThan(((InequalityEventFilter) filter).rhs);
                    }
                    return false;
                case LESS_EQUALS:
                    switch (((InequalityEventFilter) filter).logicalOperation){
                        case LESS_EQUALS:
                        case LESS:
                            return rhs.lessThan(((InequalityEventFilter) filter).rhs);
                    }
                    return false;
                case GREATER:
                    switch (((InequalityEventFilter) filter).logicalOperation){
                        case GREATER_EQUALS:
                            if (rhs.equals(((InequalityEventFilter) filter).rhs)) return true;
                        case GREATER:
                            return rhs.greaterThan(((InequalityEventFilter) filter).rhs);
                    }
                    return false;
                case GREATER_EQUALS:
                    switch (((InequalityEventFilter) filter).logicalOperation){
                        case GREATER_EQUALS:
                        case GREATER:
                            return rhs.greaterThan(((InequalityEventFilter) filter).rhs);
                    }
                    return false;
            }
        }
        if (filter instanceof EqualityEventFilter){
            if (!lhs.equals(((EqualityEventFilter) filter).lhs)){
                return false;
            }
            if (((EqualityEventFilter) filter).logicalOperation == LogicalOperation.NOT_EQUALS){
                switch (logicalOperation){
                    case LESS_EQUALS:
                        if (rhs.equals(((EqualityEventFilter) filter).rhs)) return false;
                    // fall through
                    case LESS:
                        return rhs.lessThan(((EqualityEventFilter) filter).rhs);
                    case GREATER_EQUALS:
                        if (rhs.equals(((EqualityEventFilter) filter).rhs)) return false;
                        // fall through
                    case GREATER:
                        return rhs.greaterThan(((EqualityEventFilter) filter).rhs);
                }
            }
            // no way an inequality dominates a strict equality.
            return false;
        }

        if (filter instanceof CompoundEventFilter) {
            return ((CompoundEventFilter) filter).dominatedBy(this);
        }

        // incomparable maybe ??
        return false;
    }

    @Override
    public EventFilter negate() {
        return new InequalityEventFilter(label, lhs, logicalOperation.negate(), rhs);
    }
}
