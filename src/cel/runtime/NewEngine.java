package cel.runtime;

import cel.runtime.cea.ExecutableCEA;
import cel.runtime.event.Event;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Consumer;

public class NewEngine {
    // used for normal execution
    private LinkedList<Integer> active_states;
    Map<Integer, ExtensibleList> states;      // represents list_q /forall q \in Q

    // used for NXT and LAST execution
    private Order order;
    private NXTNode[] node_states;

    // used for MAX execution
    private Map<MaxTuple, ExtensibleList> stateListMax;
    private Map<DetNode, ExtensibleList> stateListDet;

    private boolean discardPartials = true;

    private int i = 1;
    private Hashtable<Integer, Event> usefulValues;
    private long totalTime = 0;

    private Semantic semantic;

    private Consumer<MatchGrouping> sendMatch;

    private final int stateN;
    private final int q0;

    private ExecutableCEA cea;
    private CELTraverser celT;

    public NewEngine(ExecutableCEA cea){
        this.stateN = cea.getNStates();
        this.q0 = 0;
        this.cea = cea;
        this.celT = new CELTraverser(cea);
        restartMachine();
    }

    public void setDiscardPartials(boolean discard){
        discardPartials = discard;
        restartMachine();
    }

    public void setMatchCallback(Consumer<MatchGrouping> callback){
        sendMatch = callback;
    }

    public double getExecutionTime(){
        return ((double)totalTime) / 1000000000;
    }

    public void setANY(){
        semantic = Semantic.ANY;
    }

    public void setMAX(){
        semantic = Semantic.MAX;
    }

    public void setLAST(){
        semantic = Semantic.LAST;
    }

    public void setNXT(){
        semantic = Semantic.NXT;
    }

    public void setSTRICT(){
        semantic = Semantic.STRICT;
    }

    public void newValue(Event e, BitSet b){
        if (semantic == Semantic.ANY) {
            newValueANY(e, b);
        }
        else if (semantic == Semantic.MAX){
            newValueMAX(e, b);
        }
//        else if (semantic == Semantic.NXT){
//            newValueNXT(e, b);
//        }
//        else if (semantic == Semantic.LAST){
//            newValueLAST(e, b);
//        }
//        else if (semantic == Semantic.STRICT){
//            newValueSTRICT(e);
//        }
    }

    private void restartMachine(){
        usefulValues = new Hashtable<>();
        ExtensibleList ex = new ExtensibleList();
        ex.add(Node.Empty);

        states = new HashMap<>();
        states.put(q0, ex);

        active_states = new LinkedList<>();
        active_states.add(q0);

        order = new Order(stateN);
        node_states = new NXTNode[stateN];
        node_states[q0] = NXTNode.Empty;
        LinkedList<Integer> I = new LinkedList<>();
        I.add(q0);
        order.add(I);

        stateListMax = new HashMap<>();
        MaxTuple maxI = new MaxTuple(new HashSet<>(I), new HashSet<>());
        ExtensibleList maxStart = new ExtensibleList();
        maxStart.add(Node.Empty);
        stateListMax.put(maxI, maxStart);

        stateListDet = new HashMap<>();
        DetNode detI = new DetNode(new HashSet<>(I));
        ExtensibleList detStart = new ExtensibleList();
        detStart.add(Node.Empty);
        stateListDet.put(detI, detStart);
    }

    private void newValueANY(Event e, BitSet b) {
        long startTime = System.nanoTime();

        LinkedList<Integer> new_states = new LinkedList<>(); // to keep up with active states
        Set<Integer> added_states = new HashSet<>();                // so that we dont add a state twice to the active list
        Map<Integer, ExtensibleList> _states = new HashMap<>();      // represents list_q /forall q \in Q
        ExtensibleList ex;
        boolean match = false;
        // System.out.println(active_states.toString());
        for (int p : active_states){                              //  \forall p \in Q such that list_p is not empty
            List<Integer> nextStates = celT.nextStateDet(p, b);
            Integer q_b = nextStates.get(0);
            Integer q_w = nextStates.get(1);

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
                if (!added_states.contains(q_w)){
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

        states = _states;                                           // update the state of the machine
        active_states = new_states;

        totalTime += System.nanoTime() - startTime;

        if (match) enumerate();
        i++;
    }

//    private void newValueNXT(Event e, BitSet b) {
//        long startTime = System.nanoTime();
//
//        NXTNode[] _states = new NXTNode[stateN];
//        Order new_o = new Order(stateN);
//        Set<Integer> qset;
//        LinkedList<Integer> bll;
//        boolean match = false;
//
//        for (LinkedList<Integer> a : order) {
//            bll = new LinkedList<Integer>();
//            for (int p : a) {
//                qset = cea.blackTransition(p, b);
//                for (Integer q : qset) {
//                    if (!new_o.containsState(q)){
//                        usefulValues.put(i, e);
//                        _states[q] = new NXTNode(i, node_states[p]);
//                        bll.add(q);
//                        if (cea.isFinal(q)){
//                            match = true;
//                        }
//                    }
//                }
//            }
//            if (bll.size() > 0){
//                new_o.add(bll);
//            }
//            bll = new LinkedList<>();
//            for (int p : a){
//                qset = cea.whiteTransition(p, b);
//                for (Integer q : qset) {
//                    if (!new_o.containsState(q)){
//                        _states[q] = node_states[p];
//                        bll.add(q);
//                    }
//                }
//            }
//            if (bll.size() > 0){
//                new_o.add(bll);
//            }
//        }
//
//        order = new_o;
//        node_states = _states;
//
//        totalTime += System.nanoTime() - startTime;
//        if (match) enumerateNXT();
//        i++;
//    }
//
//    private void newValueLAST(Event e, BitSet b) {
//        long startTime = System.nanoTime();
//
//        NXTNode[] _states = new NXTNode[stateN];
//        Order new_o = new Order(stateN);
//        Set<Integer> qset;
//        LinkedList<Integer> bll;
//        boolean match = false;
//
//        for (LinkedList<Integer> a : order) {
//            bll = new LinkedList<>();
//            for (int p : a) {
//                qset = cea.blackTransition(p, b);
//                for (Integer q : qset) {
//                    if (!new_o.containsState(q)){
//                        usefulValues.put(i, e);
//                        _states[q] = new NXTNode(i, node_states[p]);
//                        bll.add(q);
//                        if (cea.isFinal(q)){
//                            match = true;
//                        }
//                    }
//                }
//            }
//            if (bll.size() > 0){
//                new_o.add(bll);
//            }
//        }
//        for (LinkedList<Integer> a : order) {
//            bll = new LinkedList<>();
//            for (int p : a){
//                qset = cea.whiteTransition(p, b);
//                for (Integer q : qset) {
//                    if (!new_o.containsState(q)){
//                        _states[q] = node_states[p];
//                        bll.add(q);
//                    }
//                }
//            }
//            if (bll.size() > 0){
//                new_o.add(bll);
//            }
//        }
//
//        order = new_o;
//        node_states = _states;
//
//        totalTime += System.nanoTime() - startTime;
//        if (match) enumerateNXT();
//        i++;
//    }
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

    private void newValueMAX(Event e, BitSet b) {
        long startTime = System.nanoTime();

        LinkedList<Integer> new_states = new LinkedList<>(); // to keep up with active states
        Set<Integer> added_states = new HashSet<>();                // so that we dont add a state twice to the active list
        Map<Integer, ExtensibleList> _states = new HashMap<>();      // represents list_q /forall q \in Q
        ExtensibleList ex;
        boolean match = false;
        // System.out.println(active_states.toString());
        for (int p : active_states){                              //  \forall p \in Q such that list_p is not empty
            List<Integer> nextStates = celT.nextStateMAX(p, b);
            Integer q_b = nextStates.get(0);
            Integer q_w = nextStates.get(1);

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
                if (!added_states.contains(q_w)){
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

        states = _states;                                           // update the state of the machine
        active_states = new_states;

        totalTime += System.nanoTime() - startTime;

        if (match) enumerateMAX();
        i++;
    }

//    private void newValueMAX(Event e, BitSet b) {
//        long startTime = System.nanoTime();
//
//        Map<MaxTuple, ExtensibleList> newStateList = new HashMap<MaxTuple, ExtensibleList>();
//        boolean match = false;
//
//        for (MaxTuple t: stateListMax.keySet()){
//            Set<Integer> TB = blackTransitionSet(t.T, b);
//            Set<Integer> UB = blackTransitionSet(t.U, b);
//            Set<Integer> TW = whiteTransitionSet(t.T, b);
//            Set<Integer> UW = whiteTransitionSet(t.U, b);
//            // System.out.println("Tuple: " + t.T + " " + t.U);
//            // System.out.println("TB: " + TB + " UB: " + UB);
//            // System.out.println("TW: " + TW + " UW: " + UW);
//            Set<Integer> newTB = new HashSet<Integer>(TB);
//            newTB.removeAll(UB);
//
//            if (newTB.size() > 0){
//                usefulValues.put(i, e);
//                MaxTuple newTup = new MaxTuple(newTB, UB);
//                if (!newStateList.containsKey(newTup)){
//                    newStateList.put(newTup, new ExtensibleList());
//                }
//                for(int q: newTup.T){
//                    if (cea.isFinal(q)){
//                        match = true;
//                        newTup.isFinal = true;
//                        break;
//                    }
//                }
//                newStateList.get(newTup).add(new Node(i, stateListMax.get(t)));
//            }
//
//            Set<Integer> newUW = new HashSet<>(UB);
//            newUW.addAll(UW);
//            newUW.addAll(TB);
//            TW.removeAll(newUW);
//
//            if (TW.size() > 0){
//                MaxTuple newTup = new MaxTuple(TW, newUW);
//                if (!newStateList.containsKey(newTup)){
//                    newStateList.put(newTup, new ExtensibleList());
//                }
//                newStateList.get(newTup).extend(stateListMax.get(t));
//            }
//
//        }
//
//        stateListMax = newStateList;
//
//        totalTime += System.nanoTime() - startTime;
//        if (match) enumerateMAX();
//        i++;
//    }

    private void enumerate(){
        if (sendMatch != null){
            MatchGrouping m = new MatchGrouping(semantic, usefulValues, i);
            for (int q: active_states){
                if (cea.isFinal(q)){
                    m.addFinal(states.get(q));
                }
            }
            sendMatch.accept(m);
        }
        if (discardPartials){
            restartMachine();
        }
    }

    private void enumerateMAX(){
        if (sendMatch != null){
            MatchGrouping m = new MatchGrouping(semantic, usefulValues, i);
            for (int q: active_states){
                if (celT.isFinal(q)){
                    m.addFinal(states.get(q));
                }
            }
            sendMatch.accept(m);
        }
        if (discardPartials){
            restartMachine();
        }
    }

    private void enumerateNXT(){

        if (sendMatch != null){
            MatchGrouping m = new MatchGrouping(semantic, usefulValues, i);
            for (int q: order.getStates()){
                if (cea.isFinal(q)){
                    m.addFinal(node_states[q]);
                    sendMatch.accept(m);
                    break;
                }
            }
        }

        if (discardPartials){
            restartMachine();
        }
    }

}
