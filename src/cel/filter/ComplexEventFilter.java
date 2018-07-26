package cel.filter;

import cel.event.Label;
import cel.values.Attribute;
import cel.values.ValueType;

public abstract class ComplexEventFilter implements PatternFilter {
    Label label;
    Attribute attribute;
    ValueType valueType;
    LogicalOperation logicalOperation;

    ComplexEventFilter(Label label, Attribute attribute, ValueType valueType, LogicalOperation logicalOperation) {
        this.label = label;
        this.attribute = attribute;
        this.valueType = valueType;
        this.logicalOperation = logicalOperation;
    }

    public abstract EventFilter translateToEventFilter();
}
