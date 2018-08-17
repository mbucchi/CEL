package cel.cea;

import static java.util.stream.Collectors.toList;

public class SequenceCEA extends CEA {

    public SequenceCEA(CEA first, CEA second) {
        initState = 0;
        nStates = first.nStates + second.nStates - 1;
        finalState = nStates - 1;

        // add all first CEA transitions
        transitions.addAll(first.transitions);

        // displace the second CEA transitions and add them
        int toDisplace = first.nStates - 1;

        // TODO: do not assume the last state is final
        transitions.addAll(
                second.transitions
                        .stream()
                        .map(transition -> transition.displaceTransition(toDisplace))
                        //map from state = finalstate
                        .collect(toList())
        );

        // add all the corresponding labels
        labelSet.addAll(first.labelSet);
        labelSet.addAll(second.labelSet);

        // add all the corresponding eventSchemas
//        eventSchemas.addAll(first.eventSchemas);
//        eventSchemas.addAll(second.eventSchemas);
    }
}
