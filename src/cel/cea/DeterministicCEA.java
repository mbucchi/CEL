package cel.cea;

import cel.cea.predicate.Predicate;
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

    public DeterministicCEA(CEA toDeterminize) {

//        System.out.println(toDeterminize.toString());
        long compileTime = System.nanoTime();

        addedStates = new HashSet<>();

        /* statesLeft represents the list of states left to check */
        statesLeft = new HashSet<>();
        List<Integer> initialList = new ArrayList<>();
        initialList.add(INITIAL_STATE);
        statesLeft.add(initialList);

        /* we get the transition set for each state */
        Map<Integer, Set<Transition>> reachableFromWithBlack = createTransitionMap(toDeterminize, BLACK);
        Map<Integer, Set<Transition>> reachableFromWithWhite = createTransitionMap(toDeterminize, WHITE);
        List<Transition> usefulBlackTransitions = new ArrayList<>();
        List<Transition> usefulWhiteTransitions = new ArrayList<>();

        newTransitions = new ArrayList<>();
        List<Integer> current;
        while (statesLeft.iterator().hasNext()) {

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
        renameStates();
        compileTime = System.nanoTime() - compileTime;
        System.out.println("Determinization time: " + ((double) compileTime / 1000000000));
        /* TODO: MERGE TRANSITIONS WITH EQUAL TO AND FROM STATE */
        mergeTransitions();
        /* TODO: COLLAPSE FINAL STATES */
        collapseFinalStates();
        /* TODO: UPDATE AUTOMATA */
//        finalState = newFinalState;
//        labelSet = newLabelSet;
//        eventSchemas = newEventSchemas;
    }

    private void makeNewTransition(List<Transition> usefulTransitions, List<Transition> currentTransitionList, TransitionType color) {

        /* currentTransitionList holds which transitions will be true */
        Set<Integer> toStates = new HashSet<>();
        Transition newTransition = new Transition(fromState, color);
        Predicate newPredicate = new Predicate();

        for (Transition currentTransition : currentTransitionList) {
            newPredicate.addPredicate(currentTransition.getPredicate());
            toStates.add(currentTransition.getToState());
        }


        for (Transition currentTransition : usefulTransitions) {
            if (!currentTransitionList.contains(currentTransition)) {
                newPredicate.addPredicate(currentTransition.getPredicate().negate());
            }
        }

        if (!newPredicate.satisfiable) {
            return;
        }
        if (newPredicate.getPredicates().size() == 1) {
            newPredicate = newPredicate.getPredicates().iterator().next();
        }
        newTransition.setPredicate(newPredicate);

        List<Integer> toStatesList = new ArrayList<>(toStates);
        Integer toState = getNewStateNumber(toStatesList);
        if (!addedStates.contains(toState)) {
            statesLeft.add(toStatesList);
        }
        newTransition = newTransition.replaceToState(toState);
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

    private static Integer getNewStateNumber(List<Integer> stateList) {

        Integer res = -1;

        for (Integer state : stateList) {
            res += (int) Math.pow(2, state);
        }

        return res;
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
    }

    private void renameStates() {
        int nStates = 0;
        for (Integer addedState : addedStates) {
            newStatesMap.put(addedState, nStates++);
        }
        ArrayList<Transition> newTransitions = new ArrayList<>();
        for (Transition t : transitions) {
            newTransitions.add(t.replaceToState(newStatesMap.get(t.getToState()))
                    .replaceFromState(newStatesMap.get(t.getFromState())));
        }
        transitions = newTransitions;
        this.nStates = nStates;
    }

    private void collapseFinalStates() {
        /* TODO: IMPLEMENT THIS */
    }
}