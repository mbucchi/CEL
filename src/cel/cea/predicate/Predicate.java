package cel.cea.predicate;

import cel.event.EventSchema;
import cel.event.Label;
import cel.filter.AndEventFilter;
import cel.filter.EventFilter;
import cel.stream.StreamSchema;

import java.util.*;

public class Predicate {

    // this predicate is over ALL events and is always true.
    public static final Predicate TRUE_PREDICATE = new Predicate();

    private Collection<EventFilter> filterCollection;
    private StreamSchema streamSchema;
    private EventSchema eventSchema;
    private ArrayList<Predicate> predicates;
    private boolean negated = false;

    private Set<Label> labelSet;
    public boolean satisfiable;

    public Predicate() {
        filterCollection = new ArrayList<>();
        labelSet = new HashSet<>();
        predicates = new ArrayList<>();
        satisfiable = true;
    }

    public Predicate(Set<Label> labels) {
        this();
        labelSet = labels;
    }

    public Predicate(EventSchema eventSchema) {
        this();
        this.eventSchema = eventSchema;
        if (eventSchema != null) {
            addLabel(eventSchema.getNameLabel());
        }

    }

    public Predicate(StreamSchema streamSchema, EventSchema eventSchema) {
        this(eventSchema);
        this.streamSchema = streamSchema;
    }

//    public Predicate(Predicate inner) {
//        this(inner.getStreamSchema(), inner.getEventSchema());
//        this.labelSet.addAll(inner.labelSet);
//        this.filterCollection.addAll(inner.filterCollection);
//        this.satisfiable = inner.satisfiable;
//    }

    Predicate(Predicate p1, Predicate p2) {
        this();
        this.addPredicate(p1);
        this.addPredicate(p2);
    }

    Predicate(ArrayList<Predicate> predicates) {
        this();
        this.addPredicates(predicates);
    }

    public EventSchema getEventSchema() {
        return eventSchema;
    }

    private StreamSchema getStreamSchema() {
        return streamSchema;
    }

    public Set<Label> getLabelSet() {
        return labelSet;
    }

    public Collection<EventFilter> getFilterCollection() {
        return filterCollection;
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
        if (!satisfiable) {
            return;
        }
        if (predicates.size() > 0) {
            for (Predicate predicate : predicates) {
                if (useless(predicate, p)) {
                    return;
                }
                if (useless(p, predicate)) {
                    predicates.remove(predicate);
                }
                if (unsatisfiable(predicate, p)) {
                    satisfiable = false;
                    return;
                }
            }
            for (Predicate predicate : predicates) {
                if (merged(predicate, p)) {
                    return;
                }
            }
        }
        predicates.add(p.copy());
    }

    private boolean useless(Predicate p1, Predicate p2) {
        /* method that checks if p1 is equivalent to p1 && p2 */
        if (p1.dominates(p2)) {
            return true;
        }
        if (!p1.negated && p2.negated) {
            return p1.eventSchema != null && !p1.eventSchema.equals(p2.eventSchema);
        }
        /* TODO: ADD MORE CASES */
        return false;
    }

    private boolean unsatisfiable(Predicate p1, Predicate p2) {
        /* Method that checks if p1 && p2 is unsatisfiable */
        if (p1.dominates(p2.negate()) || p2.dominates(p1.negate())) {
            return true;
        }
        if (!p1.negated && !p2.negated) {
            if (p1.eventSchema != null && !p1.eventSchema.equals(p2.eventSchema)) {
                return true;
            }
            for (EventFilter ef1 : p1.getFilterCollection()) {
                for (EventFilter ef2 : p2.getFilterCollection()) {
                    if (ef1.equivalentTo(ef2.negate())) {
                        return true;
                    }
                }
            }
        }
        if (!p1.negated && p2.negated) {
            ArrayList<EventFilter> satisfiableFilters = new ArrayList<>(p2.getFilterCollection());
            for (EventFilter ef1 : p1.getFilterCollection()) {
                for (EventFilter ef2 : p2.getFilterCollection()) {
                    if (ef1.dominates(ef2)) {
                        satisfiableFilters.remove(ef2);
                    }
                }
            }
            return satisfiableFilters.isEmpty();
        }
        /* TODO: ADD MORE CASES */
        return false;
    }

    private void addPredicates(ArrayList<Predicate> predicates) {
        if (predicates == null) {
            return;
        }
        for (Predicate p : predicates) {
            this.addPredicate(p);
        }
    }

    public ArrayList<Predicate> getPredicates() {
        return predicates;
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
                equalEventSchema(this, other) && equalStreamSchema(this, other) &&
                this.negated == other.negated &&
                this.labelSet.equals(other.labelSet) &&
                new HashSet<>(this.filterCollection).equals(new HashSet<>(other.filterCollection));
    }

    private boolean dominates(Predicate other) {
        if (this.equals(other)) return true;
        if (this.negated != other.negated) return false;
        if (this.filterCollection.size() == 0 && other.filterCollection.size() == 0) {
            return true;
        }
        /* TODO: ADD MORE CASES */
//        if (predicates.size() > 0) {
//
//        } else {
//
//        }
        return false;
    }

    private boolean equalEventSchema(Predicate p1, Predicate p2) {
        if (p1.eventSchema == null) {
            return p2.eventSchema == null;
        }
        return p1.eventSchema.equals(p2.eventSchema);
    }

    private boolean equalStreamSchema(Predicate p1, Predicate p2) {
        if (p1.streamSchema == null) {
            return p2.streamSchema == null;
        }
        return p1.streamSchema.equals(p2.streamSchema);
    }

    private boolean merged(Predicate p1, Predicate p2) {
        if (equalEventSchema(p1, p2) && equalStreamSchema(p1, p2)) {
            if (!p1.negated && p2.negated) {
                boolean satisfiable = false;
                ArrayList<EventFilter> filtersToNegate = new ArrayList<>();
                for (EventFilter filter : p2.getFilterCollection()) {
                    if (!notRedundant(p1.getFilterCollection(), filter.negate())) {
                        satisfiable = true;
                        continue;
                    }
                    if (notRedundant(p1.getFilterCollection(), filter)) {
                        satisfiable = true;
                        filtersToNegate.add(filter);
                    }
                }
                if (!satisfiable) {
                    this.satisfiable = false;
                    return true;
                }
                if (filtersToNegate.isEmpty()) {
                    return true;
                }
                EventFilter newFilter;
                if (filtersToNegate.size() > 1) {
                    EventFilter temp = filtersToNegate.get(0);
                    newFilter = new AndEventFilter(temp.getLabel(), filtersToNegate);
                } else {
                    newFilter = filtersToNegate.get(0);
                }
                p1.addFilter(newFilter.negate());
                return true;
            }
            if (!p1.negated) {
                for (EventFilter filter : p2.getFilterCollection()) {
                    if (notRedundant(p1.getFilterCollection(), filter)) {
                        p1.addFilter(filter);
                    }
                }
                return true;
            }
        }
        return false;
    }

    private ArrayList<EventFilter> removeRedundants(ArrayList<EventFilter> filters) {
        for (int i = 0; i < filters.size(); i++) {
            for (int j = i + 1; j < filters.size(); j++) {
                if (filters.get(i).dominates(filters.get(j))) {
                    filters.remove(j);
                    }
                }
            }
        return filters;
    }

    public boolean notRedundant(Collection<EventFilter> filters, EventFilter newFilter) {
        /* checks if filters does not imply newFilter */
        for (EventFilter filter : filters) {
            if (filter.dominates(newFilter)) {
                return false;
            }
        }
        return true;
    }

    public void flatten() {
        if (predicates.size() == 1) {
            Predicate target = predicates.get(0);
            streamSchema = target.streamSchema;
            eventSchema = target.eventSchema;
            negated = target.negated ^ negated;
            filterCollection = target.filterCollection;
            labelSet = target.labelSet;
            satisfiable = true;
            predicates.clear();
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (predicates.size() > 0) {
            if (negated) {
                stringBuilder.append("NotMultiPredicate( ");
            } else {
                stringBuilder.append("MultiPredicate( ");
            }
            for (Predicate p : predicates) {
                stringBuilder.append(p.toString());
                stringBuilder.append(" ");
            }
        } else {
            String streamName = streamSchema != null ? streamSchema.getName() : "*";
            String eventName = eventSchema != null ? eventSchema.getName() : "*";
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
