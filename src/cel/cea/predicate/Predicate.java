package cel.cea.predicate;

import cel.event.Event;
import cel.event.EventSchema;
import cel.event.Label;
import cel.filter.EventFilter;
import cel.stream.StreamSchema;
import cel.values.Attribute;
import cel.values.ValueType;
import org.antlr.v4.runtime.misc.Array2DHashSet;

import java.util.*;
import java.util.stream.Collectors;

public class Predicate {

    // this predicate is over ALL events and is always true.
    public static final Predicate TRUE_PREDICATE = new Predicate();

    public Collection<EventFilter> filterCollection;
    private Set<StreamSchema> streamSchema;
    private Set<EventSchema> eventSchema;
    private Collection<Predicate> predicates;
    private boolean negated = false;

    public Set<Label> labelSet;
    public boolean satisfiable;

    public Predicate() {
        filterCollection = new ArrayList<>();
        labelSet = new HashSet<>();
        predicates = new ArrayList<>();
        streamSchema = new HashSet<>();
        eventSchema = new HashSet<>();
        satisfiable = true;
    }

    public Predicate(HashSet<Label> labels) {
        this();
        labelSet = labels;
    }

    public Predicate(Set<EventSchema> eventSchema) {
        this();
        this.eventSchema.addAll(eventSchema);
        for (EventSchema evSch : eventSchema) {
            addLabel(evSch.getNameLabel());
        }
    }

    public Predicate(Set<StreamSchema> streamSchema, Set<EventSchema> eventSchema) {
        this(eventSchema);
        this.streamSchema.addAll(streamSchema);
    }

    public Predicate(Predicate inner) {
        this(inner.getStreamSchema(), inner.getEventSchema());
        this.labelSet.addAll(inner.labelSet);
        this.filterCollection.addAll(inner.filterCollection);
        this.satisfiable = inner.satisfiable;
    }

    public Predicate(Predicate p1, Predicate p2) {
        this();
        this.addPredicate(p1);
        this.addPredicate(p2);
    }

    public Predicate(Collection<Predicate> predicates) {
        this();
        this.addPredicates(predicates);
    }

    public Set<EventSchema> getEventSchema() {
        return eventSchema;
    }

    public Set<StreamSchema> getStreamSchema() {
        return streamSchema;
    }

    public Set<Label> getLabelSet() {
        return labelSet;
    }

    public Collection<Collection<EventFilter>> getFilterCollection() {
        Collection<Collection<EventFilter>> streamSchema = new ArrayList<>();
        for (Predicate p : predicates) {
            streamSchema.addAll(p.getFilterCollection());
        }
        return streamSchema;
    }

    public void addFilter(EventFilter filter) {
        if (!satisfiable) return;
//        if (validForAttributeTypes(filter)){
        if (predicates.size() > 0) {
            for (Predicate p : predicates) {
                p.addFilter(filter);
            }
        } else {
            filterCollection.add(filter);
        }
//        }
//        else {
//            makeFalsePredicate();
//        }
    }

    public void addPredicate(Predicate p) {
        if (predicates.size() > 0) {
            for (Predicate pred : predicates) {
                if (pred.dominates(p)) {
                    return;
                }
                if (pred.dominates(p.negate())) {
                    satisfiable = false;
                    return;
                }
            }
        }
        predicates.add(p);
        Set<StreamSchema> strSch = p.getStreamSchema();
        Set<EventSchema> evSch = p.getEventSchema();
        if (strSch != null) {
            streamSchema.addAll(strSch);
        }
        if (evSch != null) {
            eventSchema.addAll(evSch);
        }
        labelSet.addAll(p.getLabelSet());
    }

    public void addPredicates(Collection<Predicate> predicates) {
        if (predicates == null) {
            return;
        }
        for (Predicate p : predicates) {
            this.addPredicate(p);
        }
    }

    public Collection<Predicate> getPredicates() {
        return predicates;
    }

    //    private boolean validForAttributeTypes(EventFilter filter) {
//        Map<String, ValueType> attributeTypes = eventSchema.getAttributes();
//
//        for (Attribute attribute : filter.getAttributes()){
//            ValueType valueType= attributeTypes.getOrDefault(attribute.getName(),null);
//            if (valueType == null){
//                // Attribute does not exist
//                return false;
//            }
//            boolean validAttribute = false;
//            for (ValueType filterValueType : filter.getValueTypes()){
//                if (valueType.interoperableWith(filterValueType)) {
//                    validAttribute = true;
//                    break;
//                }
//            }
//            if (!validAttribute) return false;
//        }
//        return true;
//    }

    private void makeFalsePredicate() {
        filterCollection.clear();
        satisfiable = false;
    }

    public void addLabel(Label label) {
        labelSet.add(label);
    }

    public boolean containsLabel(Label label){
        return labelSet.contains(label);
    }

    public Predicate copy() {
        Predicate newPredicate = new Predicate(streamSchema, eventSchema);
        newPredicate.labelSet.addAll(labelSet);
        newPredicate.filterCollection.addAll(filterCollection);
        newPredicate.addPredicates(predicates);
        newPredicate.satisfiable = satisfiable;
        return newPredicate;
    }

    public boolean overStream(StreamSchema streamSchema){
        return this.streamSchema == null || this.streamSchema.equals(streamSchema);
    }

    public boolean overEvent(EventSchema eventSchema){
        return this.eventSchema == null || this.eventSchema.equals(eventSchema);
    }

    public Predicate negate() {
        Predicate newPredicate = this.copy();
        newPredicate.negated = !negated;
        return newPredicate;
    }

    public boolean equals(Predicate other) {
        if (this == other) return true;
        return this.predicates.equals(other.predicates) &&
                this.eventSchema.equals(other.eventSchema) &&
                this.streamSchema.equals(other.streamSchema) &&
                this.negated == other.negated &&
                this.labelSet.equals(other.labelSet) &&
                new HashSet<>(this.filterCollection).equals(new HashSet<>(other.filterCollection));
    }

    public boolean dominates(Predicate other) {
        if (this.equals(other)) return true;
        if (this.negated != other.negated) return false;
        if (this.filterCollection.size() == 0 && other.filterCollection.size() == 0) {
            return true;
        }
        if (predicates.size() > 0) {
            
        } else {

        }
        return false;
    }

    public void minimize() {
        /* TODO: IMPLEMENT THIS */
    }

    public String getStreamNames() {
        StringBuilder ret = new StringBuilder();
        int i = 0;
        for (StreamSchema strSch : streamSchema) {
            ret.append(strSch.getName());
            if (++i != streamSchema.size()) {
                ret.append(", ");
            }
        }
        return ret.toString();
    }

    public String getEventNames() {
        StringBuilder ret = new StringBuilder();
        int i = 0;
        for (EventSchema evSch : eventSchema) {
            ret.append(evSch.getName());
            if (++i != eventSchema.size()) {
                ret.append(", ");
            }
        }
        return ret.toString();
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (predicates.size() > 0) {
            stringBuilder.append("ListPredicates( ");
            for (Predicate p : predicates) {
                stringBuilder.append(p.toString());
                stringBuilder.append(" ");
            }
        } else {
            String streamName = streamSchema.size() != 0 ? getStreamNames() : "*";
            String eventName = eventSchema.size() != 0 ? getEventNames() : "*";
            if (negated) {
                stringBuilder.append("NotPredicate(");
            } else {
                stringBuilder.append("Predicate(");
            }
            stringBuilder.append(streamName).append(", ").append(eventName).append(", [");
            boolean hasFilters = false;
            for (EventFilter eventFilter : filterCollection) {
                if (hasFilters) stringBuilder.append(", ");
                stringBuilder.append(eventFilter.toString());
                hasFilters = true;
            }

            if (!satisfiable) stringBuilder.append("FALSE");
            else if (!hasFilters && predicates.size() == 0) stringBuilder.append("TRUE");

            if (predicates.size() > 0) {
                for (Predicate p : predicates) {
                    stringBuilder.append(p.toString());
                }
            }

            stringBuilder.append("])");
        }
        return stringBuilder.toString();
    }
}
