package cel.filter;

import cel.event.Label;
import cel.values.Value;

public class InequalityEventFilter extends AtomicEventFilter {

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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof InequalityEventFilter)) return false;
        return label.equals(((InequalityEventFilter) obj).label) &&
                lhs.equals(((InequalityEventFilter) obj).lhs) &&
                rhs.equals(((InequalityEventFilter) obj).rhs) &&
                logicalOperation.equals(((InequalityEventFilter) obj).logicalOperation);
    }

    @Override
    public boolean equivalentTo(FilterComparable filter) {
        if (this == filter) return true;
        if (this.equals(filter)) return true;
        if (filter instanceof CompoundEventFilter) return filter.equivalentTo(this);
        if (filter instanceof AtomicEventFilter) {
            AtomicEventFilter f = (AtomicEventFilter) filter;
            if (this.logicalOperation.equals(LogicalOperation.LESS)) {
                if (f.logicalOperation.equals(LogicalOperation.GREATER)) {
                    return label.equals(f.label) && lhs.equals(f.rhs) && rhs.equals(f.lhs);
                }
            } else if (this.logicalOperation.equals(LogicalOperation.LESS_EQUALS)) {
                if (f.logicalOperation.equals(LogicalOperation.GREATER_EQUALS)) {
                    return label.equals(f.label) && lhs.equals(f.rhs) && rhs.equals(f.lhs);
                }
            } else if (this.logicalOperation.equals(LogicalOperation.GREATER)) {
                if (f.logicalOperation.equals(LogicalOperation.LESS)) {
                    return label.equals(f.label) && lhs.equals(f.rhs) && rhs.equals(f.lhs);
                }
            } else if (this.logicalOperation.equals(LogicalOperation.GREATER_EQUALS)) {
                if (f.logicalOperation.equals(LogicalOperation.LESS_EQUALS)) {
                    return label.equals(f.label) && lhs.equals(f.rhs) && rhs.equals(f.lhs);
                }
            }
        }
        /* TODO: COMPARE TO OREVENTFILTERS/ANDEVENTFILTERS */
        return false;
    }

    @Override
    public boolean dominates(FilterComparable filter) {
        if (equivalentTo(filter)) return true;
        if (filter instanceof CompoundEventFilter) {
            return ((CompoundEventFilter) filter).dominatedBy(this);
        }
        // TODO: compare other kinds of filters
        return false;
    }

    @Override
    public EventFilter negate() {
        return new InequalityEventFilter(label, lhs, logicalOperation.negate(), rhs);
    }
}
