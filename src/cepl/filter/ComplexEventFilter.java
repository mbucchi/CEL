package cepl.filter;

import cepl.event.Label;
import cepl.values.Attribute;
import cepl.values.ValueType;

public abstract class ComplexEventFilter implements PatternFilter {
    Label label;
    Attribute attribute;
    ValueType valueType;
    LogicalOperation logicalOperation;

    ComplexEventFilter(Label label, Attribute attribute, ValueType valueType, LogicalOperation logicalOperation){
        this.label = label;
        this.attribute = attribute;
        this.valueType = valueType;
        this.logicalOperation = logicalOperation;
    }

    public abstract EventFilter translateToEventFilter();
}
