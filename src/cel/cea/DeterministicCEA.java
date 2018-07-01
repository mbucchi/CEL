package cel.cea;

import cel.cea.predicate.Predicate;
import cel.cea.transition.Transition;
import cel.cea.transition.TransitionType;
import cel.filter.AndEventFilter;
import cel.filter.EventFilter;

import java.util.*;

import static cel.cea.transition.TransitionType.BLACK;
import static cel.cea.transition.TransitionType.WHITE;


public class DeterministicCEA extends CEA {

    private static final int INITIAL = 0;
    private static final int ADDED = 1;

    private int[] addedStates;
    private Map<Integer, Integer> newStatesMap = new HashMap<>();
    private Set<List<Integer>> statesLeft;
    private ArrayList<Transition> newTransitions;
    private Integer fromState;

    public DeterministicCEA(CEA toDeterminize) {

        System.out.println(toDeterminize.toString());
        long compileTime = System.nanoTime();
        /* added states list holds 2^nStates initially */
        addedStates = new int[(int) Math.pow(2, toDeterminize.nStates)];

        /* statesLeft represents the list of states left to check */
        statesLeft = new HashSet<>();
        List<Integer> initialList = new ArrayList<>();
        initialList.add(INITIAL);
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
            if (addedStates[fromState] == ADDED) {
                continue;
            }
            addedStates[fromState] = ADDED;

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
                    makeNewTransition(usefulBlackTransitions, currentTransitionList);
                }
            }

            for (int i = 1; i <= usefulWhiteTransitions.size(); i++) {
                for (List<Transition> currentTransitionList : getCombinations(usefulWhiteTransitions, i)) {
                    makeNewTransition(usefulWhiteTransitions, currentTransitionList);
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

    private void makeNewTransition(List<Transition> usefulTransitions, List<Transition> currentTransitionList) {

        /* currentTransitionList holds which transitions will be true */
        if (!usefulSet(currentTransitionList)) {
            return;
        }
        Set<Integer> toStates = new HashSet<>();
        Transition newTransition = currentTransitionList.get(0).copy();
        toStates.add(newTransition.getToState());

        /* TODO: CHECK EVENT TYPE AND STREAM TYPE FOR MULTIPLE EVENTS OR STREAMS */
        for (Transition currentTransition : currentTransitionList.subList(1, currentTransitionList.size())) {

            for (EventFilter filter : currentTransition.getFilters()) {
                if (notRedundant(newTransition, filter)) {
                    newTransition.addFilter(filter);
                }

            }
            toStates.add(currentTransition.getToState());
        }

        LinkedList<EventFilter> negatedFilters = new LinkedList<>();
        for (Transition currentTransition : usefulTransitions) {
            if (!currentTransitionList.contains(currentTransition) && isComparable(newTransition, currentTransition)) {
                LinkedList<EventFilter> filtersToNegate = new LinkedList<>();
                for (EventFilter filter : currentTransition.getFilters()) {
                    if (notRedundant(newTransition, filter)) {
                        filtersToNegate.add(filter);
                    }
                }
                /* if some negating some transition makes the predicate unsatisfiable, return without changes */
                if (filtersToNegate.isEmpty()) {
                    return;
                }
                negatedFilters.addAll(negateFilters(filtersToNegate));
            }
        }
        if (!negatedFilters.isEmpty()) {
            negatedFilters = removeRedundantFilters(negatedFilters);
            for (EventFilter filter : negatedFilters) {
                newTransition.addFilter(filter);
            }
        }

        List<Integer> toStatesList = new ArrayList<>(toStates);
        Integer toState = getNewStateNumber(toStatesList);
        if (addedStates[toState] != ADDED) {
            statesLeft.add(toStatesList);
        }
        newTransition = newTransition.replaceFromState(fromState).replaceToState(toState);
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
            if ((color == WHITE && !(t.isBlack()))) {
                transitionFrom.get(t.getFromState()).add(t);
            } else if ((color == BLACK && t.isBlack())) {
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
            newSet.add(0, actual);
            combination.add(newSet);
        }
        combination.addAll(getCombinations(subSet, size));

        return combination;
    }

    private boolean usefulSet(List<Transition> transitionList) {
        Transition first = transitionList.remove(0);
        for (Transition transition : transitionList) {
            if (!(isComparable(first, transition))) {
                return false;
            }
        }
        transitionList.add(first);
        return true;
    }

    private LinkedList<EventFilter> negateFilters(Collection<EventFilter> filters) {
        LinkedList<EventFilter> newFilters = new LinkedList<>();
        LinkedList<EventFilter> negatedFilters = new LinkedList<>();
        newFilters.addAll(filters);
        EventFilter current;
        while ((current = newFilters.poll()) != null) {
            LinkedList<EventFilter> currentFilterList = new LinkedList<>();
            currentFilterList.add(current);
            for (EventFilter filter : newFilters) {
                if (current.getLabel() == filter.getLabel()) {
                    currentFilterList.add(filter);
                    newFilters.remove(filter);
                }
            }
            AndEventFilter newFilter = new AndEventFilter(current.getLabel(), currentFilterList);
            negatedFilters.add(newFilter.negate());
        }
        return negatedFilters;
    }

    private boolean notRedundant(Transition t, EventFilter f) {
        /* Extremely naive implementation, just checks if the filters are equal */
        /* TODO: ADD MORE CASES */
        /* TODO: CHECK IF ONE FILTER IMPLIES ANOTHER ON INTEGER COMPARISON (SHOULD BE DONE ON EVENTFILTER CLASS) */
        for (EventFilter transitionFilter : t.getFilters()) {
            if (transitionFilter.equivalentTo(f)) {
                return false;
            }
        }
        return true;
    }

    private void mergeTransitions() {
        /* TODO: IMPLEMENT THIS */
    }

    private void renameStates() {
        int nStates = 0;
        for (int i = 0; i < addedStates.length; i++) {
            if (addedStates[i] == ADDED) {
                newStatesMap.put(i, nStates++);
            }
        }
        LinkedList<Transition> newTransitions = new LinkedList<>();
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

    private LinkedList<EventFilter> removeRedundantFilters(LinkedList<EventFilter> filters) {
        for (int i = 0; i < filters.size(); i++) {
            for (int j = i + 1; j < filters.size(); j++) {
                if (filters.get(i).equivalentTo(filters.get(j))) {
                    filters.remove(j);
                }
            }
        }
        return filters;
    }

    private boolean isComparable(Transition t1, Transition t2) {
        Predicate p1 = t1.getPredicate();
        Predicate p2 = t2.getPredicate();
        return p1.overStream(p2.getStreamSchema()) && p1.overEvent(p2.getEventSchema());
    }
}