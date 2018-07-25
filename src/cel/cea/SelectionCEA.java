package cel.cea;

import cel.predicate.FilterPredicate;
import cel.predicate.Predicate;
import cel.cea.transition.Transition;
import cel.cea.transition.TransitionType;
import cel.event.EventSchema;
import cel.stream.StreamSchema;

import java.util.HashSet;

public class SelectionCEA extends CEA {

    public SelectionCEA(EventSchema eventSchema){
        this(new FilterPredicate(eventSchema), eventSchema);
    }

    public SelectionCEA(StreamSchema streamSchema, EventSchema eventSchema){
        this(new FilterPredicate(streamSchema, eventSchema), eventSchema);
    }

    private SelectionCEA(Predicate predicate, EventSchema eventSchema){
        super(2, 0, new HashSet<>(){{add(1);}});
        transitions.add(new Transition(0,0, Predicate.getTruePredicate(), TransitionType.WHITE));
        transitions.add(new Transition(0,1, predicate, TransitionType.BLACK));

        labelSet.add(eventSchema.getNameLabel());
        eventSchemas.add(eventSchema);
    }


}
