package cepl.cea;

import cepl.cea.utils.Label;

public class AssignCEA extends CEA {
    public AssignCEA(CEA cea, Label label) {
        super(cea);
        for (Transition transition : transitions) {
            transition.addLabel(label);
        }
        labelSet.add(label);
    }
}
