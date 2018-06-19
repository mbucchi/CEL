package cel.filter;

import cel.event.Label;

import java.util.ArrayList;
import java.util.Collection;

abstract class CompoundEventFilter extends EventFilter {

    Collection<EventFilter> eventFilterCollection;

    CompoundEventFilter(Label label) {
        super(label);
        eventFilterCollection = new ArrayList<>();
    }

    abstract boolean dominatedBy(FilterComparable filter);

    abstract void addEventFilter(EventFilter eventFilter);
}
