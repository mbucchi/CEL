package cel.runtime;

import cel.cea.CEA;
import cel.cea.predicate.Predicate;
import cel.cea.transition.Transition;
import cel.event.EventSchema;
import cel.filter.EventFilter;
import cel.runtime.utils.BitMapTransition;
import cel.stream.StreamSchema;

import java.util.*;

public class CEATraverser {

    private CEA cea;
    private ArrayList<Object> filterOrder;
    private Map<Object, Integer> filterRepetitions;
    private Map<Integer, ArrayList<BitMapTransition>> blackStateMap;
    private Map<Integer, ArrayList<BitMapTransition>> whiteStateMap;

    public CEATraverser(CEA cea) {
        this.cea = cea;
        filterOrder = new ArrayList<>();
        makeFilterOrder();
//        System.out.println(filterOrder);
        makeBitMapTransitions();
//        System.out.println("breakpoint");
    }

    public List<Integer> getNextState(Integer currentState, BitSet satisfiedFilters) {
        /* ret.get(0) holds the next state with a black transition,
           ret.get(1) holds the next state with a white transition.
          */
        ArrayList<Integer> ret = new ArrayList<>();

        ArrayList<BitMapTransition> blackTransitions = blackStateMap.get(currentState);
        getNextStateForColor(satisfiedFilters, ret, blackTransitions);

        ArrayList<BitMapTransition> whiteTransitions = whiteStateMap.get(currentState);
        getNextStateForColor(satisfiedFilters, ret, whiteTransitions);

        return ret;
    }

    private void getNextStateForColor(BitSet satisfiedFilters, ArrayList<Integer> ret, ArrayList<BitMapTransition> blackTransitions) {
        if (blackTransitions.isEmpty()) {
            ret.add(null);
        } else {
            for (BitMapTransition t : blackTransitions) {
                if (t.isSatisfiedBy(satisfiedFilters)) {
                    ret.add(t.getToState());
                    break;
                }
            }
        }
    }

    private void makeFilterOrder() {
        makeFilterRepetitionsMap();
        Comparator<Object> comp = (o1, o2) -> {
            if (o1.equals(o2)) {
                return 0;
            }
            if (filterRepetitions.get(o1).equals(filterRepetitions.get(o2))) {
                /* TODO: MAKE SORTING STABLE */
//                if ()
                return 0;
            }
            if (filterRepetitions.get(o1) > filterRepetitions.get(o2)) {
                return -1;
            }
            return 1;
        };

//        System.out.println(filterRepetitions);

        filterOrder.addAll(filterRepetitions.keySet());
        filterOrder.sort(comp);
    }

    private void makeFilterRepetitionsMap() {
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

    private void makeBitMapTransitions() {
        blackStateMap = new HashMap<>();
        whiteStateMap = new HashMap<>();
        for (int i = 0; i < cea.getnStates(); i++) {
            blackStateMap.put(i, new ArrayList<>());
            whiteStateMap.put(i, new ArrayList<>());
        }

        for (Transition t : cea.getTransitions()) {
            BitMapTransition newTransition = new BitMapTransition(t.getToState());
            Predicate p = t.getPredicate();
            if (p.hasChildren()) {
                for (Predicate child : p.getPredicates()) {
                    if (child.isNegated()) {
                        makeOrFilters(t, newTransition, child);
                    } else {
                        makeAndFilters(t, newTransition, child);
                    }
                }
            } else if (p.isNegated()){
                makeOrFilters(t, newTransition, p);
            } else {
                makeAndFilters(t, newTransition, p);
            }
        }
    }

    private void makeAndFilters(Transition t, BitMapTransition newTransition, Predicate p) {
        BitSet transitionANDBitMask = new BitSet(filterOrder.size());
        BitSet transitionANDBitResult = new BitSet(filterOrder.size());

        if (p.getEventSchema() != null) {
            int eventPos = filterOrder.indexOf(p.getEventSchema());
            transitionANDBitMask.set(eventPos);
            transitionANDBitResult.set(eventPos);
        }

        if (p.getStreamSchema() != null) {
            int streamPos = filterOrder.indexOf(p.getStreamSchema());
            transitionANDBitMask.set(streamPos);
            transitionANDBitResult.set(streamPos);
        }

        for (EventFilter f : p.getFilterCollection()) {
            int filterPos;
            if (filterOrder.contains(f)) {
                filterPos = filterOrder.indexOf(f);
                transitionANDBitResult.set(filterPos);
            } else {
                filterPos = filterOrder.indexOf(f.negate());
            }
            transitionANDBitMask.set(filterPos);
        }

        newTransition.addANDMask(transitionANDBitMask);
        newTransition.addANDResult(transitionANDBitResult);

        putTransition(t, newTransition);
    }

    private void makeOrFilters(Transition t, BitMapTransition newTransition, Predicate p) {
        BitSet transitionORBitMask = new BitSet(filterOrder.size());
        BitSet transitionORBitResult = new BitSet(filterOrder.size());

        if (p.getEventSchema() != null) {
            int eventPos = filterOrder.indexOf(p.getEventSchema());
            transitionORBitMask.set(eventPos);
        }

        if (p.getStreamSchema() != null) {
            int streamPos = filterOrder.indexOf(p.getStreamSchema());
            transitionORBitMask.set(streamPos);
        }

        for (EventFilter f : p.getFilterCollection()) {
            int filterPos;
            if (filterOrder.contains(f)) {
                filterPos = filterOrder.indexOf(f);
            } else {
                filterPos = filterOrder.indexOf(f.negate());
                transitionORBitResult.set(filterPos);
            }
            transitionORBitMask.set(filterPos);
        }

        newTransition.addORMask(transitionORBitMask);
        newTransition.addORResults(transitionORBitResult);

        putTransition(t, newTransition);
    }

    private void putTransition(Transition t, BitMapTransition newTransition) {
        if (t.isBlack()) {
            blackStateMap.get(t.getFromState()).add(newTransition);
        } else {
            whiteStateMap.get(t.getFromState()).add(newTransition);
        }
    }
}
