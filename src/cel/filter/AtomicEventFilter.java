package cel.filter;

import cel.event.Label;
import cel.values.Value;
import cel.values.ValueType;

import java.util.EnumSet;

abstract class AtomicEventFilter extends EventFilter {
    LogicalOperation logicalOperation;
    Value lhs, rhs;

    AtomicEventFilter(Label label, Value lhs, LogicalOperation logicalOperation, Value rhs) {
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
        if (!(obj instanceof AtomicEventFilter)) return false;

        // operation and both sides must be the same
        if (!logicalOperation.equals(((AtomicEventFilter) obj).logicalOperation)) return false;
        if (!lhs.equals(((AtomicEventFilter) obj).rhs)) return false;
        if (!rhs.equals(((AtomicEventFilter) obj).rhs)) return false;

        return true;
    }

    @Override
    public boolean notApplicable() {
        for (ValueType leftValueType : lhs.getTypes()) {
            for (ValueType rightValueType: rhs.getTypes()) {
                if (leftValueType.interoperableWith(rightValueType)) return false;
            }
        }
        return true;
    }

    @Override
    public boolean isConstant() {
        return attributes.size() == 0;
    }
}
