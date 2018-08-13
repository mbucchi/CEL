package cel.runtime;

import cel.runtime.cea.ExecutableCEA;

import java.util.*;

public class CELTraverser {

    private ExecutableCEA cea;
    private Integer newStateNumber = 0;

    private List<Set<Integer>> integerToSet;
    private Map<Set<Integer>, Integer> setToInteger;

    private List<MaxTuple> integerToTuple;
    private Map<MaxTuple, Integer> tupleToInteger;

    private List<BitSet> blackMasks;
    private List<BitSet> whiteMasks;

    private List<Map<BitSet, Integer>> knownBlackTransitions;
    private List<Map<BitSet, Integer>> knownWhiteTransitions;

    private static final int REJECT = -1;

    public CELTraverser(ExecutableCEA cea) {
        this.cea = cea;
        integerToSet = new ArrayList<>();
        setToInteger = new HashMap<>();

        integerToTuple = new ArrayList<>();
        tupleToInteger = new HashMap<>();


        blackMasks = new ArrayList<>();
        whiteMasks = new ArrayList<>();

        knownBlackTransitions = new ArrayList<>();
        knownWhiteTransitions = new ArrayList<>();

        knownBlackTransitions.add(new HashMap<>());
        knownWhiteTransitions.add(new HashMap<>());

        Set<Integer> start = new HashSet<>();
        start.add(0);

        integerToSet.add(start);
        setToInteger.put(start, 0);

        blackMasks.add(getBlackMask(start));
        whiteMasks.add(getWhiteMask(start));

        MaxTuple maxStart = new MaxTuple(start, null);
        integerToTuple.add(maxStart);
        tupleToInteger.put(maxStart, 0);

    }

    public List<Integer> nextStateDet(Integer state, BitSet vector) {
        List<Integer> ret = new ArrayList<>(2);
        Set<Integer> states = integerToSet.get(state);

        Integer toState = knownBlackTransitions.get(state).getOrDefault(vector, null);
        if (toState != null) {
            ret.add(toState);
        } else {
            Set<Integer> nextStates = new HashSet<>();
            for (Integer s : states) {
                nextStates.addAll(cea.blackTransition(s, vector));
            }

            Integer nextState = getStateName(nextStates);

            if (nextStates.isEmpty()) {
                knownBlackTransitions.get(state).put(vector, REJECT);
                ret.add(REJECT);
            } else {
                knownBlackTransitions.get(state).put(vector, nextState);
                ret.add(nextState);
            }
        }

        toState = knownWhiteTransitions.get(state).get(vector);
        if (toState != null) {
            ret.add(toState);
        } else {

            Set<Integer> nextStates = new HashSet<>();
            for (Integer s : states) {
                nextStates.addAll(cea.whiteTransition(s, vector));
            }

            Integer nextState = getStateName(nextStates);

            if (nextStates.isEmpty()) {
                knownWhiteTransitions.get(state).put(vector, REJECT);
                ret.add(REJECT);
            } else {
                knownWhiteTransitions.get(state).put(vector, nextState);
                ret.add(nextState);
            }
        }

        return ret;
    }

//    public Integer nextStateBlackDet(Integer state, BitSet vector) {
////        BitSet mask = blackMasks.get(state);
////        vector.and(mask);
//        Integer toState = knownBlackTransitions.get(state).getOrDefault(vector, null);
//        if (toState != null) {
//            return toState;
//        }
//
//        Set<Integer> states = integerToSet.get(state);
//
//        Set<Integer> nextStates = new HashSet<>();
//        for (Integer s : states) {
//            nextStates.addAll(cea.blackTransition(s, vector));
//        }
//
//        Integer nextState = getStateName(nextStates);
//
//        if (nextStates.isEmpty()) {
//            knownBlackTransitions.get(state).put(vector, REJECT);
//            return REJECT;
//        }
//
//        knownBlackTransitions.get(state).put(vector, nextState);
//        return nextState;
//    }
//
//    public Integer nextStateWhiteDet(Integer state, BitSet vector) {
////        BitSet mask = whiteMasks.get(state);
////        vector.and(mask);
//        Integer toState = knownWhiteTransitions.get(state).get(vector);
//        if (toState != null) {
//            return toState;
//        }
//
//        Set<Integer> states = integerToSet.get(state);
//
//        Set<Integer> nextStates = new HashSet<>();
//        for (Integer s : states) {
//            nextStates.addAll(cea.whiteTransition(s, vector));
//        }
//
//        Integer nextState = getStateName(nextStates);
//
//        if (nextStates.isEmpty()) {
//            knownWhiteTransitions.get(state).put(vector, REJECT);
//            return REJECT;
//        }
//        knownWhiteTransitions.get(state).put(vector, nextState);
//        return nextState;
//    }

    public List<Integer> nextStateMAX(Integer state, BitSet vector) {
        List<Integer> ret = new ArrayList<>(2);
        Integer toState = knownBlackTransitions.get(state).get(vector);
        if (toState != null) {
            ret.add(toState);
            ret.add(knownWhiteTransitions.get(state).get(vector));
            return ret;
        }

        MaxTuple tuple = integerToTuple.get(state);

        Set<Integer> TB = blackTransitionSet(tuple.T, vector);
        Set<Integer> UB = blackTransitionSet(tuple.U, vector);
        Set<Integer> TW = whiteTransitionSet(tuple.T, vector);
        Set<Integer> UW = whiteTransitionSet(tuple.U, vector);

        Set<Integer> newTB = new HashSet<>(TB);
        newTB.removeAll(UB);

        if (newTB.isEmpty()) {
            knownBlackTransitions.get(state).put(vector, REJECT);
            ret.add(REJECT);
        } else {
            MaxTuple newTup = new MaxTuple(newTB, UB);
            Integer nextState = getStateName(newTup);
            knownBlackTransitions.get(state).put(vector, nextState);
            ret.add(nextState);
        }

        Set<Integer> newUW = new HashSet<>(UB);
        newUW.addAll(UW);
        newUW.addAll(TB);
        TW.removeAll(newUW);

        if (TW.isEmpty()) {
            knownWhiteTransitions.get(state).put(vector, REJECT);
            ret.add(REJECT);
        } else {
            MaxTuple newTup = new MaxTuple(TW, newUW);
            Integer nextState = getStateName(newTup);
            knownWhiteTransitions.get(state).put(vector, nextState);
            ret.add(nextState);
        }
        return ret;
    }

//    public Integer nextStateBlackMAX(Integer state, BitSet vector) {
////        BitSet mask = blackMasks.get(state);
////        vector.and(mask);
//        Integer toState = knownBlackTransitions.get(state).get(vector);
//        if (toState != null) {
//            return toState;
//        }
//        MaxTuple tuple = integerToTuple.get(state);
//
//
//        Set<Integer> TB = blackTransitionSet(tuple.T, vector);
//        Set<Integer> UB = blackTransitionSet(tuple.U, vector);
//
//        Set<Integer> newTB = new HashSet<>(TB);
//        newTB.removeAll(UB);
//
//        if (newTB.isEmpty()) {
//            knownBlackTransitions.get(state).put(vector, REJECT);
//            return REJECT;
//        }
//
//        MaxTuple newTup = new MaxTuple(newTB, UB);
//        Integer nextState = getStateName(newTup);
//        knownBlackTransitions.get(state).put(vector, nextState);
//        return nextState;
//    }
//
//    public Integer nextStateWhiteMAX(Integer state, BitSet vector) {
////        BitSet mask = whiteMasks.get(state);
////        vector.and(mask);
//        Integer toState = knownWhiteTransitions.get(state).get(vector);
//        if (toState != null) {
//            return toState;
//        }
//        MaxTuple tuple = integerToTuple.get(state);
//
//
//        Set<Integer> TB = blackTransitionSet(tuple.T, vector);
//        Set<Integer> UB = blackTransitionSet(tuple.U, vector);
//        Set<Integer> TW = whiteTransitionSet(tuple.T, vector);
//        Set<Integer> UW = whiteTransitionSet(tuple.U, vector);
//        Set<Integer> newUW = new HashSet<>(UB);
//        newUW.addAll(UW);
//        newUW.addAll(TB);
//        TW.removeAll(newUW);
//
//        if (TW.isEmpty()) {
//            knownWhiteTransitions.get(state).put(vector, REJECT);
//            return REJECT;
//        }
//
//        MaxTuple newTup = new MaxTuple(TW, newUW);
//        Integer nextState = getStateName(newTup);
//        knownWhiteTransitions.get(state).put(vector, nextState);
//        return nextState;
//    }

    public Integer nextStateBlackLast(Integer state, BitSet vector) {

        return -1;
    }

    public Integer nextStateWhiteLast(Integer state, BitSet vector) {
        return -1;
    }

    private Integer getStateName(Set<Integer> nextStates) {
        Integer newState = setToInteger.get(nextStates);
        if (newState == null) {
            newState = ++newStateNumber;
            setToInteger.put(nextStates, newState);
            integerToSet.add(nextStates);
            knownBlackTransitions.add(new HashMap<>());
            knownWhiteTransitions.add(new HashMap<>());
            blackMasks.add(getBlackMask(nextStates));
            whiteMasks.add(getWhiteMask(nextStates));
            System.out.println(integerToSet.toString());
        }
        return newState;
    }

    private Integer getStateName(MaxTuple nextStates) {
        Integer newState;
        if (tupleToInteger.containsKey(nextStates)) {
            newState = tupleToInteger.get(nextStates);
        } else {
            newState = ++newStateNumber;
            tupleToInteger.put(nextStates, newState);
            integerToTuple.add(nextStates);
            knownBlackTransitions.add(new HashMap<>());
            knownWhiteTransitions.add(new HashMap<>());
            blackMasks.add(getBlackMask(nextStates));
            whiteMasks.add(getWhiteMask(nextStates));
            System.out.println(integerToTuple.toString());
            System.out.println(integerToTuple.size());
        }
        return newState;
    }

    public boolean isFinalDet(Integer state) {
        Set<Integer> states = integerToSet.get(state);
        for (Integer s : states) {
            if (cea.isFinal(s)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFinalMAX(Integer state) {
        Set<Integer> states = integerToTuple.get(state).T;
        for (Integer s : states) {
            if (cea.isFinal(s)) {
                return true;
            }
        }
        return false;
    }

    private Set<Integer> blackTransitionSet(Set<Integer> states, BitSet vector) {
        Set<Integer> ret = new HashSet<>();

        for (Integer state : states) {
            ret.addAll(cea.blackTransition(state, vector));
        }

        return ret;
    }

    private Set<Integer> whiteTransitionSet(Set<Integer> states, BitSet vector) {
        Set<Integer> ret = new HashSet<>();

        for (Integer state : states) {
            ret.addAll(cea.whiteTransition(state, vector));
        }

        return ret;
    }

    private BitSet getBlackMask(Set<Integer> states) {
        BitSet mask = new BitSet();

        for (Integer state : states) {
            mask.or(cea.getBlackTransitionMask(state));
        }

        return mask;
    }

    private BitSet getBlackMask(MaxTuple tuple) {
        BitSet mask = new BitSet();
        Set<Integer> states = new HashSet<>();

        states.addAll(tuple.T);
        states.addAll(tuple.U);
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

    private BitSet getWhiteMask(MaxTuple tuple) {
        BitSet mask = new BitSet();

        Set<Integer> states = new HashSet<>();

        states.addAll(tuple.T);
        states.addAll(tuple.U);

        for (Integer state : states) {
            mask.or(cea.getWhiteTransitionMask(state));
        }

        return mask;
    }

}
