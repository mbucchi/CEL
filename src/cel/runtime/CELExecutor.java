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

    private static final int REJECT = -1;

    public CELExecutor(ExecutableCEA cea) {
        this.cea = cea;
        integerToSet = new ArrayList<>();
        setToInteger = new HashMap<>();

        blackMasks = new ArrayList<>();
        whiteMasks = new ArrayList<>();

        knownBlackTransitions = new ArrayList<>();
        knownWhiteTransitions = new ArrayList<>();

        Set<Integer> start = new HashSet<>();
        start.add(0);
        integerToSet.add(start);
        setToInteger.put(start, 0);
    }

    public Integer nextStateBlack(Integer state, BitSet vector) {
        Set<Integer> states;
        if (blackMasks.size() > state) {
            BitSet mask = blackMasks.get(state);
            vector.and(mask);
            Integer toState = knownBlackTransitions.get(state).getOrDefault(vector, null);
            if (toState != null) {
                return toState;
            }
            states = integerToSet.get(state);
        } else {
            states = integerToSet.get(state);
            BitSet mask = getBlackMask(states);
            blackMasks.add(mask);
            vector.and(mask);
        }

        Set<Integer> nextStates = new HashSet<>();
        for (Integer s : states) {
            nextStates.addAll(cea.blackTransition(s, vector));
        }

        Integer nextState = getStateName(nextStates);

        if (nextStates.isEmpty()) {
            if (knownBlackTransitions.size() <= state) {
                knownBlackTransitions.add(new HashMap<>());
            }
            knownBlackTransitions.get(state).put(vector, REJECT);
            return REJECT;
        }

        if (knownBlackTransitions.size() <= state) {
            knownBlackTransitions.add(new HashMap<>());
        }
        knownBlackTransitions.get(state).put(vector, nextState);
        return nextState;
    }

    public Integer nextStateWhite(Integer state, BitSet vector) {
        Set<Integer> states;
        if (whiteMasks.size() > state) {
            BitSet mask = whiteMasks.get(state);
            vector.and(mask);
            Integer toState = knownWhiteTransitions.get(state).getOrDefault(vector, null);
            if (toState != null) {
                return toState;
            }
            states = integerToSet.get(state);
        } else {
            states = integerToSet.get(state);
            BitSet mask = getWhiteMask(states);
            whiteMasks.add(mask);
            vector.and(mask);
        }

        Set<Integer> nextStates = new HashSet<>();
        for (Integer s : states) {
            nextStates.addAll(cea.whiteTransition(s, vector));
        }

        Integer nextState = getStateName(nextStates);

        if (nextStates.isEmpty()) {
            if (knownWhiteTransitions.size() <= state) {
                knownWhiteTransitions.add(new HashMap<>());
            }

            knownWhiteTransitions.get(state).put(vector, REJECT);
            return REJECT;
        }

        if (knownWhiteTransitions.size() <= state) {
            knownWhiteTransitions.add(new HashMap<>());
        }
        knownWhiteTransitions.get(state).put(vector, nextState);
        return nextState;
    }


    private Integer getStateName(Set<Integer> nextStates) {
        Integer newState = setToInteger.getOrDefault(nextStates, null);
        if (newState == null) {
            newState = ++newStateNumber;
            setToInteger.put(nextStates, newState);
            integerToSet.add(nextStates);
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

    private BitSet getBlackMask(Set<Integer> states) {
        BitSet mask = new BitSet();

        for (Integer state : states) {
            mask.or(cea.getBlackTransitionMask(state));
        }

        return mask;
    }

    private BitSet getWhiteMask(Set<Integer> states) {
        BitSet mask = new BitSet();

        for (Integer state : states) {
            mask.or(cea.getWhiteTransitionMask(state));
        }

        return mask;
    }
}
