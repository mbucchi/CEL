package cel.filter;

import cel.event.Label;
import cel.values.Attribute;
import cel.values.StringLiteral;


// TODO: not yet implemented
public class LikeEventFilter extends AtomicEventFilter {

    private Attribute attribute;
    private StringLiteral value;

    public LikeEventFilter(Label label, Attribute attribute, StringLiteral value) {
        super(label, attribute, LogicalOperation.IN, value);
        this.attribute = attribute;
        this.value = value;
        throw new Error("Not yet implemented");
    }

    @Override
    public boolean equivalentTo(FilterComparable filter) {
        return this.equals(filter);
        // TODO: compare other kinds of filters
    }

    @Override
    public boolean dominates(FilterComparable filter) {
        return equivalentTo(filter);
        // TODO: compare other kinds of filters
    }

    @Override
    public EventFilter negate() {
        LikeEventFilter negated = new LikeEventFilter(label, attribute, value);
        negated.logicalOperation = LogicalOperation.NOT_IN;
        return negated;
    }
}
