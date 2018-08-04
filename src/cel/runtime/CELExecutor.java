package cel.runtime;

import cel.runtime.cea.ExecutableCEA;

import java.util.*;

public class CELExecutor {

    private ExecutableCEA cea;
    private Integer newStateNumber = 0;
    private Map<Integer, Set<Integer>> integerToSet;
    private Map<Set<Integer>, Integer> setToInteger;
    private Map<Integer, Map<BitSet, Integer>> knownBlackTransitions;
    private Map<Integer, Map<BitSet, Integer>> knownWhiteTransitions;

    public CELExecutor(ExecutableCEA cea) {
        this.cea = cea;
        integerToSet = new HashMap<>();
        setToInteger = new HashMap<>();
        Set<Integer> start = new HashSet<>();
        knownBlackTransitions = new HashMap<>();
        knownWhiteTransitions = new HashMap<>();
        start.add(0);
        integerToSet.put(0, start);
        setToInteger.put(start, 0);
    }

    public Integer nextStateBlack(Integer state, BitSet vector) {
        if (knownBlackTransitions.containsKey(state) && knownBlackTransitions.get(state).containsKey(vector)) {
            return knownBlackTransitions.get(state).get(vector);
        }

        Set<Integer> states = integerToSet.get(state);
        Set<Integer> nextStates = new HashSet<>();
        for (Integer s : states) {
            nextStates.addAll(cea.blackTransition(s, vector));
        }

        if (nextStates.isEmpty()) {
            return null;
        }

        Integer newState = getNewStateName(nextStates);

        if (!knownBlackTransitions.containsKey(state)) {
            knownBlackTransitions.put(state, new HashMap<>());
        }

        knownBlackTransitions.get(state).put(vector, newState);
        return newState;
    }

    public Integer nextStateWhite(Integer state, BitSet vector) {
        if (knownWhiteTransitions.containsKey(state) && knownWhiteTransitions.get(state).containsKey(vector)) {
            return knownWhiteTransitions.get(state).get(vector);
        }

        Set<Integer> states = integerToSet.get(state);
        Set<Integer> nextStates = new HashSet<>();
        for (Integer s : states) {
            nextStates.addAll(cea.whiteTransition(s, vector));
        }

        if (nextStates.isEmpty()) {
            return null;
        }

        Integer newState = getNewStateName(nextStates);

        if (!knownWhiteTransitions.containsKey(state)) {
            knownWhiteTransitions.put(state, new HashMap<>());
        }

        knownWhiteTransitions.get(state).put(vector, newState);
        return newState;
    }

    private Integer getNewStateName(Set<Integer> nextStates) {
        Integer newState;
        if (setToInteger.containsKey(nextStates)) {
            newState = setToInteger.get(nextStates);
        } else {
            newState = newStateNumber++;
            setToInteger.put(nextStates, newState);
            integerToSet.put(newState, nextStates);
        }
        return newState;
    }

}
