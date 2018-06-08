package cepl.filter;

import cepl.cea.Transition;
import cepl.cea.utils.Label;

import java.util.ArrayList;
import java.util.Collection;

public class AndEventFilter extends CompoundEventFilter {

    public AndEventFilter(Label label, EventFilter left, EventFilter right){
        super(label);
        addEventFilter(left);
        addEventFilter(right);
    }

    public AndEventFilter(Label label, Collection<EventFilter> eventFilters){
        super(label);
        for (EventFilter eventFilter : eventFilters){
            addEventFilter(eventFilter);
        }
    }

    @Override
    protected void applyToTransition(Transition transition) {
        for (EventFilter eventFilter : eventFilterCollection) {
            transition.addFilter(eventFilter);
        }
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
        if (eventFilter instanceof AndEventFilter){
            for (EventFilter ev : ((AndEventFilter) eventFilter).eventFilterCollection) {
                addEventFilter(ev);
            }
        }
        else {
            ArrayList<EventFilter> newEventFilters = new ArrayList<>();
            for (EventFilter ev: eventFilterCollection){
                if (!eventFilter.dominates((ev))){
                    newEventFilters.add(ev);
                }
                if (ev.dominates(eventFilter)){
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
        // TODO: not yet sure how to check this
        return false;
    }

    @Override
    public boolean dominates(FilterComparable filter) {
        if (equivalentTo((filter))) return true;
        for (EventFilter ef : eventFilterCollection){
            if (ef.dominates(filter)) return true;
        }
        return false;
    }

    @Override
    boolean dominatedBy(FilterComparable filter) {
        for (EventFilter ef : eventFilterCollection){
            if (!filter.dominates(ef)) return false;
        }
        return true;
    }
}
