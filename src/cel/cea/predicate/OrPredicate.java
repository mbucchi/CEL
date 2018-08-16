package cel.cea.predicate;

public class OrPredicate extends Predicate {

    public OrPredicate() {
        super();
    }

    public OrPredicate(Predicate left, Predicate right) {
        super(left, right);
    }

//    public OrPredicate(Collection<Predicate> preds) {
//        super(preds);
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