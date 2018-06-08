package cepl.cea;

import cepl.cea.utils.TransitionType;
import cepl.event.EventSchema;
import cepl.cea.utils.Label;
import cepl.stream.StreamSchema;

import java.util.*;

import static java.util.stream.Collectors.toList;

public class CEA {

    private int nStates;
    private int initState;
    private int finalState;
    private Collection<Transition> transitions;
    private Set<Label> labelSet;

    private CEA(int nStates, int initState, int finalState){
        transitions = new ArrayList<>();
        labelSet = new HashSet<>();
        this.nStates = nStates;
        this.initState = initState;
        this.finalState = finalState;
    }

    public CEA copy() {
        CEA newCea = new CEA(nStates, initState, finalState);
        newCea.transitions.addAll(
                transitions
                        .stream()
                        .map(Transition::copy)
                        .collect(toList())
        );
        newCea.labelSet.addAll(labelSet);
        return newCea;
    }

    public Collection<Transition> getTransitions() {
        return transitions;
    }

    public CEA relabelCEA(Label label) {
        CEA newCea = copy();
        for (Transition transition : newCea.transitions) {
            transition.addLabel(label);
        }
        newCea.labelSet.add(label);
        return newCea;
    }

    public static CEA createOrCEA(CEA left, CEA right) {
        int nStates = left.nStates + right.nStates + 2;
        int initState = 0;
        int finalState = nStates - 1;

        CEA cea = new CEA(nStates, initState, finalState);

        // copy all but final original transitions from left CEA, displacing them one state
        int toDisplaceLeft = 1;
        cea.transitions.addAll(
                left.transitions
                        .stream()
                        .filter(transition -> transition.getToState() != left.finalState)
                        .map(transition -> transition.displaceTransition(toDisplaceLeft))
                        .collect(toList())
        );

        // copy all black transitions from initial state in left CEA and displace
        cea.transitions.addAll(
                left.transitions
                        .stream()
                        .filter(transition -> transition.getFromState() == left.initState)
                        .filter(transition -> transition.getType() == TransitionType.BLACK)
                        .map(transition -> transition.displaceTransition(toDisplaceLeft)
                                .replaceFromState(0))
                        .collect(toList())
        );

        // copy all black transitions to final state in left CEA and displace
        cea.transitions.addAll(
                left.transitions
                        .stream()
                        .filter(transition -> transition.getToState() == left.finalState)
                        .filter(transition -> transition.getType() == TransitionType.BLACK)
                        .map(transition -> transition.displaceTransition(toDisplaceLeft)
                                .replaceToState(finalState))
                        .collect(toList())
        );

        // repeat previous steps but displacing 1 + left.nStates states
        int toDisplaceRight = 1 + left.nStates;
        cea.transitions.addAll(
                right.transitions
                        .stream()
                        .map(transition -> transition.displaceTransition(toDisplaceRight))
                        .collect(toList())
        );

        // copy all black transitions from initial state in right CEA and displace
        cea.transitions.addAll(
                right.transitions
                        .stream()
                        .filter(transition -> transition.getFromState() == right.initState)
                        .filter(transition -> transition.getType() == TransitionType.BLACK)
                        .map(transition -> transition.displaceTransition(toDisplaceRight)
                                                     .replaceFromState(0))
                        .collect(toList())
        );

        // copy all black transitions to final state in right CEA and displace
        cea.transitions.addAll(
                right.transitions
                        .stream()
                        .filter(transition -> transition.getToState() == right.finalState)
                        .filter(transition -> transition.getType() == TransitionType.BLACK)
                        .map(transition -> transition.displaceTransition(toDisplaceRight)
                                .replaceToState(finalState))
                        .collect(toList())
        );

        // add the missing white transition
        cea.transitions.add(new Transition(0, 0, Predicate.TRUE_PREDICATE, TransitionType.WHITE));

        // add all the corresponding labels
        cea.labelSet.addAll(left.labelSet);
        cea.labelSet.addAll(right.labelSet);

        return cea;
    }

    public static CEA createKleeneCEA(CEA inner) {
        int initState = 0;
        int finalState = inner.finalState;

        CEA cea = inner.copy();

        // copy all transitions that go to finalState and add them to initState as well
        cea.transitions.addAll(
                cea.transitions
                        .stream()
                        .filter(transition -> transition.getToState() == finalState)
                        .map(transition -> transition.replaceToState(initState))
                        .collect(toList())
        );

        return cea;
    }

    public static CEA createSeqCEA(CEA first, CEA second) {

        int nStates = first.nStates + second.nStates - 1;
        CEA cea = new CEA(nStates, 0, nStates - 1);
        // add all first CEA transitions
        cea.transitions.addAll(first.transitions);

        // displace the second CEA transitions and add them
        int toDisplace = first.nStates - 1;
        cea.transitions.addAll(
                second.transitions
                        .stream()
                        .map( transition -> transition.displaceTransition(toDisplace) )
                        .collect(toList())
        );

        // add all the corresponding labels
        cea.labelSet.addAll(first.labelSet);
        cea.labelSet.addAll(second.labelSet);

        return cea;
    }

    public static CEA createSelectionCEA(EventSchema eventSchema){
        return createSelectionCEA(new Predicate(eventSchema), eventSchema);
    }

    public static CEA createSelectionCEA(StreamSchema streamSchema, EventSchema eventSchema){
        return createSelectionCEA(new Predicate(streamSchema, eventSchema), eventSchema);
    }

    private static CEA createSelectionCEA(Predicate predicate, EventSchema eventSchema){
        CEA cea = new CEA(2, 0, 1);

        cea.transitions.add(new Transition(0,0, Predicate.TRUE_PREDICATE, TransitionType.WHITE));
        cea.transitions.add(new Transition(0,1, predicate, TransitionType.BLACK));

        cea.labelSet.add(eventSchema.getNameLabel());

        return cea;
    }

}
