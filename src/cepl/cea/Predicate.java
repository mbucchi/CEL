package cepl.cea;

import cepl.event.Event;
import cepl.event.EventSchema;
import cepl.cea.utils.Label;
import cepl.filter.EventFilter;
import cepl.stream.StreamSchema;
import cepl.values.Attribute;
import cepl.values.ValueType;

import java.util.*;

class Predicate {
    static final Predicate TRUE_PREDICATE = new Predicate();

    private Collection<EventFilter> filterCollection;
    private StreamSchema streamSchema;
    private EventSchema eventSchema;
    private Set<Label> labelSet;
    private boolean satisfiable;

    private Predicate() {
        filterCollection = new ArrayList<>();
        labelSet = new HashSet<>();
        satisfiable = true;
    }

    Predicate(EventSchema eventSchema) {
        this();
        this.eventSchema = eventSchema;
        addLabel(eventSchema.getNameLabel());
    }

    Predicate(StreamSchema streamSchema, EventSchema eventSchema) {
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
        Map<String, Class> attributeClassMap = eventSchema.getAttributes();

        for (Attribute attribute : filter.getAttributes()){
            Class cls = attributeClassMap.getOrDefault(attribute.getName(),null);
            if (cls == null){
                // Attribute does not exist
                return false;
            }
            boolean validAttribute = false;
            for (ValueType valueType : filter.getValueTypes()){
                if (valueType.validForDataType(cls)) {
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
        return newPredicate;
    }

    @Override
    public String toString() {
        String eventName = eventSchema != null ? eventSchema.getName() : "*";
        StringBuilder stringBuilder = new StringBuilder("Predicate(" + eventName + ", ");
        boolean hasFilters = false;
        for (EventFilter eventFilter : filterCollection) {
            if (hasFilters) stringBuilder.append(", ");
            stringBuilder.append(eventFilter.toString());
            hasFilters = true;
        }

        if (!satisfiable) stringBuilder.append("FALSE");
        else if (!hasFilters) stringBuilder.append("TRUE");

        stringBuilder.append(")");
        return stringBuilder.toString();
    }
}
