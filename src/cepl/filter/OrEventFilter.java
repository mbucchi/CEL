package cepl.filter;

import cepl.cea.utils.Transition;
import cepl.event.Label;

import java.util.ArrayList;
import java.util.Collection;

public class OrEventFilter extends CompoundEventFilter {
    
    public OrEventFilter(Label label, EventFilter left, EventFilter right){
        super(label);
        addEventFilter(left);
        addEventFilter(right);
    }

    OrEventFilter(Label label, Collection<EventFilter> eventFilters){
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

        return new AndEventFilter(label, negatedInnerFilters);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof OrEventFilter)) return false;
        for (EventFilter thisFilter : eventFilterCollection) {
            boolean anyEqual = false;
            for (EventFilter otherFilter : ((OrEventFilter) obj).eventFilterCollection) {
                if (thisFilter.equals(otherFilter)){
                    anyEqual = true;
                    break;
                }
            }
            if (!anyEqual) return false;
        }
        return true;
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
            // new event may add value types, we need to update value types
            valueTypes.addAll(eventFilter.valueTypes);
            attributes.addAll(eventFilter.attributes);
        }
    }

    @Override
    public boolean equivalentTo(FilterComparable filter) {
        if (this == filter) return true;
        for (EventFilter ef : eventFilterCollection){
            if (!ef.equivalentTo(filter)) return false;
        }
        // TODO: not yet sure how to check this
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean filtersAdded = false;
        for (EventFilter eventFilter : eventFilterCollection) {
            if (filtersAdded) stringBuilder.append(" or ");
            stringBuilder
                    .append("(")
                    .append(eventFilter.toString())
                    .append(")");
            filtersAdded = true;
        }
        return stringBuilder.toString();
    }
}
