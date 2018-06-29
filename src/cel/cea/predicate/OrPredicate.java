package cel.cea.predicate;

import cel.event.EventSchema;
import cel.stream.StreamSchema;

public class OrPredicate extends Predicate {

    public OrPredicate(Predicate left, Predicate right) {
        super(null);
        // todo
    }

    @Override
    public boolean overEvent(EventSchema eventSchema) {
        return super.overEvent(eventSchema);
    }

    @Override
    public boolean overStream(StreamSchema streamSchema) {
        return super.overStream(streamSchema);
    }
}