package cel.runtime.source.utils;

import cel.cea.predicate.Predicate;
import cel.cea.transition.Transition;
import cel.filter.EventFilter;

import java.util.*;

public class BitMapTransition {

    private BitSet ANDMask;
    private BitSet ANDResult;
    private Integer fromState;
    private HashSet<Integer> toStates;
    private List<Object> filterOrder;

    public BitMapTransition(Transition t, List<Object> filterOrder) {
        this.fromState = t.getFromState();
        this.toStates = new HashSet<>(){{add(t.getToState());}};
        this.filterOrder = filterOrder;
        makeAndFilters(t.getPredicate());
    }

    public HashSet<Integer> getToState() {
        return toStates;
    }

    public Integer getFromState() {
        return fromState;
    }

    public BitSet getANDMask() {
        return ANDMask;
    }

    public BitSet getANDResult() {
        return ANDResult;
    }

    private void makeAndFilters(Predicate p) {
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

        ANDMask = transitionANDBitMask;
        ANDResult = transitionANDBitResult;
    }

    public boolean almostEqual(BitMapTransition other) {
        return this.getANDMask().equals(other.getANDMask()) && this.getANDResult().equals(other.getANDResult()) && this.getFromState().equals(other.getFromState());
    }

    public void addToState(Set<Integer> i) {
        toStates.addAll(i);
    }
}