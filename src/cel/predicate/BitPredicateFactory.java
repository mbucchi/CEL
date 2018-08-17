package cel.predicate;

import cel.event.EventSchema;
import cel.stream.StreamSchema;

import java.util.*;
import java.util.stream.Collectors;

public class BitPredicateFactory {

    private static BitPredicateFactory instance;

    private BitPredicateFactory(
            List<StreamSchema> streamSchemas,
            List<EventSchema> eventSchemas,
            int nPredicates
    ){
        this.streamSchemas= streamSchemas;
        this.eventSchemas = eventSchemas;
        predicates = new ArrayList<>();

        streamOffset = 0;
        eventOffset = streamSchemas.size();
        predicateOffset = eventOffset + eventSchemas.size();
        nBits = predicateOffset + nPredicates;
    }

    public static BitPredicateFactory create(
            Set<StreamSchema> streamSchemas,
            Set<EventSchema> eventSchemas,
            int nPredicates
    ){
        if (instance != null) throw new Error("BitPredicateFactory has already been created");
        instance = new BitPredicateFactory(new ArrayList<>(streamSchemas), new ArrayList<>(eventSchemas), nPredicates);
        return instance;
    }

    public static BitPredicateFactory getInstance(){
        if (instance == null) throw new Error("BitPredicateFactory has not been created");
        return instance;
    }

    private List<StreamSchema> streamSchemas;
    private List<EventSchema> eventSchemas;
    private List<Predicate> predicates;
    private int nBits;
    private int streamOffset;
    private int eventOffset;
    private int predicateOffset;

    public int getnBits() {
        return nBits;
    }

    public List<String> getStringDescription(){
        List<String> strings = new ArrayList<>();
        strings.addAll(streamSchemas.stream().map(streamSchema -> "stream(event) == " + streamSchema.getName()).collect(Collectors.toList()));
        strings.addAll(eventSchemas.stream().map(eventSchema -> "type(event) == " + eventSchema.getName()).collect(Collectors.toList()));
        strings.addAll(predicates.stream().map(Predicate::toString).collect(Collectors.toList()));
        return strings;
    }

    public BitPredicate from(EventSchema eventSchema){
        int eventBit = eventSchemas.indexOf(eventSchema) + eventOffset;

        if (eventBit < 0) throw new Error("Invalid eventSchema for Factory");

        BitSet mask = new BitSet(nBits);
        mask.set(eventBit);

        BitSet match = new BitSet(nBits);
        match.set(eventBit);

        return new BitPredicate(mask, match);
    }

    public BitPredicate from(StreamSchema streamSchema, EventSchema eventSchema){
        int eventBit = eventSchemas.indexOf(eventSchema) + eventOffset;
        int streamBit = streamSchemas.indexOf(streamSchema) + streamOffset;

        if (eventBit < 0) throw new Error("Invalid eventSchema for Factory");
        if (streamBit < 0) throw new Error("Invalid streamSchema for Factory");

        BitSet mask = new BitSet(nBits);
        mask.set(eventBit);
        mask.set(streamBit);

        BitSet match = new BitSet(nBits);
        match.set(eventBit);
        match.set(streamBit);

        return new BitPredicate(mask, match);
    }

    public BitPredicate from(Predicate predicate){
        int predicateBit = predicates.indexOf(predicate);

        if (predicateBit < 0) {
            predicateBit = predicates.size();
            predicates.add(predicate);
        }

        predicateBit += predicateOffset;

        BitSet mask = new BitSet(nBits);
        mask.set(predicateBit);

        BitSet match = new BitSet(nBits);
        match.set(predicateBit);
        return new BitPredicate(mask, match);
    }
}
