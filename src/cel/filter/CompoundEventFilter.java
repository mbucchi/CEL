package cel.filter;

import cel.event.Label;

import java.util.ArrayList;
import java.util.Collection;

public abstract class CompoundEventFilter extends EventFilter {

    Collection<EventFilter> eventFilterCollection;

    CompoundEventFilter(Label label) {
        super(label);
        eventFilterCollection = new ArrayList<>();
    }

    abstract boolean dominatedBy(FilterComparable filter);

    abstract void addEventFilter(EventFilter eventFilter);

    @Override
    public boolean notApplicable() {
        for (EventFilter eventFilter : eventFilterCollection) {
            if (eventFilter.notApplicable()) return true;
        }
        return false;
    }

    public Collection<EventFilter> getEventFilterCollection() {
        return eventFilterCollection;
    }

    @Override
    public boolean isConstant() {
        for (EventFilter eventFilter : eventFilterCollection) {
            if (eventFilter.isConstant()) return true;
        }
        return false;
    }
}
