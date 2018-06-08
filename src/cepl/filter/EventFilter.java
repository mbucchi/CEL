package cepl.filter;

import cepl.cea.CEA;
import cepl.cea.Transition;
import cepl.cea.utils.Label;

public abstract class EventFilter implements PatternFilter, FilterComparable {

    protected Label label;

    EventFilter(Label label){
        this.label = label;
    }

    @Override
    public CEA applyToCEA(CEA cea) {
        for (Transition transition : cea.getTransitions()){
            if (transition.overLabel(label)){
                applyToTransition(transition);
            }
        }
        return cea;
    }

    protected void applyToTransition(Transition transition) {
        for (EventFilter eventFilter : transition.getFilters()){
            // no need to add if there is a filter that already states this condition
            if (eventFilter.dominates(this)) return;
        }
        transition.addFilter(this);
    }

    public abstract EventFilter negate();
}
