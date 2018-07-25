package cel.predicate;

import cel.event.EventSchema;
import cel.event.Label;
import cel.stream.StreamSchema;

import java.util.*;

public abstract class Predicate {

    private final EventSchema eventSchema;
    private final StreamSchema streamSchema;
    private final Set<Label> labelSet;

    public EventSchema getEventSchema() {
        return eventSchema;
    }

    public StreamSchema getStreamSchema() {
        return streamSchema;
    }

    public Set<Label> getLabelSet() {
        return labelSet;
    }

    protected Predicate(EventSchema eventSchema) {
        this(eventSchema, StreamSchema.ANY());
    }

    protected Predicate(EventSchema eventSchema, StreamSchema streamSchema) {
        this(eventSchema, streamSchema, Set.of(eventSchema.getNameLabel()));
    }

    protected Predicate(EventSchema eventSchema, StreamSchema streamSchema, Set<Label> labelSet) {
        this.eventSchema = eventSchema;
        this.streamSchema = streamSchema;
        this.labelSet = new HashSet<>(labelSet);
    }

    protected Predicate(Predicate toCopy) {
        eventSchema = toCopy.eventSchema;
        streamSchema = toCopy.streamSchema;
        labelSet = new HashSet<>(toCopy.labelSet);
    }

    public static Predicate getTruePredicate() {
        return TruePredicate.instance;
    }

    public static Predicate getFalsePredicate() {
        return FalsePredicate.instance;
    }

    public Predicate addLabel(Label label) {
        Predicate newPredicate = this.copy();
        newPredicate.labelSet.add(label);
        return newPredicate;
    }

    public static boolean overSameStreamAndEvent(Collection<Predicate> predicates) {
        List<Predicate> predicateList = List.copyOf(predicates);
        Predicate first = predicateList.get(0);
        EventSchema eventSchema = first.eventSchema;
        StreamSchema streamSchema = first.streamSchema;

        return predicateList.stream().allMatch(
                p -> eventSchema.equals(p.eventSchema) && streamSchema.equals(p.streamSchema)
        );
    }

    public abstract Predicate copy();

    protected abstract boolean implies(Predicate other);

    @Override
    public abstract boolean equals(Object obj);

    public abstract Predicate flatten();

    public abstract Predicate negate();

    public boolean overEvent(EventSchema eventSchema){
        if (this.eventSchema.isAny()) return true;
        return eventSchema.equals(this.eventSchema);
    }

    public abstract boolean isSatisfiable();

    abstract boolean inconsistentWith(Predicate other);

    abstract boolean consistentWith(Collection<Predicate> predicates);

    public boolean overLabel(Label label) {
        return labelSet.contains(label);
    }

    public boolean overSameEvent(Predicate other) {
        return eventSchema.equals(other.eventSchema);
    }

    public boolean overStream(StreamSchema streamSchema){
        if (this.streamSchema.isAny()) return true;
        return streamSchema.equals(this.streamSchema);
    }

    @Override
    public String toString(){
        String streamName = streamSchema.isAny() ?  "*" : streamSchema.getName();
        String eventName = eventSchema.isAny() ? "*" : eventSchema.getName();
        return "Predicate(" + streamName + ", " + eventName + ", " + innerToString() + ")";
    }

    abstract String innerToString();

    protected static List<Predicate> getUseful(Collection<Predicate> predicates) {
        List<Predicate> useful = new ArrayList<>();

        for (Predicate p : predicates) {
            if (useful.stream()
                    .filter(pr -> pr != p)
                    .anyMatch(pr -> pr.implies(p))) continue;
            useful.add(p);
        }
        return useful;
    }

    protected static boolean isConsistent(Collection<Predicate> predicates) {
        return predicates
                .stream()
                .allMatch(p1 -> p1.isSatisfiable() && p1.consistentWith(predicates));
    }
}
