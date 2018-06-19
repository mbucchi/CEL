package cepl.cea;

import cepl.cea.utils.Transition;
import cepl.event.Label;
import cepl.event.EventSchema;

import java.util.*;

public class MinimizedCEA extends CEA {

    public MinimizedCEA(CEA toMinimize) {
        // calculate reachable states
        Map<Integer, Set<Integer>> reachableFrom = createStatesReachableMap(toMinimize);

        // useful states
        Set<Integer> reachableFromQ0 = reachableFrom.get(0);
        Set<Integer> usefulStates = new HashSet<>();

        for (int q: reachableFromQ0){
            Set<Integer> reachableQ = reachableFrom.get(q);
            if (reachableQ.contains(toMinimize.finalState))
                usefulStates.add(q);
        }

        // renumber all useful states
        int[] newNames = new int[toMinimize.nStates];
        int newStateN = 0;

        for (int q = 0; q < toMinimize.nStates; q++){
            if (usefulStates.contains(q)){
                newNames[q] = newStateN++;
            }
        }

        // rename initial and final states
        int newInitState = newNames[toMinimize.initState];
        int newFinalState = newNames[toMinimize.finalState];


        // rename useless transitions and rename the remaining ones

        ArrayList<Transition> newTransitions = new ArrayList<>();
        Set<Label> newLabelSet = new HashSet<>();
        Set<EventSchema> newEventSchemas = new HashSet<>();


        for (Transition transition: toMinimize.transitions){
            if (usefulStates.contains(transition.getFromState()) && usefulStates.contains(transition.getToState())){

                int newFromState = newNames[transition.getFromState()];
                int newToState = newNames[transition.getToState()];
                Transition newTransition = transition
                        .replaceFromState(newFromState)
                        .replaceToState(newToState);
                newTransitions.add(newTransition);

                newLabelSet.addAll(transition.getLabels());
                newEventSchemas.add(transition.getEventSchema());
            }
        }

        // update automata
        nStates = newStateN;
        initState = newInitState;
        finalState = newFinalState;
        transitions = newTransitions;
        labelSet = newLabelSet;
        eventSchemas = newEventSchemas;
    }

    private static Map<Integer, Set<Integer>> createStatesReachableMap(CEA cea){

        Map<Integer, Set<Integer>> reachableFrom = new HashMap<>();
        for (int q = 0; q < cea.nStates; q++){
            Set<Integer> set = new HashSet<>();
            set.add(q);
            reachableFrom.put(q, set);
        }

        for (Transition t: cea.transitions)
            reachableFrom.get(t.getFromState()).add(t.getToState());

        boolean updated = true;

        while (updated){
            // try to add new reachable states for each state
            updated = false;
            for (int p = 0; p < cea.nStates; p++){
                Set<Integer> reachableP = reachableFrom.get(p);
                Set<Integer> newReachable = new HashSet<>();
                for (int q: reachableP){
                    newReachable.addAll(reachableFrom.get(q));
                }
                updated = updated || reachableP.addAll(newReachable);
            }
        }

        return reachableFrom;
    }

}
