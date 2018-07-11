package cel.cea;

import cel.cea.predicate.Predicate;
import cel.cea.transition.Transition;
import cel.cea.transition.TransitionType;
import cel.event.EventSchema;
import cel.stream.StreamSchema;

import java.util.Set;

public class SelectionCEA extends CEA {

    public SelectionCEA(Set<EventSchema> eventSchema){
        this(new Predicate(eventSchema), eventSchema);
    }

    public SelectionCEA(Set<StreamSchema> streamSchema, Set<EventSchema> eventSchema){
        this(new Predicate(streamSchema, eventSchema), eventSchema);
    }

    private SelectionCEA(Predicate predicate, Set<EventSchema> eventSchema){
        super(2, 0, 1);
        transitions.add(new Transition(0,0, Predicate.TRUE_PREDICATE, TransitionType.WHITE));
        transitions.add(new Transition(0,1, predicate, TransitionType.BLACK));

        for (EventSchema evSch : eventSchema) {
            labelSet.add(evSch.getNameLabel());
        }
        eventSchemas.addAll(eventSchema);
    }


}
