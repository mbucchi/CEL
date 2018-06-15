package cepl.cea;

import cepl.event.EventSchema;
import cepl.cea.utils.Label;
import java.util.*;

import static java.util.stream.Collectors.toList;

public class CEA {

    int nStates;
    int initState;
    int finalState;
    Collection<Transition> transitions;
    Set<Label> labelSet;
    Set<EventSchema> eventSchemas;

    CEA() {
        transitions = new ArrayList<>();
        labelSet = new HashSet<>();
        eventSchemas = new HashSet<>();
    }

    CEA(CEA otherCea) {
        this();
        nStates = otherCea.nStates;
        initState = otherCea.initState;
        finalState = otherCea.finalState;

        transitions.addAll(
                otherCea.transitions
                        .stream()
                        .map(Transition::copy)
                        .collect(toList())
        );

        labelSet.addAll(otherCea.labelSet);
        eventSchemas.addAll(otherCea.eventSchemas);
    }

    CEA(int nStates, int initState, int finalState){
        this();
        this.nStates = nStates;
        this.initState = initState;
        this.finalState = finalState;
    }

    public CEA copy() {
        return new CEA(this);
    }

    public Collection<Transition> getTransitions() {
        return new ArrayList<>(transitions);
    }

    public Set<EventSchema> getEventSchemas(){
        return new HashSet<>(eventSchemas);
    }
}
