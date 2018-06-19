package cel.filter;

import cel.cea.CEA;
import cel.event.Label;
import cel.values.Attribute;
import cel.values.ValueType;


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
