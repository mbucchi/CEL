package cepl.filter;

import cepl.cea.utils.Label;
import cepl.values.Value;
import cepl.values.ValueType;

import java.util.EnumSet;

abstract class SimpleEventFilter extends EventFilter {
    LogicalOperation logicalOperation;
    Value lhs, rhs;
    private EnumSet<ValueType> valueTypes;

    SimpleEventFilter(Label label, Value lhs, LogicalOperation logicalOperation, Value rhs) {
        super(label);
        this.lhs = lhs;
        this.logicalOperation = logicalOperation;
        this.rhs = rhs;
        setType();
    }

   private void setType(){
        valueTypes = ValueType.ANY();
        valueTypes.retainAll(lhs.getTypes());
        valueTypes.retainAll(rhs.getTypes());
   }
}
