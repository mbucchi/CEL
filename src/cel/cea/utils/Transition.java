package cel.cea.utils;

import cel.event.Label;
import cel.event.EventSchema;
import cel.filter.EventFilter;

import java.util.Collection;
import java.util.Set;

public class Transition implements Comparable<Transition> {

    private int fromState;
    private int toState;
    private Predicate predicate;
    private TransitionType transitionType;

    public Transition(int fromState, int toState, Predicate predicate, TransitionType transitionType) {
        this.fromState = fromState;
        this.toState = toState;
        this.predicate = predicate;
        this.transitionType = transitionType;
    }

    public Transition displaceTransition(int nStates) {
        return new Transition(fromState + nStates, toState + nStates, predicate, transitionType);
    }

    public Transition replaceToState(int toState){
        return new Transition(fromState, toState, predicate, transitionType);
    }

    public Transition replaceFromState(int fromState){
        return new Transition(fromState, toState, predicate, transitionType);
    }

    public int getFromState() {
        return fromState;
    }

    public int getToState() {
        return toState;
    }

    public boolean overLabel(Label label){
        return predicate.containsLabel(label);
    }

    public boolean isBlack(){
        return transitionType == TransitionType.BLACK;
    }

    public void addFilter(EventFilter filter){
        predicate.addFilter(filter);
    }

    public Collection<EventFilter> getFilters(){
        return predicate.getFilterCollection();
    }

    public void addLabel(Label label){
        predicate.addLabel(label);
    }

    public Transition copy() {
        return new Transition(fromState, toState, predicate.copy(), transitionType);
    }

    public TransitionType getType() {
        return transitionType;
    }

    public Set<Label> getLabels() {
        return predicate.getLabelSet();
    }

    public EventSchema getEventSchema() {
        return predicate.getEventSchema();
    }

    @Override
    public String toString() {
        return "Transition(" +
                transitionType.getSymbol() + ", " +
                fromState + ", " +
                toState + ", " +
                predicate.toString() +
                ")";
    }

    @Override
    public int compareTo(Transition o) {
        // Black < white
        if (transitionType != o.transitionType) {
            if (transitionType == TransitionType.BLACK) return -1;
            else return 1;
        }
        if (fromState < o.fromState) return -1;
        if (fromState > o.fromState) return 1;
        return Integer.compare(toState, o.toState);
    }
}