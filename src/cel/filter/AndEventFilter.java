package cel.filter;

import cel.cea.utils.Transition;
import cel.event.Label;

import java.util.ArrayList;
import java.util.Collection;

public class AndEventFilter extends CompoundEventFilter {

    public AndEventFilter(Label label, EventFilter left, EventFilter right){
        super(label);
        addEventFilter(left);
        addEventFilter(right);
    }

    AndEventFilter(Label label, Collection<EventFilter> eventFilters){
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof AndEventFilter)) return false;
        for (EventFilter thisFilter : eventFilterCollection) {
            boolean anyEqual = false;
            for (EventFilter otherFilter : ((AndEventFilter) obj).eventFilterCollection) {
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
            throw new Error("Inner filters must have the same labels");
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
            // new event may be more restrictive, we need to update value types
            valueTypes.retainAll(eventFilter.valueTypes);
        }
    }

    @Override
    public boolean equivalentTo(FilterComparable filter) {
        return this.equals(filter);
        // TODO: not yet sure how to check this
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        boolean filtersAdded = false;
        for (EventFilter eventFilter : eventFilterCollection) {
            if (filtersAdded) stringBuilder.append(" and ");
            stringBuilder
                    .append("(")
                    .append(eventFilter.toString())
                    .append(")");
            filtersAdded = true;
        }
        return stringBuilder.toString();
    }
}
