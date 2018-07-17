package cel.cea.predicate;


import cel.event.EventSchema;
import cel.stream.StreamSchema;

import java.util.*;

public class AndPredicate extends Predicate {


    public AndPredicate() {
        super();
    }

    public AndPredicate(Predicate left, Predicate right) {
        super(left, right);
    }

//    public AndPredicate(Collection<Predicate> preds) {
//        super(preds);
//    }

//    public OrPredicate negate() {
//        for ()
//    }
//    @Override
//    public boolean overEvent(EventSchema eventSchema) {
//        return super.overEvent(eventSchema);
//    }
//
//    @Override
//    public boolean overStream(StreamSchema streamSchema) {
//        return super.overStream(streamSchema);
//    }
}
