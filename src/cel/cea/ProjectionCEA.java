package cel.cea;

import cel.cea.utils.Transition;
import cel.query.ProjectionList;

import static cel.cea.utils.TransitionType.WHITE;

public class ProjectionCEA extends CEA {
    public ProjectionCEA(CEA inner, ProjectionList projectionList){
        super(inner);

        for (Transition transition : transitions) {
            if (!projectionList.containsAny(transition.getLabels())){
                transition.setType(WHITE);
            }
        }
    }
}
