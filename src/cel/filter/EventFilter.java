package cel.filter;

import cel.cea.CEA;
import cel.cea.transition.Transition;
import cel.event.Label;
import cel.values.Attribute;
import cel.values.ValueType;

import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;

public abstract class EventFilter implements PatternFilter, FilterComparable {

    protected Label label;
    protected EnumSet<ValueType> valueTypes;
    protected Collection<Attribute> attributes;

    public EnumSet<ValueType> getValueTypes() {
        return valueTypes;
    }

    public Collection<Attribute> getAttributes() {
        return attributes;
    }

    EventFilter(Label label) {
        this.label = label;
        valueTypes = ValueType.ANY();
        attributes = new HashSet<>();
    }

    @Override
    public CEA applyToCEA(CEA cea) {
        for (Transition transition : cea.getTransitions()) {
            if (transition.overLabel(label) && transition.isBlack()) {
                applyToTransition(transition);
            }
        }
        return cea;
    }

    protected void applyToTransition(Transition transition) {
//        for (EventFilter eventFilter : transition.getFilters()){
//            // no need to add if there is a filter that already states this condition
//            if (eventFilter.dominates(this)) return;
//        }
        transition.addFilter(this);
    }

    public abstract EventFilter negate();

    public abstract boolean equals(Object obj);

    public abstract boolean notApplicable();

    public abstract boolean isConstant();

    public Label getLabel() {
        return label;
    }
}
