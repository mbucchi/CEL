package cepl.cea;

import cepl.event.EventSchema;
import cepl.cea.utils.Label;
import cepl.filter.EventFilter;
import cepl.stream.StreamSchema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Predicate {
    public static final Predicate TRUE_PREDICATE = new Predicate();

    private Collection<EventFilter> filterCollection;
    private StreamSchema streamSchema;
    private EventSchema eventSchema;
    private Set<Label> labelSet;

    private Predicate() {
        filterCollection = new ArrayList<>();
        labelSet = new HashSet<>();
    }

    public Predicate(EventSchema eventSchema) {
        this();
        this.eventSchema = eventSchema;
        addLabel(eventSchema.getNameLabel());
    }

    public Predicate(StreamSchema streamSchema, EventSchema eventSchema) {
        this(eventSchema);
        this.streamSchema = streamSchema;
    }

    public boolean operatesOverLabel(Label label) {
        return labelSet.contains(label);
    }

    public Collection<EventFilter> getFilterCollection() {
        return filterCollection;
    }

    public void addFilter(EventFilter filter) {
        filterCollection.add(filter);
    }

    public void addLabel(Label label) {
        labelSet.add(label);
    }

    public boolean containsLabel(Label label){
        return labelSet.contains(label);
    }

    public Predicate copy() {
        if (this == TRUE_PREDICATE) return TRUE_PREDICATE;
        Predicate newPredicate = new Predicate(streamSchema, eventSchema);
        newPredicate.labelSet.addAll(labelSet);
        newPredicate.filterCollection.addAll(filterCollection);
        return newPredicate;
    }
}
