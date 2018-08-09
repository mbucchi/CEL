package cel.runtime;

import cel.runtime.cea.ExecutableCEA;

import java.util.*;

public class CELExecutor {

    private ExecutableCEA cea;
    private Integer newStateNumber = 0;

    private List<Set<Integer>> integerToSet;
    private Map<Set<Integer>, Integer> setToInteger;

    private List<BitSet> blackMasks;
    private List<BitSet> whiteMasks;

    private List<Map<BitSet, Integer>> knownBlackTransitions;
    private List<Map<BitSet, Integer>> knownWhiteTransitions;

    public CELExecutor(ExecutableCEA cea) {
        this.cea = cea;
        integerToSet = new ArrayList<>();
        setToInteger = new HashMap<>();
        knownBlackTransitions = new ArrayList<>();
        knownWhiteTransitions = new ArrayList<>();

        Set<Integer> start = new HashSet<>();
        start.add(0);
        integerToSet.add(start);
        setToInteger.put(start, 0);
    }

    public Integer nextStateBlack(Integer state, BitSet vector) {
        /* agregar mask y optimizar acceso */
        if (knownBlackTransitions.get(state).containsKey(vector)) {
            return knownBlackTransitions.get(state).get(vector);
        }

        Set<Integer> states = integerToSet.get(state);
        Set<Integer> nextStates = new HashSet<>();
        for (Integer s : states) {
            nextStates.addAll(cea.blackTransition(s, vector));
        }

        if (nextStates.isEmpty()) {
            /* guardar esto */
//            knownBlackTransitions.put(state, new HashMap<>(){{put(vector, -1);}});
            return -1;
        }

        Integer newState = getStateName(nextStates);

//        if (!knownBlackTransitions.containsKey(state)) {
//            knownBlackTransitions.put(state, new HashMap<>());
//        }

        knownBlackTransitions.get(state).put(vector, newState);
        return newState;
    }

    public Integer nextStateWhite(Integer state, BitSet vector) {
//        if (knownWhiteTransitions.containsKey(state) && knownWhiteTransitions.get(state).containsKey(vector)) {
//            return knownWhiteTransitions.get(state).get(vector);
//        }

        Set<Integer> states = integerToSet.get(state);
        Set<Integer> nextStates = new HashSet<>();
        for (Integer s : states) {
            nextStates.addAll(cea.whiteTransition(s, vector));
        }

        if (nextStates.isEmpty()) {
            return null;
        }

        Integer newState = getStateName(nextStates);

//        if (!knownWhiteTransitions.containsKey(state)) {
//            knownWhiteTransitions.put(state, new HashMap<>());
//        }

        knownWhiteTransitions.get(state).put(vector, newState);
        return newState;
    }

    private Integer getStateName(Set<Integer> nextStates) {
        Integer newState;
        if (setToInteger.containsKey(nextStates)) {
            newState = setToInteger.get(nextStates);
        } else {
            newState = ++newStateNumber;
            setToInteger.put(nextStates, newState);
//            integerToSet.put(newState, nextStates);
            System.out.println(integerToSet.toString());
        }
        return newState;
    }

    public boolean isFinal(Integer state) {
        Set<Integer> states = integerToSet.get(state);
        for (Integer s : states) {
            if (cea.isFinal(s)) {
                return true;
            }
        }
        return false;
    }
}
