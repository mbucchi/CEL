package cel.cea;

import cel.event.Label;
import cel.predicate.BitPredicate;
import cel.predicate.BitPredicateFactory;
import cel.cea.transition.Transition;
import cel.cea.transition.TransitionType;
import cel.event.EventSchema;
import cel.stream.StreamSchema;

public class SelectionCEA extends CEA {

    public SelectionCEA(EventSchema eventSchema) {
        this(BitPredicateFactory.getInstance().from(eventSchema), eventSchema.getNameLabel());
    }

    public SelectionCEA(StreamSchema streamSchema, EventSchema eventSchema) {
        this(BitPredicateFactory.getInstance().from(streamSchema, eventSchema), eventSchema.getNameLabel());
    }

    private SelectionCEA(BitPredicate bitPredicate, Label label) {
        super(2, 0, 1);

        transitions.add(new Transition(0, 0, BitPredicate.getTruePredicate(), label, TransitionType.WHITE));
        transitions.add(new Transition(0, 1, bitPredicate, label, TransitionType.BLACK));

        labelSet.add(label);
    }


}
