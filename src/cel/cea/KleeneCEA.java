package cel.cea;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

public class KleeneCEA extends CEA {

    public KleeneCEA(CEA inner) {
        super(inner);

        // copy all transitions that go to finalState and add them to initState as well
        transitions.addAll(
            transitions
                    .stream()
                    .filter(transition -> inner.finalStates.contains(transition.getToState()))
                    .map(transition -> transition.replaceToState(initState))
                    .collect(toList())
        );
    }

}
