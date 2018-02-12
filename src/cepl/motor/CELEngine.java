package cepl.motor;

import java.util.Hashtable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import java.util.Iterator;
import java.util.function.Consumer;

enum Semantic {
    ANY,
    NXT,
    LAST,
    MAX,
    STRICT
}


public abstract class CELEngine {
    
    private int i = 1;
    private Hashtable<Integer, Event> usefulValues;
    private long totalTime = 0;
    
    private Semantic semantic;
    
    private Consumer<MatchGrouping> sendMatch;
    
    private final int stateN;
    private final int q0;
    private final boolean[] isFinal;
    private int qInit;

    protected CELEngine(int stateN, int q0, boolean[] isFinal){
        this.stateN = stateN;
        this.q0 = q0;
        this.isFinal = isFinal;
        restartMachine();
    }
    
    // used for normal execution
    private LinkedList<Integer> active_states;
    private ExtensibleList[] states;

    // used for NXT and LAST execution
    private Order order;
    private NXTNode[] node_states;

    // used for MAX execution
    private Map<MaxTuple, ExtensibleList> stateListMax;

    private boolean discardPartials = true;

    public double getExecutionTime(){
        return ((double)totalTime) / 1000000000;
    }

    public void setDiscardPartials(boolean discard){
        discardPartials = discard;
        restartMachine();        
    }

    public void setMatchCallback(Consumer<MatchGrouping> callback){
        sendMatch = callback;
    }

    protected void setANY(){
        semantic = Semantic.ANY;
    }

    protected void setMAX(){
        semantic = Semantic.MAX;
    }

    protected void setLAST(){
        semantic = Semantic.LAST;
    }

    protected void setNXT(){
        semantic = Semantic.NXT;
    }

    protected void setSTRICT(){
        semantic = Semantic.STRICT;
    }

    abstract protected int[] black_transition(int state, Event e);
    abstract protected int[] white_transition(int state, Event e);

    private void restartMachine(){
        usefulValues = new Hashtable<Integer, Event>();
        states = new ExtensibleList[stateN];
        states[q0] = new ExtensibleList();
        states[q0].add(Node.Empty);
        active_states = new LinkedList<Integer>();
        active_states.add(q0);

        qInit = q0;

        order = new Order(stateN);
        node_states = new NXTNode[stateN];
        node_states[q0] = NXTNode.Empty;
        LinkedList<Integer> I = new LinkedList<Integer>();
        I.add(q0);
        order.add(I);

        stateListMax = new HashMap<MaxTuple, ExtensibleList>();
        MaxTuple maxI = new MaxTuple(new HashSet<Integer>(I), new HashSet<Integer>());
        ExtensibleList maxStart = new ExtensibleList();
        maxStart.add(Node.Empty);
        stateListMax.put(maxI, maxStart);
    }

    public void newValue(Event e){
        if (semantic == Semantic.ANY) {
            newValueANY(e);
        }
        else if (semantic == Semantic.MAX){
            newValueMAX(e);            
        }
        else if (semantic == Semantic.NXT){
            newValueNXT(e);
        }
        else if (semantic == Semantic.LAST){
            newValueLAST(e);
        }
        else if (semantic == Semantic.STRICT){
            newValueSTRICT(e);
        }
    }

    private void newValueANY(Event e){
        long startTime = System.nanoTime();

        LinkedList<Integer> new_states = new LinkedList<Integer>(); // to keep up with active states
        boolean[] state_added = new boolean[stateN];                // so that we dont add a state twice to the active list   
        ExtensibleList[] _states = new ExtensibleList[stateN];      // represents list_q /forall q \in Q
        boolean match = false;

        for (int p : active_states){                                //  \forall p \in Q such that list_p is not empty
               
            int[] q_b = black_transition(p, e);
            int[] q_w = white_transition(p, e);

            if (q_b.length > 0)
                usefulValues.put(i, e);                                 
            for (int idx = 0; idx < q_b.length; idx++ ){            // black transition update. Including a lazy initialization   
                int q = q_b[idx];                                   // of new_list_q if it had not been initialized
                if (!state_added[q]){
                    state_added[q] = true;
                    new_states.add(q);
                }

                if (_states[q] == null){
                    _states[q] = new ExtensibleList();
                }
                _states[q].add(new Node(i, states[p]));

                if (isFinal[q]){
                    match = true;
                }
            }

            for ( int idx = 0; idx < q_w.length; idx++ ){           // white transition update. Including a lazy initialization
                int q = q_w[idx];                                   // of new_list_q if it had not been initialized
                if (!state_added[q]){                                 
                    state_added[q] = true;
                    new_states.add(q);
                }

                if (_states[q] == null){
                    _states[q] = new ExtensibleList();
                }
                _states[q].extend(states[p]);
            }               
        }

        states = _states;                                           // update the state of the machine
        active_states = new_states;
        
        totalTime += System.nanoTime() - startTime;  

        if (match) enumerate();
        i++;
    }

    private void newValueNXT(Event e){
        long startTime = System.nanoTime();
        
        NXTNode[] _states = new NXTNode[stateN];
        Order new_o = new Order(stateN);
        int[] qarr;
        int q;
        LinkedList<Integer> b;
        boolean match = false;

        for (LinkedList<Integer> a : order) {
            b = new LinkedList<Integer>();
            for (int p : a) {
                qarr = black_transition(p, e);
                for (int idx=0; idx < qarr.length; idx++){
                    q = qarr[idx];
                    if (!new_o.containsState(q)){
                        usefulValues.put(i, e);
                        _states[q] = new NXTNode(i, node_states[p]);
                        b.add(q);
                        if (isFinal[q]){
                            match = true;
                        }
                    }
                }
            }
            if (b.size() > 0){
                new_o.add(b);
            }
            b = new LinkedList<Integer>();
            for (int p : a){
                qarr = white_transition(p, e);
                for (int idx=0; idx < qarr.length; idx++){
                    q = qarr[idx];
                    if (!new_o.containsState(q)){
                        _states[q] = node_states[p];
                        b.add(q);                    
                    }
                }
            }
            if (b.size() > 0){
                new_o.add(b);
            }
        }

        order = new_o;
        node_states = _states;

        totalTime += System.nanoTime() - startTime;
        if (match) enumerateNXT();
        i++;
    }

    private void newValueLAST(Event e){
        long startTime = System.nanoTime();
        
        NXTNode[] _states = new NXTNode[stateN];
        Order new_o = new Order(stateN);
        int[] qarr;
        int q;
        LinkedList<Integer> b;
        boolean match = false;

        for (LinkedList<Integer> a : order) {
            b = new LinkedList<Integer>();
            for (int p : a) {
                qarr = black_transition(p, e);
                for (int idx=0; idx < qarr.length; idx++){                
                    q = qarr[idx];
                    if (!new_o.containsState(q)){
                        usefulValues.put(i, e);
                        _states[q] = new NXTNode(i, node_states[p]);
                        b.add(q);
                        if (isFinal[q]){
                            match = true;
                        }
                    }
                }
            }
            if (b.size() > 0){
                new_o.add(b);
            }
        }
        for (LinkedList<Integer> a : order) {
            b = new LinkedList<Integer>();
            for (int p : a){
                qarr = white_transition(p, e);
                for (int idx=0; idx < qarr.length; idx++){  
                    q = qarr[idx];              
                    if (!new_o.containsState(q)){
                        _states[q] = node_states[p];
                        b.add(q);                    
                    }
                }
            }
            if (b.size() > 0){
                new_o.add(b);
            }
        }

        order = new_o;
        node_states = _states;

        totalTime += System.nanoTime() - startTime;
        if (match) enumerateNXT();
        i++;
    }

    private void newValueSTRICT(Event e){
        long startTime = System.nanoTime();

        LinkedList<Integer> new_states = new LinkedList<Integer>(); 
        boolean[] state_added = new boolean[stateN];                
        ExtensibleList[] _states = new ExtensibleList[stateN];      
        boolean match = false;

        int n_qInit = white_transition(qInit, e)[0];
        if (n_qInit != -1){
            _states[n_qInit] = new ExtensibleList();
            _states[n_qInit].add(Node.Empty);     
            state_added[n_qInit] = true;
            new_states.add(n_qInit);
        }
        
        for (int p : active_states){                                
               
            int[] q_b = black_transition(p, e);

            for (int idx = 0; idx < q_b.length; idx++){             // black transition update. Including a lazy initialization
                usefulValues.put(i, e);                             // of new_list_q if it had not been initialized
                int q = q_b[idx];
                if (!state_added[q]){
                    state_added[q] = true;
                    new_states.add(q);
                }

                if (_states[q] == null){
                    _states[q] = new ExtensibleList();
                }
                _states[q].add(new Node(i, states[p]));

                if (isFinal[q]){
                    match = true;
                }
            }
        }

        states = _states;
        active_states = new_states;
        qInit = n_qInit;
        
        totalTime += System.nanoTime() - startTime;  

        if (match) enumerate();
        i++;
    }


    private Set<Integer> blackTransitionSet(Set<Integer> stateSet, Event e){
        Set<Integer> newStateSet = new HashSet<Integer>();
        for (Integer q: stateSet){
            int[] ps = black_transition(q, e);
            for (int i = 0; i < ps.length; i++){
                newStateSet.add(ps[i]);
            }
        }
        return newStateSet;
    }

    private Set<Integer> whiteTransitionSet(Set<Integer> stateSet, Event e){
        Set<Integer> newStateSet = new HashSet<Integer>();
        for (Integer q: stateSet){
            int[] ps = white_transition(q, e);
            for (int i = 0; i < ps.length; i++){
                newStateSet.add(ps[i]);
            }
        }
        return newStateSet;
    }

    private void newValueMAX(Event e){
        long startTime = System.nanoTime();

        Map<MaxTuple, ExtensibleList> newStateList = new HashMap<MaxTuple, ExtensibleList>();
        boolean match = false;

        for (MaxTuple t: stateListMax.keySet()){
            Set<Integer> TB = blackTransitionSet(t.T, e);
            Set<Integer> UB = blackTransitionSet(t.U, e);
            Set<Integer> TW = whiteTransitionSet(t.T, e);
            Set<Integer> UW = whiteTransitionSet(t.U, e);

            Set<Integer> newUB = UB;
            Set<Integer> newTB = new HashSet<Integer>(TB);
            newTB.removeAll(newUB);

            if (newTB.size() > 0){
                usefulValues.put(i, e);
                MaxTuple newTup = new MaxTuple(newTB, newUB);
                if (!newStateList.containsKey(newTup)){
                    newStateList.put(newTup, new ExtensibleList());
                }
                for(int q: newTup.T){
                    if (isFinal[q]){
                        match = true;
                        newTup.isFinal = true;
                        break;
                    }
                }
                newStateList.get(newTup).add(new Node(i, stateListMax.get(t)));
            }

            Set<Integer> newUW = new HashSet<Integer>(UB);
            newUW.addAll(UW);
            newUW.addAll(TB);
            Set<Integer> newTW = TW;
            TW.removeAll(newUW);

            if (newTW.size() > 0){
                MaxTuple newTup = new MaxTuple(newTW, newUW);
                if (!newStateList.containsKey(newTup)){
                    newStateList.put(newTup, new ExtensibleList());
                }
                newStateList.get(newTup).extend(stateListMax.get(t));
            }

        }

        stateListMax = newStateList;

        totalTime += System.nanoTime() - startTime;  
        if (match) enumerateMAX();
        i++;
    }

    private void enumerate(){
        if (sendMatch != null){
            MatchGrouping m = new MatchGrouping(semantic, usefulValues, i);
            for (int q: active_states){
                if (isFinal[q]){
                    m.addFinal(states[q]);
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
                if (isFinal[q]){
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

    private void enumerateMAX(){
        if (sendMatch != null){
            MatchGrouping m = new MatchGrouping(semantic, usefulValues, i);
            for (MaxTuple t: stateListMax.keySet()){
                if (t.isFinal){
                    m.addFinal(stateListMax.get(t));
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
