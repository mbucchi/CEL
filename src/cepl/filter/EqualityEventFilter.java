package cepl.filter;

import cepl.event.Label;
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
        if (filter instanceof CompoundEventFilter) return filter.equivalentTo(this);

        // TODO: compare other kinds of filters
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
        return new EqualityEventFilter(label, lhs, logicalOperation.negate(), rhs);
    }
}
