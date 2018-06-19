package cel.cea;

import cel.cea.utils.Transition;
import cel.event.Label;

public class AssignCEA extends CEA {
    public AssignCEA(CEA cea, Label label) {
        super(cea);
        for (Transition transition : transitions) {
            transition.addLabel(label);
        }
        labelSet.add(label);
    }
}
