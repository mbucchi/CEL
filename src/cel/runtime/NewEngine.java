package cel.runtime;

import cel.runtime.cea.ExecutableCEA;
import cel.runtime.event.Event;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.IntFunction;

public class NewEngine {
    /* used for all executions */
    private LinkedList<Integer> active_states;
    private Map<Integer, ExtensibleList> states;      // represents list_q /forall q \in Q

    private boolean discardPartials = true;

    /* used for enumeration */
    private int i = 1;
    private Hashtable<Integer, Event> usefulValues;

    private long totalTime = 0;

    private Semantic semantic;

    private Consumer<MatchGrouping> sendMatch;

    private final int q0;
    private CELTraverser celT;

    public NewEngine(ExecutableCEA cea) {
        this.q0 = 0;
        this.celT = new CELTraverser(cea);
        restartEngine();
    }

    public void setDiscardPartials(boolean discard) {
        discardPartials = discard;
//        restartEngine();
    }

    public void setMatchCallback(Consumer<MatchGrouping> callback){
        sendMatch = callback;
    }

    public double getExecutionTime(){
        return ((double)totalTime) / 1000000000;
    }

    public void initALL() {
        semantic = Semantic.ANY;
        celT.initALL();
    }

    public void initMAX() {
        semantic = Semantic.MAX;
        celT.initMAX();
    }

    public void initLAST() {
        semantic = Semantic.LAST;
        celT.initLAST();
    }

    public void initNEXT() {
        semantic = Semantic.NXT;
        celT.initNEXT();
    }

    public void initSTRICT() {
//        semantic = Semantic.STRICT;
    }

    public void newValue(Event e, BitSet b) {
        if (semantic == Semantic.ANY) {
            newValueANY(e, p -> celT.nextStateALL(p, b));
        }
        else if (semantic == Semantic.MAX) {
            newValueANY(e, p -> celT.nextStateMAX(p, b));
        }
        else if (semantic == Semantic.NXT) {
            newValueANY(e, p -> celT.nextStateNEXT(p, b));
        }
        else if (semantic == Semantic.LAST) {
            newValueANY(e, p -> celT.nextStateLAST(p, b));
        }
//        else if (semantic == Semantic.STRICT){
//            newValueSTRICT(e);
//        }
    }

    private void restartEngine() {
        usefulValues = new Hashtable<>();
        ExtensibleList ex = new ExtensibleList();
        ex.add(Node.Empty);

        states = new HashMap<>();
        states.put(q0, ex);

        active_states = new LinkedList<>();
        active_states.add(q0);
    }

    private void newValueANY(Event e, IntFunction<List<Integer>> intFunction) {
        long startTime = System.nanoTime();

        /* same algorithm as old ANY */
        LinkedList<Integer> new_states = new LinkedList<>();
        Set<Integer> added_states = new HashSet<>();
        Map<Integer, ExtensibleList> _states = new HashMap<>();
        boolean match = false;
        // System.out.println(active_states.toString());
        for (int p : active_states) {
            List<Integer> nextStates = intFunction.apply(p);
            Integer q_b = nextStates.get(0);
            Integer q_w = nextStates.get(1);
            ExtensibleList ex;
            if (q_b != -1) {
                usefulValues.put(i, e);
                // black transition update. Including a lazy initialization
                // of new_list_q if it had not been initialized
                if (!added_states.contains(q_b)) {
                    added_states.add(q_b);
                    new_states.add(q_b);
                }

                ex = _states.get(q_b);
                if (ex == null) {
                    ex = new ExtensibleList();
                    _states.put(q_b, ex);
                }
                ex.add(new Node(i, states.get(p)));

                if (celT.isFinal(q_b)) {
                    match = true;
                }
            }

            if (q_w != -1) {
                if (!added_states.contains(q_w)) {
                    added_states.add(q_w);
                    new_states.add(q_w);
                }

                ex = _states.get(q_w);
                if (ex == null) {
                    ex = new ExtensibleList();
                    _states.put(q_w, ex);
                }
                ex.extend(states.get(p));
            }
        }

        states = _states;
        active_states = new_states;

        totalTime += System.nanoTime() - startTime;

        if (match) {
            enumerate();
        }
        i++;
    }

//    private void newValueSTRICT(Event e, BitSet b) {
//        long startTime = System.nanoTime();
//
//        LinkedList<Integer> new_states = new LinkedList<>();
//        boolean[] state_added = new boolean[stateN];
//        ExtensibleList[] _states = new ExtensibleList[stateN];
//        boolean match = false;
//
//        Integer n_qInit = cea.whiteTransition(qInit, b).iterator().next();
//        if (n_qInit != -1){
//            _states[n_qInit] = new ExtensibleList();
//            _states[n_qInit].add(Node.Empty);
//            state_added[n_qInit] = true;
//            new_states.add(n_qInit);
//        }
//
//        for (int p : active_states){
//
//            int[] q_b = black_transition(p, e);
//
//            for (int idx = 0; idx < q_b.length; idx++){             // black transition update. Including a lazy initialization
//                usefulValues.put(i, e);                             // of new_list_q if it had not been initialized
//                int q = q_b[idx];
//                if (!state_added[q]){
//                    state_added[q] = true;
//                    new_states.add(q);
//                }
//
//                if (_states[q] == null){
//                    _states[q] = new ExtensibleList();
//                }
//                _states[q].add(new Node(i, states[p]));
//
//                if (isFinal[q]){
//                    match = true;
//                }
//            }
//        }
//
//        states = _states;
//        active_states = new_states;
//        qInit = n_qInit;
//
//        totalTime += System.nanoTime() - startTime;
//
//        if (match) enumerate();
//        i++;
//    }

    private void enumerate() {
        if (sendMatch != null) {
            MatchGrouping m = new MatchGrouping(Semantic.ANY, usefulValues, i);
            for (int q: active_states){
                if (celT.isFinal(q)){
                    m.addFinal(states.get(q));
                }
            }
            sendMatch.accept(m);
        }
        if (discardPartials) {
            restartEngine();
        }
    }

}
