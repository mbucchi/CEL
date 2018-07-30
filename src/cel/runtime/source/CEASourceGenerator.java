package cel.runtime.source;

import cel.cea.CEA;
import cel.cea.predicate.Predicate;
import cel.cea.transition.Transition;
import cel.event.EventSchema;
import cel.filter.EventFilter;
import cel.runtime.source.utils.BitMapTransition;
import cel.stream.StreamSchema;

import java.util.*;

public class CEASourceGenerator {

    private static int WIDTH = 4;

    private CEA cea;
    private List<Object> bitVectorOrder;
    private Map<Object, Integer> filterRepetitions;
    private Map<Integer, List<BitMapTransition>> blackTransitionMap;
    private Map<Integer, List<BitMapTransition>> whiteTransitionMap;
    private Set<Integer> addedBitSets;

    public CEASourceGenerator(CEA cea) {
        this.cea = cea;
        makeFilterOrder(cea);
    }

    public List<Object> getBitVectorOrder() {
        return bitVectorOrder;
    }

    public String makeSourceCode() {
        StringBuilder src = new StringBuilder();

        src.append(makeHeaders());
        src.append("public class CEA implements ExecutableCEA {\n\n");
        src.append(makeVariables());
        src.append(makeInit());
        src.append(makeBlackTransitions());
        src.append(makeWhiteTransitions());
        src.append(makeIsFinal());
        src.append(makeGetNStates());
        src.append("}");
        System.out.println(src.toString());

        return src.toString();
    }

    private String makeHeaders() {
        StringBuilder ret = new StringBuilder();

        ret.append("package cel.runtime.cea;\n");
        ret.append("\n");
        ret.append("import cel.runtime.cea.ExecutableCEA;\n");
        ret.append("import java.util.BitSet;\n");
        ret.append("import java.util.HashSet;\n");
        ret.append("import java.util.Set;\n");
        ret.append("\n");

        return ret.toString();
    }

    private String makeVariables() {
        StringBuilder ret = new StringBuilder();

        ret.append(indent(1)).append("private int finalState = ").append(cea.getFinalState()).append(";\n");
        ret.append(indent(1)).append("private BitSet tb;\n");
        makeBitMapTransitions();
        addedBitSets = new HashSet<>();
        for (Integer i = 0; i < cea.getnStates(); i++) {
            makeBitSets(ret, i, blackTransitionMap);
            makeBitSets(ret, i, whiteTransitionMap);
        }
        ret.append("\n");

        return ret.toString();
    }

    private void makeBitSets(StringBuilder ret, Integer i, Map<Integer, List<BitMapTransition>> transitionMap) {
        if (!transitionMap.get(i).isEmpty()) {
            for (BitMapTransition t : transitionMap.get(i)) {
                makeBitSetString(ret, t, t.getANDMask());
                makeBitSetString(ret, t, t.getANDResult());
            }
        }
    }

    private void makeBitSetString(StringBuilder ret, BitMapTransition t, BitSet b) {
        StringBuilder longArr = new StringBuilder();
        int hash = Arrays.hashCode(b.toLongArray());
        if (!addedBitSets.contains(hash)) {
            addedBitSets.add(hash);
            ret.append(indent(1)).append("private BitSet bitSet");
            boolean first = true;
            for (Long l : b.toLongArray()) {
                if (first) {
                    first = false;
                } else {
                    longArr.append("L, ");
                }
                if (l >= 0) {
                    ret.append(l);
                } else {
                    ret.append("_").append(l * -1);
                }
                ret.append("L");
                longArr.append(l);
            }
            ret.append(" = BitSet.valueOf(");
            ret.append("new long[]{");
            ret.append(longArr.toString());
            if (longArr.toString().isEmpty()) {
                ret.append("});\n");
            } else {
                ret.append("L});\n");
            }
        }
    }

    private String makeInit() {
        StringBuilder ret = new StringBuilder();

        ret.append(indent(1)).append("public CEA() {\n");
        ret.append(indent(1)).append("}\n\n");

        return ret.toString();
    }

    private void makeBitMapTransitions() {
        blackTransitionMap = new HashMap<>();
        whiteTransitionMap = new HashMap<>();

        for (int i = 0; i < cea.getnStates(); i++) {
            blackTransitionMap.put(i, new ArrayList<>());
            whiteTransitionMap.put(i, new ArrayList<>());
        }

        for (Transition t : cea.getTransitions()) {
            BitMapTransition newTransition = new BitMapTransition(t, bitVectorOrder);
            if (t.isBlack()) {
                if (notMergeable(newTransition, t.getFromState(), blackTransitionMap)) {
                    blackTransitionMap.get(t.getFromState()).add(newTransition);
                }
            } else {
                if (notMergeable(newTransition, t.getFromState(), whiteTransitionMap)) {
                    whiteTransitionMap.get(t.getFromState()).add(newTransition);
                }
            }
        }
    }

    private boolean notMergeable(BitMapTransition t, Integer i, Map<Integer, List<BitMapTransition>> transitionMap) {
        for (BitMapTransition bt : transitionMap.get(i)) {
            if (bt.almostEqual(t)) {
                bt.addToState(t.getToState());
                return false;
            }
        }
        return true;
    }

    private String makeBlackTransitions() {
        StringBuilder ret = new StringBuilder();

        ret.append(indent(1)).append("public Set<Integer> blackTransition(Integer state, BitSet b) {\n");
        makeTransitions(ret, blackTransitionMap);

        ret.append("\n");
        ret.append(indent(2)).append("return toStates;\n");
        ret.append(indent(1)).append("}\n\n");
        return ret.toString();
    }

    private String makeWhiteTransitions() {
        StringBuilder ret = new StringBuilder();

        ret.append(indent(1)).append("public Set<Integer> whiteTransition(Integer state, BitSet b) {\n");
        makeTransitions(ret, whiteTransitionMap);

        ret.append("\n");
        ret.append(indent(2)).append("return toStates;\n");
        ret.append(indent(1)).append("}\n\n");
        return ret.toString();
    }

    private void makeTransitions(StringBuilder ret, Map<Integer, List<BitMapTransition>> transitionMap) {
        ret.append(indent(2)).append("Set<Integer> toStates = new HashSet<>();\n");
        boolean first = true;
        for (Integer i = 0; i < cea.getnStates(); i++) {
            if (transitionMap.get(i).isEmpty()) {
                continue;
            }
            if (first) {
                first = false;
                ret.append(indent(2)).append("if (state.equals(").append(i).append(")) {\n");
            } else {
                ret.append("else if (state.equals(").append(i).append(")) {\n");
            }
            for (BitMapTransition t : transitionMap.get(i)) {
                if (t.getANDMask().isEmpty()) {
                    for (Integer j : t.getToState()) {
                        ret.append(indent(3)).append("toStates.add(").append(j).append(");\n");
                    }
                } else {
                    ret.append(indent(3)).append("tb = (BitSet) b.clone();\n");
                    ret.append(indent(3)).append("tb.and(bitSet");
                    for (Long l : t.getANDMask().toLongArray()) {
                        if (l >= 0) {
                            ret.append(l);
                        } else {
                            ret.append("_").append(l * -1);
                        }
                        ret.append("L");
                    }
                    ret.append(");\n");
                    ret.append(indent(3)).append("tb.xor(bitSet");
                    for (Long l : t.getANDResult().toLongArray()) {
                        if (l >= 0) {
                            ret.append(l);
                        } else {
                            ret.append("_").append(l * -1);
                        }
                        ret.append("L");
                    }
                    ret.append(");\n");
                    ret.append(indent(3)).append("if (tb.isEmpty()) {\n");
                    for (Integer j : t.getToState()) {
                        ret.append(indent(4)).append("toStates.add(").append(j).append(");\n");
                    }
                    ret.append(indent(3)).append("}\n");
                }
            }
            ret.append(indent(2)).append("} ");
        }
    }

    private String makeIsFinal() {
        StringBuilder ret = new StringBuilder();

        ret.append(indent(1)).append("public boolean isFinal(Integer state) {\n");
        ret.append(indent(2)).append("return state.equals(this.finalState);\n");
        ret.append(indent(1)).append("}\n\n");

        return ret.toString();
    }

    private String makeGetNStates() {
        StringBuilder ret = new StringBuilder();
        ret.append(indent(1)).append("public Integer getNStates() {\n");
        ret.append(indent(2)).append("return ").append(cea.getnStates()).append(";\n");
        ret.append(indent(1)).append("}\n\n");

        return ret.toString();
    }

    private String indent(int level) {
        return new String(new char[level * WIDTH]).replace("\0", " ");
    }

    private void makeFilterOrder(CEA cea) {
        makeFilterRepetitionsMap(cea);
        Comparator<Object> comp = (o1, o2) -> {
            if (o1.equals(o2)) {
                return 0;
            }
            if (filterRepetitions.get(o1).equals(filterRepetitions.get(o2))) {
                return 0;
            }
            if (filterRepetitions.get(o1) > filterRepetitions.get(o2)) {
                return -1;
            }
            return 1;
        };

        bitVectorOrder = new ArrayList<>();
        bitVectorOrder.addAll(filterRepetitions.keySet());
        bitVectorOrder.sort(comp);
    }

    private void makeFilterRepetitionsMap(CEA cea) {
        filterRepetitions = new HashMap<>();

        for (Transition t : cea.getTransitions()) {
            Predicate p = t.getPredicate();
            if (p.hasChildren()) {
                for (Predicate child : p.getPredicates()) {
                    putStreamSchema(child.getStreamSchema());
                    putEventSchema(child.getEventSchema());
                    /* Predicates should not have repeated filters inside */
                    for (EventFilter e : child.getFilterCollection()) {
                        putEventFilter(e);
                    }
                }
            } else {
                putStreamSchema(p.getStreamSchema());
                putEventSchema(p.getEventSchema());
                for (EventFilter e : p.getFilterCollection()) {
                    putEventFilter(e);
                }
            }
        }
    }

    private void putEventFilter(EventFilter e) {
        if (filterRepetitions.containsKey(e)) {
            filterRepetitions.put(e, filterRepetitions.get(e) + 1);
        } else if (filterRepetitions.containsKey(e.negate())) {
            filterRepetitions.put(e.negate(), filterRepetitions.get(e.negate()) + 1);
        } else {
            filterRepetitions.put(e, 1);
        }
    }

    private void putStreamSchema(StreamSchema s) {
        if (s == null) {
            return;
        }
        if (filterRepetitions.containsKey(s)) {
            filterRepetitions.put(s, filterRepetitions.get(s) + 1);
        } else {
            filterRepetitions.put(s, 1);
        }
    }

    private void putEventSchema(EventSchema e) {
        if (e == null) {
            return;
        }
        if (filterRepetitions.containsKey(e)) {
            filterRepetitions.put(e, filterRepetitions.get(e) + 1);
        } else {
            filterRepetitions.put(e, 1);
        }
    }
}
