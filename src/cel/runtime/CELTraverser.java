package cel.runtime;

import cel.runtime.cea.ExecutableCEA;

import java.util.*;

class CELTraverser {

    private ExecutableCEA cea;
    private Integer newStateNumber = 0;
    private boolean initialized = false;

    private List<Set<Integer>> integerToSet;
    private Map<Set<Integer>, Integer> setToInteger;

    private List<MaxTuple> integerToMAXTuple;
    private Map<MaxTuple, Integer> MAXTupleToInteger;

    private List<NEXTTuple> integerToNEXTTuple;
    private Map<NEXTTuple, Integer> NEXTTupleToInteger;

    private List<LASTTuple> integerToLASTTuple;
    private Map<LASTTuple, Integer> LASTTupleToInteger;

    private List<BitSet> blackMasks;
    private List<BitSet> whiteMasks;

    private List<Map<BitSet, Integer>> knownBlackTransitions;
    private List<Map<BitSet, Integer>> knownWhiteTransitions;

    private List<Boolean> finalStates;

    private static final Integer REJECT = -1;

    CELTraverser(ExecutableCEA cea) {
        this.cea = cea;

        blackMasks = new ArrayList<>();
        whiteMasks = new ArrayList<>();

        knownBlackTransitions = new ArrayList<>();
        knownWhiteTransitions = new ArrayList<>();

        finalStates = new ArrayList<>();

        if (cea.isFinal(0)) {
            finalStates.add(true);
        } else {
            finalStates.add(false);
        }

        knownBlackTransitions.add(new HashMap<>());
        knownWhiteTransitions.add(new HashMap<>());
    }

    void initALL() {
        checkInit();
        integerToSet = new ArrayList<>();
        setToInteger = new HashMap<>();

        Set<Integer> start = new HashSet<>();
        start.add(0);

        integerToSet.add(start);
        setToInteger.put(start, 0);

        blackMasks.add(getBlackMask(start));
        whiteMasks.add(getWhiteMask(start));
    }

    void initMAX() {
        checkInit();
        integerToMAXTuple = new ArrayList<>();
        MAXTupleToInteger = new HashMap<>();

        Set<Integer> start = new HashSet<>();
        start.add(0);

        MaxTuple maxStart = new MaxTuple(start, null);
        integerToMAXTuple.add(maxStart);
        MAXTupleToInteger.put(maxStart, 0);

        blackMasks.add(getBlackMask(start));
        whiteMasks.add(getWhiteMask(start));
    }

    void initNEXT() {
        checkInit();
        integerToNEXTTuple = new ArrayList<>();
        NEXTTupleToInteger = new HashMap<>();

        Set<Integer> start = new HashSet<>();
        start.add(0);

        NEXTTuple nextStart = new NEXTTuple(start, null);
        integerToNEXTTuple.add(nextStart);
        NEXTTupleToInteger.put(nextStart, 0);

        blackMasks.add(getBlackMask(start));
        whiteMasks.add(getWhiteMask(start));
    }

    void initLAST() {
        checkInit();
        integerToLASTTuple = new ArrayList<>();
        LASTTupleToInteger = new HashMap<>();

        Set<Integer> start = new HashSet<>();
        start.add(0);

        LASTTuple lastStart = new LASTTuple(start, null, null);
        integerToLASTTuple.add(lastStart);
        LASTTupleToInteger.put(lastStart, 0);

        blackMasks.add(getBlackMask(start));
        whiteMasks.add(getWhiteMask(start));
    }

    private void checkInit() {
//        if (initialized) {
//            throw
//        }
        initialized = true;
    }

    List<Integer> nextStateALL(Integer state, BitSet vector) {
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

            if (nextState == finalStates.size()) {
                addToFinals(nextStates);
            }

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

            if (nextState == finalStates.size()) {
                addToFinals(nextStates);
            }

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

    List<Integer> nextStateMAX(Integer state, BitSet vector) {
        List<Integer> ret = new ArrayList<>(2);
        Integer toState = knownBlackTransitions.get(state).get(vector);
        if (toState != null) {
            ret.add(toState);
            ret.add(knownWhiteTransitions.get(state).get(vector));
            return ret;
        }

        MaxTuple tuple = integerToMAXTuple.get(state);

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
            if (nextState == finalStates.size()) {
                addToFinals(newTB);
            }
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
            if (nextState == finalStates.size()) {
                addToFinals(TW);
            }
        }
        return ret;
    }

    List<Integer> nextStateNEXT(Integer state, BitSet vector) {
        List<Integer> ret = new ArrayList<>(2);
        Integer toState = knownBlackTransitions.get(state).get(vector);
        if (toState != null) {
            ret.add(toState);
            ret.add(knownWhiteTransitions.get(state).get(vector));
            return ret;
        }

        NEXTTuple tuple = integerToNEXTTuple.get(state);

        Set<Integer> TB = blackTransitionSet(tuple.T, vector);
        Set<Integer> UB = blackTransitionSet(tuple.U, vector);
        Set<Integer> TW = whiteTransitionSet(tuple.T, vector);
        Set<Integer> UW = whiteTransitionSet(tuple.U, vector);

        Set<Integer> newUB = new HashSet<>(UB);
        newUB.addAll(UW);
        Set<Integer> newTB = new HashSet<>(TB);
        newTB.removeAll(newUB);

        if (newTB.isEmpty()) {
            knownBlackTransitions.get(state).put(vector, REJECT);
            ret.add(REJECT);
        } else {
            NEXTTuple newTup = new NEXTTuple(newTB, newUB);
            Integer nextState = getStateName(newTup);
            knownBlackTransitions.get(state).put(vector, nextState);
            ret.add(nextState);
            if (nextState == finalStates.size()) {
                addToFinals(newTB);
            }
        }

        Set<Integer> newUW = new HashSet<>(UB);
        newUW.addAll(UW);
        newUW.addAll(TB);
        TW.removeAll(newUW);

        if (TW.isEmpty()) {
            knownWhiteTransitions.get(state).put(vector, REJECT);
            ret.add(REJECT);
        } else {
            NEXTTuple newTup = new NEXTTuple(TW, newUW);
            Integer nextState = getStateName(newTup);
            knownWhiteTransitions.get(state).put(vector, nextState);
            ret.add(nextState);
            if (nextState == finalStates.size()) {
                addToFinals(TW);
            }
        }
        return ret;
    }

    List<Integer> nextStateLAST(Integer state, BitSet vector) {
        List<Integer> ret = new ArrayList<>(2);
        Integer toState = knownBlackTransitions.get(state).get(vector);

        if (toState != null) {
            ret.add(toState);
            ret.add(knownWhiteTransitions.get(state).get(vector));
            return ret;
        }

        LASTTuple tuple = integerToLASTTuple.get(state);

        /* read the paper appendix */

        Set<Integer> TB = blackTransitionSet(tuple.T, vector);
        Set<Integer> UB = blackTransitionSet(tuple.U, vector);
        Set<Integer> SB = blackTransitionSet(tuple.S, vector);
        Set<Integer> TW = whiteTransitionSet(tuple.T, vector);
        Set<Integer> UW = whiteTransitionSet(tuple.U, vector);
        Set<Integer> SW = whiteTransitionSet(tuple.S, vector);

        Set<Integer> newUB = new HashSet<>(UB);
        Set<Integer> newTB = new HashSet<>(TB);
        newTB.removeAll(newUB);
        Set<Integer> newSB = new HashSet<>(SB);
        newSB.addAll(SW);
        newSB.addAll(UW);
        newSB.addAll(TW);
        newSB.removeAll(newUB);
        newSB.removeAll(newTB);

        if (newTB.isEmpty()) {
            knownBlackTransitions.get(state).put(vector, REJECT);
            ret.add(REJECT);
        } else {
            LASTTuple newTup = new LASTTuple(newTB, newUB, newSB);
            Integer nextState = getStateName(newTup);
            knownBlackTransitions.get(state).put(vector, nextState);
            ret.add(nextState);
            if (nextState == finalStates.size()) {
                addToFinals(newTB);
            }
        }

        Set<Integer> newUW = new HashSet<>(UB);
        newUW.addAll(UW);
        newUW.addAll(TB);
        newUW.addAll(SB);
        Set<Integer> newTW = new HashSet<>(TW);
        newTW.removeAll(newUW);

        if (TW.isEmpty()) {
            knownWhiteTransitions.get(state).put(vector, REJECT);
            ret.add(REJECT);
        } else {
            LASTTuple newTup = new LASTTuple(newTW, newUW, SW);
            Integer nextState = getStateName(newTup);
            knownWhiteTransitions.get(state).put(vector, nextState);
            ret.add(nextState);
            if (nextState == finalStates.size()) {
                addToFinals(newTW);
            }
        }
        return ret;
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
//            System.out.println(integerToSet.toString());
        }
        return newState;
    }

    private Integer getStateName(MaxTuple nextStates) {
        Integer newState;
        if (MAXTupleToInteger.containsKey(nextStates)) {
            newState = MAXTupleToInteger.get(nextStates);
        } else {
            newState = ++newStateNumber;
            MAXTupleToInteger.put(nextStates, newState);
            integerToMAXTuple.add(nextStates);
            knownBlackTransitions.add(new HashMap<>());
            knownWhiteTransitions.add(new HashMap<>());
            //            blackMasks.add(getBlackMask(nextStates));
            //            whiteMasks.add(getWhiteMask(nextStates));
//                        System.out.println(integerToMAXTuple.toString());
//                        System.out.println(integerToMAXTuple.size());
        }
        return newState;
    }

    private Integer getStateName(NEXTTuple nextStates) {
        Integer newState;
        if (NEXTTupleToInteger.containsKey(nextStates)) {
            newState = NEXTTupleToInteger.get(nextStates);
        } else {
            newState = ++newStateNumber;
            NEXTTupleToInteger.put(nextStates, newState);
            integerToNEXTTuple.add(nextStates);
            knownBlackTransitions.add(new HashMap<>());
            knownWhiteTransitions.add(new HashMap<>());
            //            blackMasks.add(getBlackMask(nextStates));
            //            whiteMasks.add(getWhiteMask(nextStates));
            //            System.out.println(integerToTuple.toString());
            //            System.out.println(integerToTuple.size());
        }
        return newState;
    }

    private Integer getStateName(LASTTuple nextStates) {
        Integer newState;
        if (LASTTupleToInteger.containsKey(nextStates)) {
            newState = LASTTupleToInteger.get(nextStates);
        } else {
            newState = ++newStateNumber;
            LASTTupleToInteger.put(nextStates, newState);
            integerToLASTTuple.add(nextStates);
            knownBlackTransitions.add(new HashMap<>());
            knownWhiteTransitions.add(new HashMap<>());
            //            blackMasks.add(getBlackMask(nextStates));
            //            whiteMasks.add(getWhiteMask(nextStates));
//                        System.out.println(integerToLASTTuple.toString());
//                        System.out.println(integerToTuple.size());
        }
        return newState;
    }

    private void addToFinals(Set<Integer> stateSet) {
        for (Integer i : stateSet) {
            if (cea.isFinal(i)) {
                finalStates.add(true);
                return;
            }
        }
        finalStates.add(false);
    }

    boolean isFinal(Integer state) {
        return finalStates.get(state);
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
