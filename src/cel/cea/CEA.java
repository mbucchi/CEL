package cel.cea;

import cel.cea.transition.Transition;
import cel.event.EventSchema;
import cel.event.Label;
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

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("CEA(\n")
                .append("  nStates=").append(nStates).append(",\n")
                .append("  initState=").append(initState).append(",\n")
                .append("  finalState=").append(finalState).append(",\n")
                .append("  transitions=[");

        if (transitions.size() > 0){
            stringBuilder.append("\n    ");
        }

        Transition[] transitionArray = transitions.toArray(new Transition[0]);


        int i;
        for (i=0; i<transitions.size()-1; i++){
            Transition transition = transitionArray[i];
            stringBuilder.append(transition).append(",\n    ");
        }

        if (transitions.size() > 0) {
            stringBuilder.append(transitionArray[i]).append("\n  ");
        }

        stringBuilder.append("]\n)");
        return stringBuilder.toString();
    }
}
