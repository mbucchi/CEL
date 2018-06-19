package cepl.cea;

import cepl.cea.utils.Predicate;
import cepl.cea.utils.Transition;
import cepl.cea.utils.TransitionType;
import cepl.event.EventSchema;
import cepl.stream.StreamSchema;

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
