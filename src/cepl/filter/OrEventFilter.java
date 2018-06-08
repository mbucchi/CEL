package cepl.filter;

import cepl.cea.Transition;
import cepl.cea.utils.Label;

import java.util.ArrayList;
import java.util.Collection;

public class OrEventFilter extends CompoundEventFilter {
    
    public OrEventFilter(Label label, EventFilter left, EventFilter right){
        super(label);
        addEventFilter(left);
        addEventFilter(right);
    }

    public OrEventFilter(Label label, Collection<EventFilter> eventFilters){
        super(label);
        for (EventFilter eventFilter : eventFilters){
            addEventFilter(eventFilter);
        }
    }

    @Override
    protected void applyToTransition(Transition transition) {
        transition.addFilter(this);
    }

    @Override
    public EventFilter negate() {
        ArrayList<EventFilter> negatedInnerFilters = new ArrayList<>();

        for (EventFilter ev: eventFilterCollection) {
            negatedInnerFilters.add(ev.negate());
        }

        return new OrEventFilter(label, negatedInnerFilters);
    }

    void addEventFilter(EventFilter eventFilter){
        if (!eventFilter.label.equals(label)){
            throw new Error("Inner filters must have the same labels and attributes");
        }
        if (eventFilter instanceof OrEventFilter){
            for (EventFilter ev : ((OrEventFilter) eventFilter).eventFilterCollection) {
                addEventFilter(ev);
            }
        }
        else {
            ArrayList<EventFilter> newEventFilters = new ArrayList<>();
            for (EventFilter ev: eventFilterCollection){
                if (!ev.dominates(eventFilter)){
                    newEventFilters.add(ev);
                }
                if (eventFilter.dominates(ev)){
                    return;
                }
            }
            newEventFilters.add(eventFilter);
            eventFilterCollection = newEventFilters;
        }
    }

    @Override
    public boolean equivalentTo(FilterComparable filter) {
        if (this == filter) return true;
        for (EventFilter ef : eventFilterCollection){
            if (!ef.equivalentTo(filter)) return false;
        }
        return true;
    }

    @Override
    public boolean dominates(FilterComparable filter) {
        if (equivalentTo(filter)) return true;
        for (EventFilter ef : eventFilterCollection){
            if (!ef.dominates(filter)) return false;
        }
        return true;
    }

    @Override
    boolean dominatedBy(FilterComparable filter) {
        for (EventFilter ef : eventFilterCollection){
            if (filter.dominates(ef)) return true;
        }
        return false;
    }
}
