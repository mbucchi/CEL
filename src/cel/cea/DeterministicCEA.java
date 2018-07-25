package cel.cea;

import cel.predicate.AndPredicate;
import cel.predicate.Predicate;
import cel.cea.transition.Transition;
import cel.cea.transition.TransitionType;

import java.util.*;

import static cel.cea.transition.TransitionType.BLACK;
import static cel.cea.transition.TransitionType.WHITE;


public class DeterministicCEA extends CEA {

    private static final int INITIAL_STATE = 0;
    private static final int FIRST = 0;

    private Set<Integer> addedStates;
    private Map<Integer, Integer> newStatesMap = new HashMap<>();
    private Set<List<Integer>> statesLeft;
    private ArrayList<Transition> newTransitions;
    private Integer fromState;
    private Map<List<Integer>, Integer> newStateNameMap = new HashMap<>();
    private Set<Integer> newFinalStates = new HashSet<>();

    private Map<Integer, Set<Transition>> reachableFromWithBlack;
    private Map<Integer, Set<Transition>> reachableFromWithWhite;

    public DeterministicCEA(CEA toDeterminize) {

//        System.out.println(toDeterminize.toString());
        long compileTime = System.nanoTime();

        /* Temporarily set finalstates to toDeterminize's final states */
        finalStates = toDeterminize.finalStates;

        addedStates = new HashSet<>();

        /* statesLeft represents the list of states left to check */
        statesLeft = new HashSet<>();
        List<Integer> initialList = new ArrayList<>();
        initialList.add(INITIAL_STATE);
        statesLeft.add(initialList);

        /* we get the transition set for each state */
        reachableFromWithBlack = createTransitionMap(toDeterminize, BLACK);
        reachableFromWithWhite = createTransitionMap(toDeterminize, WHITE);
        List<Transition> usefulBlackTransitions = new ArrayList<>();
        List<Transition> usefulWhiteTransitions = new ArrayList<>();

        newTransitions = new ArrayList<>();
        List<Integer> current;
        while (statesLeft.size() > 0) {

            current = statesLeft.iterator().next();
            statesLeft.remove(current);

            /* states are now lists, and are represented as 2^state_0 + ... + 2^state_j - 1 */
            fromState = getNewStateNumber(current);
            if (addedStates.contains(fromState)) {
                continue;
            }
            addedStates.add(fromState);

            usefulBlackTransitions.clear();
            usefulWhiteTransitions.clear();

            /* get all reachable states from current state set */
            for (Integer state : current) {
                usefulBlackTransitions.addAll(reachableFromWithBlack.get(state));
                usefulWhiteTransitions.addAll(reachableFromWithWhite.get(state));
            }

            /* TODO: FIND A BETTER WAY TO DO THIS */
            /* get all possible transitions combinations */
            for (int i = 1; i <= usefulBlackTransitions.size(); i++) {
                for (List<Transition> currentTransitionList : getCombinations(usefulBlackTransitions, i)) {
                    makeNewTransition(usefulBlackTransitions, currentTransitionList, BLACK);
                }
            }

            for (int i = 1; i <= usefulWhiteTransitions.size(); i++) {
                for (List<Transition> currentTransitionList : getCombinations(usefulWhiteTransitions, i)) {
                    makeNewTransition(usefulWhiteTransitions, currentTransitionList, WHITE);
                }
            }
        }

        newTransitions.sort(Transition::compareTo);
        transitions = newTransitions;
        compileTime = System.nanoTime() - compileTime;
        mergeTransitions();
        System.out.println("Determinization time: " + ((double) compileTime / 1000000000));
        labelSet = toDeterminize.labelSet;
        eventSchemas = toDeterminize.eventSchemas;
        finalStates = newFinalStates;
    }

    private void makeNewTransition(List<Transition> usefulTransitions, List<Transition> currentTransitionList, TransitionType color) {

        /* currentTransitionList holds which transitions will be true */
        Set<Integer> toStates = new HashSet<>();
        Collection<Predicate> predicates = new ArrayList<>();

        for (Transition currentTransition : usefulTransitions) {
            if (currentTransitionList.contains(currentTransition)) {
                predicates.add(currentTransition.getPredicate());
                toStates.add(currentTransition.getToState());
            }
            else {
                predicates.add(currentTransition.getPredicate().negate());
            }
        }

        if (!Predicate.overSameStreamAndEvent(predicates)){
            return;
        }

        Predicate newPredicate = new AndPredicate(predicates).flatten();

        if (!newPredicate.isSatisfiable()) {
            return;
        }

        List<Integer> toStatesList = new ArrayList<>(toStates);
        Integer toState = getNewStateNumber(toStatesList);

        for (Integer dest : toStatesList) {
            if (finalStates.contains(dest)) {
                newFinalStates.add(toState);
                break;
            }
        }
        if (!addedStates.contains(toState)) {
            statesLeft.add(toStatesList);
        }

        Transition newTransition = new Transition(fromState, toState, newPredicate, color);
        newTransitions.add(newTransition);
    }

    /**
     * Creates a transition map of the chosen color, where Integer represents
     * the from state and Set<> represents the transitions from that state
     */
    private static Map<Integer, Set<Transition>> createTransitionMap(CEA cea, TransitionType color) {

        Map<Integer, Set<Transition>> transitionFrom = new HashMap<>();
        for (int q = 0; q < cea.nStates; q++) {
            Set<Transition> set = new HashSet<>();
            transitionFrom.put(q, set);
        }

        for (Transition t : cea.transitions) {
            if (color == WHITE && !t.isBlack()) {
                transitionFrom.get(t.getFromState()).add(t);
            } else if (color == BLACK && t.isBlack()) {
                transitionFrom.get(t.getFromState()).add(t);
            }
        }

        return transitionFrom;
    }

//    private Set<Transition> getUsefulBlack(int state, EventSchema eventSchema, StreamSchema streamSchema){
//        return reachableFromWithBlack.get(state).stream().filter(
//                transition -> transition.getPredicate().overEvent(eventSchema) &&
//                        transition.getPredicate().overStream(streamSchema)
//        ).collect(Collectors.toSet());
//    }
//
//    private Set<Transition> getUsefulWhite(int state, EventSchema eventSchema, StreamSchema streamSchema){
//        return reachableFromWithWhite.get(state).stream().filter(
//                transition -> transition.getPredicate().getEventSchema().equals(eventSchema) &&
//                        transition.getPredicate().getStreamSchema().equals(streamSchema)
//        ).collect(Collectors.toSet());
//    }

    private Integer getNewStateNumber(List<Integer> stateList) {

        if (!newStateNameMap.containsKey(stateList)) {
            newStateNameMap.put(stateList, nStates++);
        }
        return newStateNameMap.get(stateList);
    }

    /* TODO: REDO ENTIRE FUNCTION TO CONSIDER ONLY VALID COMBINATIONS */
    private List<List<Transition>> getCombinations(List<Transition> values, int size) {
        if (0 == size) {
            return Collections.singletonList(Collections.emptyList());
        }

        if (values.isEmpty()) {
            return Collections.emptyList();
        }

        List<List<Transition>> combination = new LinkedList<>();
        Transition actual = values.iterator().next();
        List<Transition> subSet = new LinkedList<>(values);
        subSet.remove(actual);

        List<List<Transition>> subSetCombination = getCombinations(subSet, size - 1);

        for (List<Transition> set : subSetCombination) {
            List<Transition> newSet = new LinkedList<>(set);
            newSet.add(FIRST, actual);
            combination.add(newSet);
        }
        combination.addAll(getCombinations(subSet, size));

        return combination;
    }

    private void mergeTransitions() {
        /* TODO: IMPLEMENT THIS */
        for (Transition t : transitions) {
            t.getPredicate().flatten();
        }
    }

    private void collapseFinalStates() {
        /* TODO: IMPLEMENT THIS */
    }
}