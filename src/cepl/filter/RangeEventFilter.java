package cepl.filter;

import cepl.cea.CEA;
import cepl.cea.utils.Label;
import cepl.values.Attribute;
import cepl.values.ValueType;


// TODO : not implemented yet

public class RangeEventFilter extends ComplexEventFilter {
    RangeEventFilter(Label label, Attribute attribute, ValueType valueType, LogicalOperation logicalOperation) {
        super(label, attribute, valueType, logicalOperation);
        throw new Error("Not yet implemented");
    }

    @Override
    public EventFilter translateToEventFilter() {
        return null;
    }

    @Override
    public CEA applyToCEA(CEA cea) {
        return null;
    }
}
