//package cel.queryExecution;
//
//import cel.cea.CEA;
//import cel.query.Query;
//import cel.util.ExecutionNode;
//import cel.util.ExtensibleList;
//
//import java.util.Hashtable;
//import java.util.LinkedList;
//import java.util.function.Consumer;
//
//public class NaiveExecutor extends CELExecutor {
//
//
//    private Query query;
//    private CEA cea;
//
//    private int idx = 1;
//    private Hashtable<Integer, Object> usefulValues;
//    private long totalTime = 0;
//
//    private LinkedList<Integer> active_states;
//    private ExtensibleList[] states;
//
//    public NaiveExecutor(Query query){
//        this.query = query;
//        cea = query.getPatternCEA();
//    }
//
//    private Consumer<Object> sendMatch;
//
//
//    @Override
//    public void newValue(Object e){
//        long startTime = System.nanoTime();
//
//        LinkedList<Integer> new_states = new LinkedList<Integer>();             // to keep up with active states
//        boolean[] state_added = new boolean[cea.getNumStates()];                // so that we dont add a state twice to the active list
//        ExtensibleList[] _states = new ExtensibleList[cea.getNumStates()];      // represents list_q /forall q \in Q
//        boolean match = false;
//
//        for (int p : active_states){                                //  \forall p \in Q such that list_p is not empty
//
//            int[] q_b = black_transition(p, e);
//            int[] q_w = white_transition(p, e);
//
//            if (q_b.length > 0)
//                usefulValues.put(i, e);
//
//            for (int idx = 0; idx < q_b.length; idx++ ){            // black transition update. Including a lazy initialization
//                int q = q_b[idx];                                   // of new_list_q if it had not been initialized
//                if (!state_added[q]){
//                    state_added[q] = true;
//                    new_states.add(q);
//                }
//
//                if (_states[q] == null){
//                    _states[q] = new ExtensibleList();
//                }
//                _states[q].add(new ExecutionNode(idx, states[p]));
//
//                if (isFinal[q]){
//                    match = true;
//                }
//            }
//
//            for ( int idx = 0; idx < q_w.length; idx++ ){           // white transition update. Including a lazy initialization
//                int q = q_w[idx];                                   // of new_list_q if it had not been initialized
//                if (!state_added[q]){
//                    state_added[q] = true;
//                    new_states.add(q);
//                }
//
//                if (_states[q] == null){
//                    _states[q] = new ExtensibleList();
//                }
//                _states[q].extend(states[p]);
//            }
//        }
//
//        states = _states;                                           // update the state of the machine
//        active_states = new_states;
//
//        totalTime += System.nanoTime() - startTime;
//
//        if (match) enumerate();
//        idx++;
//    }
//
//
//    @Override
//    public void setMatchCallback(Consumer<Object> callback) {
//
//    }
//
//
//}
