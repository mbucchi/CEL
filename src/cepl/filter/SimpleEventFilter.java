package cepl.filter;

import cepl.event.Label;
import cepl.values.Value;

abstract class SimpleEventFilter extends EventFilter {
    LogicalOperation logicalOperation;
    Value lhs, rhs;

    SimpleEventFilter(Label label, Value lhs, LogicalOperation logicalOperation, Value rhs) {
        super(label);
        this.lhs = lhs;
        this.logicalOperation = logicalOperation;
        this.rhs = rhs;
        setTypeAndAttributes();
    }

   private void setTypeAndAttributes(){
        valueTypes.retainAll(lhs.getTypes());
        valueTypes.retainAll(rhs.getTypes());
        attributes.addAll(lhs.getAttributes());
        attributes.addAll(rhs.getAttributes());
   }

    @Override
    public String toString() {
        return lhs.toString() + " " + logicalOperation.toString() + " " + rhs.toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof SimpleEventFilter)) return false;

        // operation and both sides must be the same
        if (!logicalOperation.equals(((SimpleEventFilter) obj).logicalOperation)) return false;
        if (!lhs.equals(((SimpleEventFilter) obj).rhs)) return false;
        if (!rhs.equals(((SimpleEventFilter) obj).rhs)) return false;

        return true;
    }
}
