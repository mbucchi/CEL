package cel.cea.transition;

import cel.event.Label;
import cel.predicate.BitPredicate;

import java.util.HashSet;
import java.util.Set;

public class Transition implements Comparable<Transition> {

    private int fromState;
    private int toState;
    private TransitionType transitionType;
    private Set<Label> labels;
    private BitPredicate bitPredicate;

    private Transition(Transition toCopy) {
        fromState = toCopy.fromState;
        toState = toCopy.toState;
        transitionType = toCopy.transitionType;
        labels = new HashSet<>(toCopy.labels);
        bitPredicate = toCopy.bitPredicate.copy();
    }

//    public Transition(int fromState, TransitionType transitionType) {
//        this.fromState = fromState;
//        this.transitionType = transitionType;
//    }

    public Transition(int fromState, int toState, BitPredicate bitPredicate, TransitionType transitionType) {
        this.fromState = fromState;
        this.toState = toState;
        this.transitionType = transitionType;
        this.bitPredicate = bitPredicate;
        labels = new HashSet<>();
    }

    public Transition(int fromState, int toState, BitPredicate bitPredicate, Label label, TransitionType transitionType) {
        this(fromState, toState, bitPredicate, new HashSet<>(Set.of(label)), transitionType);
        labels.add(label);
    }


    public Transition(int fromState, int toState, BitPredicate bitPredicate, Set<Label> labels, TransitionType transitionType) {
        this(fromState, toState, bitPredicate, transitionType);
        this.labels = new HashSet<>(labels);
    }

    public Transition displaceTransition(int nStates) {
        return new Transition(fromState + nStates, toState + nStates, bitPredicate, labels, transitionType);
    }

    public Transition replaceToState(int toState) {
        return new Transition(fromState, toState, bitPredicate, labels, transitionType);
    }

    public Transition replaceFromState(int fromState) {
        return new Transition(fromState, toState, bitPredicate, labels, transitionType);
    }

    public int getFromState() {
        return fromState;
    }

    public int getToState() {
        return toState;
    }

    public boolean overLabel(Label label) {
        return labels.contains(label);
    }

    public boolean isBlack() {
        return transitionType == TransitionType.BLACK;
    }

    public void addLabel(Label label) {
        labels.add(label);
    }

    public Transition copy() {
        return new Transition(this);
    }

    public TransitionType getType() {
        return transitionType;
    }

    public void setType(TransitionType transitionType) {
        this.transitionType = transitionType;
    }

    public Set<Label> getLabels() {
        return labels;
    }

    public Transition addPredicate(BitPredicate bitPredicate){
        Transition newTransition = copy();
        newTransition.bitPredicate = this.bitPredicate.cojoin(bitPredicate);
        return newTransition;
    }
//    public EventSchema getEventSchema() {
//        return predicate.getEventSchema();
//    }

    @Override
    public String toString() {
        return "Transition(" +
                transitionType.getSymbol() + ", " +
                fromState + ", " +
                toState + ", " +
                bitPredicate.toString() +
                ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Transition)) return false;
        Transition other = (Transition)obj;
        return (other.bitPredicate.equals(bitPredicate)
                && other.fromState == fromState
                && other.toState == toState
                && other.transitionType == transitionType);
    }

    @Override
    public int hashCode() {
        return bitPredicate.hashCode() * 31 + transitionType.hashCode() * 17 + toState * 13 + fromState * 11;
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