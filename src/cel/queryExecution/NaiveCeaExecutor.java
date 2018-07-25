package cel.queryExecution;

import cel.cea.CEA;
import cel.predicate.Predicate;
import cel.cea.transition.Transition;
import cel.event.Event;

public class NaiveCeaExecutor extends CeaExecutor {

    private CEA cea;

    public NaiveCeaExecutor(CEA cea) {
        this.cea = cea;
    }

    @Override
    void newValue(Event event) {

    }

    private boolean satisfiesTransition(Event event, Transition transition){
        Predicate predicate = transition.getPredicate();
        if (!predicate.overEvent(event.getSchema())) return false;
        return false;
    }

    int blackTransition(int state, Event event) {
        for (Transition transition: cea.getTransitions()) {
            if (!transition.isBlack()) continue;
        }
        return 0;
    }

    int whiteTransition(int state, Event event) {
        return 0;
    }
}
