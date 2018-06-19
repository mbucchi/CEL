package cel.cea;

import static java.util.stream.Collectors.toList;

public class KleeneCEA extends CEA {

    public KleeneCEA(CEA inner) {
        super(inner);

        // copy all transitions that go to finalState and add them to initState as well
        transitions.addAll(
            transitions
                    .stream()
                    .filter(transition -> transition.getToState() == finalState)
                    .map(transition -> transition.replaceToState(initState))
                    .collect(toList())
        );
    }

}
