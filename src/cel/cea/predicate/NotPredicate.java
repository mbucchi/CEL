package cel.cea.predicate;

import cel.event.EventSchema;
import cel.stream.StreamSchema;

public class NotPredicate extends Predicate {

    public NotPredicate(Predicate inner) {
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
