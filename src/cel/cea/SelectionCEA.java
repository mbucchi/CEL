package cel.cea;

import cel.cea.predicate.Predicate;
import cel.cea.transition.Transition;
import cel.cea.transition.TransitionType;
import cel.event.EventSchema;
import cel.stream.StreamSchema;

public class SelectionCEA extends CEA {

    public SelectionCEA(EventSchema eventSchema){
        this(new Predicate(eventSchema), eventSchema);
    }

    public SelectionCEA(StreamSchema streamSchema, EventSchema eventSchema){
        this(new Predicate(streamSchema, eventSchema), eventSchema);
    }

    private SelectionCEA(Predicate predicate, EventSchema eventSchema){
        super(2, 0, 1);
        transitions.add(new Transition(0,0, Predicate.TRUE_PREDICATE, TransitionType.WHITE));
        transitions.add(new Transition(0,1, predicate, TransitionType.BLACK));

        labelSet.add(eventSchema.getNameLabel());
        eventSchemas.add(eventSchema);
    }


}
