package cepl.cea;

import cepl.cea.utils.TransitionType;
import cepl.cea.utils.Label;
import cepl.filter.EventFilter;

import java.util.Collection;

public class Transition {

    private int fromState;
    private int toState;
    private Predicate predicate;
    private TransitionType transitionType;

    Transition(int fromState, int toState, Predicate predicate, TransitionType transitionType) {
        this.fromState = fromState;
        this.toState = toState;
        this.predicate = predicate;
        this.transitionType = transitionType;
    }

    Transition displaceTransition(int nStates) {
        return new Transition(fromState + nStates, toState + nStates, predicate, transitionType);
    }

    Transition replaceToState(int toState){
        return new Transition(fromState, toState, predicate, transitionType);
    }

    Transition replaceFromState(int fromState){
        return new Transition(fromState, toState, predicate, transitionType);
    }

    int getFromState() {
        return fromState;
    }

    int getToState() {
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

    void addLabel(Label label){
        predicate.addLabel(label);
    }

    Transition copy() {
        return new Transition(fromState, toState, predicate.copy(), transitionType);
    }

    TransitionType getType() {
        return transitionType;
    }

    @Override
    public String toString() {
        return transitionType.toString() + "Transition(" +
                fromState +
                ", " +
                toState +
                ", " +
                predicate.toString() +
                ")";
    }
}