package cel.cea.utils;

import cel.event.EventSchema;
import cel.event.Label;
import cel.filter.EventFilter;
import cel.stream.StreamSchema;
import cel.values.Attribute;
import cel.values.ValueType;

import java.util.*;
import java.util.stream.Collectors;

public class Predicate {

    // this predicate is over ALL events and is always true.
    public static final Predicate TRUE_PREDICATE = new Predicate(
            EventSchema.getAllSchemas().values().stream().map(EventSchema::getNameLabel).collect(Collectors.toSet())
    );

    private Collection<EventFilter> filterCollection;
    private StreamSchema streamSchema;
    private EventSchema eventSchema;

    EventSchema getEventSchema() {
        return eventSchema;
    }

    Set<Label> getLabelSet() {
        return labelSet;
    }

    private Set<Label> labelSet;
    private boolean satisfiable;

    private Predicate() {
        filterCollection = new ArrayList<>();
        labelSet = new HashSet<>();
        satisfiable = true;
    }

    private Predicate(Set<Label> labels){
        this();
        labelSet = labels;
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

    Collection<EventFilter> getFilterCollection() {
        return filterCollection;
    }

    boolean addFilter(EventFilter filter) {
        if (!satisfiable){
            return false;
        }
        else if (validForAttributeTypes(filter)){
            filterCollection.add(filter);
            return true;
        }
        else {
            makeFalsePredicate();
            return false;
        }
    }

    private boolean validForAttributeTypes(EventFilter filter) {
        Map<String, ValueType> attributeTypes = eventSchema.getAttributes();

        for (Attribute attribute : filter.getAttributes()){
            ValueType valueType= attributeTypes.getOrDefault(attribute.getName(),null);
            if (valueType == null){
                // Attribute does not exist
                return false;
            }
            boolean validAttribute = false;
            for (ValueType filterValueType : filter.getValueTypes()){
                if (valueType.interoperableWith(filterValueType)) {
                    validAttribute = true;
                    break;
                }
            }
            if (!validAttribute) return false;
        }
        return true;
    }

    private void makeFalsePredicate() {
        filterCollection.clear();
        satisfiable = false;
    }

    void addLabel(Label label) {
        labelSet.add(label);
    }

    boolean containsLabel(Label label){
        return labelSet.contains(label);
    }

    Predicate copy() {
        if (this == TRUE_PREDICATE) return TRUE_PREDICATE;
        Predicate newPredicate = new Predicate(streamSchema, eventSchema);
        newPredicate.labelSet.addAll(labelSet);
        newPredicate.filterCollection.addAll(filterCollection);
        newPredicate.satisfiable = satisfiable;
        return newPredicate;
    }

    @Override
    public String toString() {
        String streamName = streamSchema != null ? streamSchema.getName() : "*";
        String eventName = eventSchema != null ? eventSchema.getName() : "*";
        StringBuilder stringBuilder = new StringBuilder("Predicate(" + streamName + ", " + eventName + ", [");
        boolean hasFilters = false;
        for (EventFilter eventFilter : filterCollection) {
            if (hasFilters) stringBuilder.append(", ");
            stringBuilder.append(eventFilter.toString());
            hasFilters = true;
        }

        if (!satisfiable) stringBuilder.append("FALSE");
        else if (!hasFilters) stringBuilder.append("TRUE");

        stringBuilder.append("])");
        return stringBuilder.toString();
    }
}
