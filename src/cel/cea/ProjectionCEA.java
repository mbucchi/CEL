package cel.cea;

import cel.cea.transition.Transition;
import cel.query.ProjectionList;

import static cel.cea.transition.TransitionType.WHITE;

public class ProjectionCEA extends CEA {
    public ProjectionCEA(CEA inner, ProjectionList projectionList) {
        super(inner);

        for (Transition transition : transitions) {
            if (!projectionList.containsAny(transition.getLabels())) {
                transition.setType(WHITE);
            }
        }
    }
}
