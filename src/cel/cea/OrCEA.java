package cel.cea;

import cel.cea.predicate.Predicate;
import cel.cea.transition.Transition;
import cel.cea.transition.TransitionType;

import static java.util.stream.Collectors.toList;

public class OrCEA extends CEA {

    public OrCEA(CEA left, CEA right) {
        // TODO : remove minimization
        nStates = left.nStates + right.nStates + 2;
        initState = 0;
        finalState = nStates - 1;

        // copy all transitions from left CEA, displacing them one state
        int toDisplaceLeft = 1;
        transitions.addAll(
                left.transitions
                        .stream()
                        .map(transition -> transition.displaceTransition(toDisplaceLeft))
                        .collect(toList())
        );

        // copy all black transitions from initial state in left CEA and alter them to come from
        // the new initial state
        transitions.addAll(
                left.transitions
                        .stream()
                        .filter(transition -> transition.getFromState() == left.initState)
                        .filter(transition -> transition.getType() == TransitionType.BLACK)
                        .map(transition -> transition.displaceTransition(toDisplaceLeft)
                                .replaceFromState(initState))
                        .collect(toList())
        );

        // copy all black transitions to final state in left CEA and alter them to lead to
        // the new final state
        transitions.addAll(
                left.transitions
                        .stream()
                        .filter(transition ->  transition.getToState() == left.finalState)
                        .filter(transition -> transition.getType() == TransitionType.BLACK)
                        .map(transition -> transition.displaceTransition(toDisplaceLeft)
                                .replaceToState(finalState))
                        .collect(toList())
        );

        // repeat previous steps but displacing 1 + left.nStates states
        int toDisplaceRight = 1 + left.nStates;
        transitions.addAll(
                right.transitions
                        .stream()
                        .map(transition -> transition.displaceTransition(toDisplaceRight))
                        .collect(toList())
        );

        // copy all black transitions from initial state in right CEA and displace
        transitions.addAll(
                right.transitions
                        .stream()
                        .filter(transition -> transition.getFromState() == right.initState)
                        .filter(transition -> transition.getType() == TransitionType.BLACK)
                        .map(transition -> transition.displaceTransition(toDisplaceRight)
                                .replaceFromState(0))
                        .collect(toList())
        );

        // copy all black transitions to final state in right CEA and displace
        transitions.addAll(
                right.transitions
                        .stream()
                        .filter(transition -> transition.getToState() == right.finalState)
                        .filter(transition -> transition.getType() == TransitionType.BLACK)
                        .map(transition -> transition.displaceTransition(toDisplaceRight)
                                .replaceToState(finalState))
                        .collect(toList())
        );

        // add the missing white transition
        transitions.add(new Transition(0, 0, Predicate.TRUE_PREDICATE, TransitionType.WHITE));

        // add all the corresponding labels
        labelSet.addAll(left.labelSet);
        labelSet.addAll(right.labelSet);

        // add all the corresponding eventSchemas
        eventSchemas.addAll(left.eventSchemas);
        eventSchemas.addAll(right.eventSchemas);
    }
}
