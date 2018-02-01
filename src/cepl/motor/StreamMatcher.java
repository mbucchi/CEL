package cepl.motor;

import java.util.Hashtable;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Stack;
import java.util.Iterator;
import java.util.function.Consumer;

class Event {
    int id;
    char type;

    public Event(char type, int id){
        this.type = type;
        this.id = id;
    }

    public String toString(){
        return String.format("%c(id=%d)", type, id);
    }
}

enum Semantic {
    ANY,
    NXT,
    LAST,
    MAX,
    STRICT
}


class StreamMatcher {
    
    private int i = 1;
    private Hashtable<Integer, Event> usefulValues;
    private long totalTime = 0;
    
    private Semantic semantic;
    
    private Consumer<MatchGrouping> sendMatch;
    
    private int stateN;
    private int q0;
    private boolean[] isFinal;
    private int qInit;
    
    // used for normal execution
    private LinkedList<Integer> active_states;
    private ExtensibleList[] states;

    // used for NXT and LAST execution
    private Order order;
    private NXTNode[] node_states;

    private boolean discardPartials = true;

    public double getExecutionTime(){
        return ((double)totalTime) / 1000000000;
    }

    public StreamMatcher() {
        semantic = Semantic.ANY;

        stateN = 4;
        q0 = 0;
        isFinal = new boolean[stateN];
        isFinal[3] = true;

        // initialize machine
        restartMachine();
    }

    public void setDiscardPartials(boolean discard){
        discardPartials = discard;
        restartMachine();        
    }

    public void setSemantic(Semantic semantic){
        /* given the different data structures and algorithms used for each semantic,
        the machine is reset when the semantic is changed */
        this.semantic = semantic;
        restartMachine();
    }

    public void setMatchCallback(Consumer<MatchGrouping> callback){
        sendMatch = callback;
    }

    private int black_transition(int state, Event e){
        if (state == 0 && e.type == 'A') return 1;
        if (state == 1 && e.type == 'A') return 1;
        if (state == 1 && e.type == 'B') return 2;
        if (state == 2 && e.type == 'B') return 2;
        if (state == 2 && e.type == 'C') return 3;
        return -1;
    }

    private int white_transition(int state, Event e){
        if (state < stateN - 1){
            return state;
        }
        return -1;
    }
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
    }

    public void newValue(Event e){
        if (semantic == Semantic.ANY || semantic == Semantic.MAX) {
            // we assume the automata has been compiled as a max matching automata
            newValueANY(e);
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
        long totalMatches = 0;
        
        for (int p : active_states){                                //  \forall p \in Q such that list_p is not empty
               
            int q_b = black_transition(p, e);
            int q_w = white_transition(p, e);

            if (q_b != -1){                                         // black transition update. Including a lazy initialization
                usefulValues.put(i, e);                             // of new_list_q if it had not been initialized
                if (!state_added[q_b]){
                    state_added[q_b] = true;
                    new_states.add(q_b);
                }

                if (_states[q_b] == null){
                    _states[q_b] = new ExtensibleList();
                }
                _states[q_b].add(new Node(i, states[p]));

                if (isFinal[q_b]){
                    match = true;
                    totalMatches += _states[q_b].totalMatches;                    
                }
            }

            if (q_w != -1){                                         // white transition update. Including a lazy initialization
                if (!state_added[q_w]){                             // of new_list_q if it had not been initialized
                    state_added[q_w] = true;
                    new_states.add(q_w);
                }

                if (_states[q_w] == null){
                    _states[q_w] = new ExtensibleList();
                }
                _states[q_w].extend(states[p]);
            }
        }

        states = _states;                                           // update the state of the machine
        active_states = new_states;
        
        totalTime += System.nanoTime() - startTime;  

        if (match) enumerate(totalMatches);
        i++;
    }

    private void newValueNXT(Event e){
        long startTime = System.nanoTime();
        
        NXTNode[] _states = new NXTNode[stateN];
        Order new_o = new Order(stateN);
        int q;
        LinkedList<Integer> b;
        boolean match = false;

        for (LinkedList<Integer> a : order) {
            b = new LinkedList<Integer>();
            for (int p : a) {
                q = black_transition(p, e);
                if (q > -1 && !new_o.containsState(q)){
                    usefulValues.put(i, e);
                    _states[q] = new NXTNode(i, node_states[p]);
                    b.add(q);
                    if (isFinal[q]){
                        match = true;
                    }
                }
            }
            if (b.size() > 0){
                new_o.add(b);
            }
            b = new LinkedList<Integer>();
            for (int p : a){
                q = white_transition(p, e);
                if (q > -1 && !new_o.containsState(q)){
                    _states[q] = node_states[p];
                    b.add(q);                    
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
        int q;
        LinkedList<Integer> b;
        boolean match = false;

        for (LinkedList<Integer> a : order) {
            b = new LinkedList<Integer>();
            for (int p : a) {
                q = black_transition(p, e);
                if (q > -1 && !new_o.containsState(q)){
                    usefulValues.put(i, e);
                    _states[q] = new NXTNode(i, node_states[p]);
                    b.add(q);
                    if (isFinal[q]){
                        match = true;
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
                q = white_transition(p, e);
                if (q > -1 && !new_o.containsState(q)){
                    _states[q] = node_states[p];
                    b.add(q);                    
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
        long totalMatches = 0;

        int n_qInit = white_transition(qInit, e);
        if (n_qInit != -1){
            _states[n_qInit] = new ExtensibleList();
            _states[n_qInit].add(Node.Empty);     
            state_added[n_qInit] = true;
            new_states.add(n_qInit);
        }
        
        for (int p : active_states){                                
               
            int q_b = black_transition(p, e);

            if (q_b != -1){                                         // black transition update. Including a lazy initialization
                usefulValues.put(i, e);                             // of new_list_q if it had not been initialized
                if (!state_added[q_b]){
                    state_added[q_b] = true;
                    new_states.add(q_b);
                }

                if (_states[q_b] == null){
                    _states[q_b] = new ExtensibleList();
                }
                _states[q_b].add(new Node(i, states[p]));

                if (isFinal[q_b]){
                    match = true;
                    totalMatches += _states[q_b].totalMatches;                    
                }
            }
        }

        states = _states;
        active_states = new_states;
        qInit = n_qInit;
        
        totalTime += System.nanoTime() - startTime;  

        if (match) enumerate(totalMatches);
        i++;
    }


    private void enumerate(long totalMatches){
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
}
