package cepl.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

class Automata {

    private int stateN;
    private int q0;
    private Set<Integer> finalStates;
    private ArrayList<Transition> blacks;
    private ArrayList<Transition> whites;
    private Map<String, String> varRelationMap;
    private NodeType semantic;


    public Automata(ASTNode node, Map<String, String> varRelationMap) throws Exception{
        q0 = stateN = 0;
        finalStates = new HashSet<Integer>();
        blacks = new ArrayList<Transition>();
        whites = new ArrayList<Transition>();
        this.varRelationMap = varRelationMap;

        switch(node.type){
            case MAX:
            case ANY:
            case NXT:
            case LAST:
            case STRICT:
                copyFrom(new Automata(node.children.getFirst(), varRelationMap));
                semantic = node.type;
                break;
            case OR:
                ORAutomata(new Automata(node.children.getFirst(), varRelationMap), 
                           new Automata(node.children.getLast(), varRelationMap));
                break;
            case SEQ:
                SEQAutomata(new Automata(node.children.getFirst(), varRelationMap), 
                            new Automata(node.children.getLast(), varRelationMap));
                break;
            case KLEENE:
                KleeneAutomata(new Automata(node.children.getFirst(), varRelationMap));
                break;
            case FILTER:
                FilterAutomata(new Automata(node.children.getFirst(), varRelationMap), node.children.getLast());
                break;
            case ASSIGN:
                AssignAutomata(node);
                break;
            default:
                throw new Exception("Malformed AST");
        }

        removeUselessStates();
    }

    private void copyFrom(Automata a){
        stateN = a.stateN;
        q0 = a.q0;
        finalStates = a.finalStates;
        blacks = a.blacks;
        whites = a.whites;
    }

    private void removeUselessStates(){
        // calculate reachable states

        Map<Integer, Set<Integer>> reachableFrom = new HashMap<Integer, Set<Integer>>();
        for (int q = 0; q < stateN; q++){
            Set<Integer> set = new HashSet<Integer>();
            set.add(q);
            reachableFrom.put(q, set);
        }

        for (Transition t: blacks) reachableFrom.get(t.from).addAll(t.to);
        for (Transition t: whites) reachableFrom.get(t.from).addAll(t.to);

        boolean updated = true;

        while (updated){
            updated = false;
            for (int p = 0; p < stateN; p++){
                Set<Integer> reachableP = reachableFrom.get(p);
                Set<Integer> newReachable = new HashSet<Integer>();
                for (int q: reachableP){
                    newReachable.addAll(reachableFrom.get(q));
                }
                updated = updated || reachableP.addAll(newReachable);
            }
        }

        // useful states
        Set<Integer> reachableQ0 = reachableFrom.get(0);
        Set<Integer> usefulStates = new HashSet<Integer>();
        for (int q: reachableQ0){
            Set<Integer> reachableQ = reachableFrom.get(q);
            for (int p: reachableQ){
                if (finalStates.contains(p)) usefulStates.add(q);
            }
        }
        
        // renumber all useful states
        int[] newNames = new int[stateN];
        int newStateN = 0;
        
        for (int q = 0; q < stateN; q++){
            if (usefulStates.contains(q)){
                newNames[q] = newStateN++;
            }
        }

        // rename final states
        Set<Integer> newFinals = new HashSet<Integer>();
        for (int q: finalStates){
            if (usefulStates.contains(q)){
                newFinals.add(newNames[q]);
            }
        }

        // rename transitions
        ArrayList<Transition> newBlacks = new ArrayList<Transition>();
        ArrayList<Transition> newWhites = new ArrayList<Transition>();

        for (Transition t: blacks){
            if (usefulStates.contains(t.from)){
                Transition posibleNew = new Transition(newNames[t.from], t.formula);
                for (int q: t.to){
                    if (usefulStates.contains(q)) posibleNew.addTo(newNames[q]);
                }
                if (posibleNew.to.size() > 0) newBlacks.add(posibleNew);
            }
        }

        for (Transition t: whites){
            if (usefulStates.contains(t.from)){
                Transition posibleNew = new Transition(newNames[t.from]);
                for (int q: t.to){
                    if (usefulStates.contains(q)) posibleNew.addTo(newNames[q]);
                }
                if (posibleNew.to.size() > 0) newWhites.add(posibleNew);
            }
        }

        // update automata
        stateN = newStateN;
        finalStates = newFinals;
        blacks = newBlacks;
        whites = newWhites;
        // q0 is still 0
    }

    private void ORAutomata(Automata left, Automata right) throws Exception{
        int displace = left.stateN + 1;
        stateN = right.stateN + displace;
        q0 = 0;
        for (int q: left.finalStates){
            finalStates.add(q + 1);
        }
        for (int q: right.finalStates){
            finalStates.add(q + displace);
        }
        
        for (Transition t: left.blacks){
            if (t.from == 0){
                Transition newT = new Transition(0, t.formula, t.to);
                newT.displaceTo(1);
                blacks.add(newT);
            }
            t.displace(1);
            blacks.add(t);
        }
        for (Transition t: left.whites){
            t.displace(1);            
            whites.add(t);
        }

        for (Transition t: right.blacks){
            if (t.from == 0){
                Transition newT = new Transition(0, t.formula, t.to);
                newT.displaceTo(displace);
                blacks.add(newT);
            }
            t.displace(displace);
            blacks.add(t);
        }
        for (Transition t: right.whites){
            t.displace(displace);                        
            whites.add(t);
        }

        whites.add(new Transition(0, 0));

    }

    private void SEQAutomata(Automata left, Automata right) throws Exception{
        int displace = left.stateN;
        stateN = right.stateN + displace;
        q0 = 0;
        for (int q: right.finalStates){
            finalStates.add(q + displace);
        }

        for (Transition t: left.blacks){
            boolean isFinal = false;
            for (int q: t.to){
                if (left.finalStates.contains(q)){
                    t.addTo(displace);
                }
            }
            blacks.add(t);
        }
        
        for (Transition t: left.whites){
            whites.add(t);
        }
        
        for (Transition t: right.blacks){
            t.displace(displace);
            blacks.add(t);
        }
        for (Transition t: right.whites){
            t.displace(displace);
            whites.add(t);
        }

    }

    private void KleeneAutomata(Automata inner) throws Exception{
        stateN = inner.stateN;
        q0 = 0;
        finalStates = inner.finalStates;
        whites = new ArrayList<Transition>(inner.whites);
        blacks = new ArrayList<Transition>(inner.blacks);

        for (Transition t: inner.blacks){
            for (int q: t.to){
                if (finalStates.contains(q)){
                    t.addTo(0);
                    break;
                }
            }
        }
    }

    private void FilterAutomata(Automata left, ASTNode predicate) throws Exception{
        if (left.stateN > 2) throw new Exception("Malformed AST");
        stateN = 2;
        q0 = 0;
        finalStates.add(1);
        whites.add(new Transition(0, 0));
        Transition prev = left.blacks.get(0);        
        prev.formula += " && " + getPredicate(predicate);
        blacks.add(prev);
    }

    private void AssignAutomata(ASTNode node) throws Exception{
        stateN = 2;
        q0 = 0;
        finalStates.add(1);
        String formula = "&& e instanceof " + node.children.getFirst().value.sequence;
        blacks.add(new Transition(0, formula, 1));
        whites.add(new Transition(0, 0));
    }

    private String getPredicate(ASTNode predicate) throws Exception {
        ASTNode first = predicate.children.getFirst();
        ASTNode last = predicate.children.getLast();
        switch (predicate.type){
            case PRED_AND:
                return "(" + getPredicate(first) + " && " + getPredicate(last) + ")";
            case PRED_OR:
                return "(" + getPredicate(first) + " || " + getPredicate(last) + ")";        
            case PREDICATE:
                String property = first.children.getLast().value.sequence;
                String var = first.children.getFirst().value.sequence;
                String value = last.value.sequence;
                String op = predicate.children.get(1).value.sequence;
                return "((" + varRelationMap.get(var) +  ")e)." + property + " " + op + " " + value;
            default:
                throw new Exception("Error building filter formula!");
        }
    }

    public int getQ0(){
        return q0;
    }

    public int getStateN(){
        return stateN;
    }

    public Set<Integer> getFinalState(){
        return finalStates;
    }

    public ArrayList<Transition> getBlackTransitions(){
        return blacks;
    }

    public ArrayList<Transition> getWhiteTransitions(){
        return whites;
    }

    public String getSemantic(){
        return semantic.name();
    }


    public void print(){
        System.out.println("State number: " + stateN);
        System.out.println("Init: " + q0);
        System.out.println("Final: " + finalStates);
        System.out.println("Blacks: ");
        for (Transition t: blacks){
            System.out.println("\t(" + t.from + ", " + t.formula + ", " + t.to + ")");
        }
        System.out.println("Whites: ");
        for (Transition t: whites){
            System.out.println("\t(" + t.from + ", " + t.to + ")");
        }
    }
}

class Transition {
    int from;
    String formula;
    Set<Integer> to;
    public Transition(int p, String formula, int q){
        to = new HashSet<Integer>();
        from = p;
        this.formula = formula;
        to.add(q);
    }

    public Transition(int p, String formula, Set<Integer> to){
        this.to = to;
        from = p;
        this.formula = formula;
    }

    public Transition(int p, String formula){
        to = new HashSet<Integer>();
        from = p;
        this.formula = formula;
    }

    public Transition(int p, int q){
        this(p, null, q);
    }
    public Transition(int p){
        to = new HashSet<Integer>();
        from = p;
        formula = null;
    }

    public void addTo(int q){
        to.add(q);
    }

    public void displace(int nStates){
        from += nStates;
        displaceTo(nStates);
    }

    public void displaceTo(int nStates){
        Set<Integer> newTo = new HashSet<Integer>();
        for (int q: to){
            newTo.add(q + nStates);
        }
        to = newTo;
    }

    public Integer[] getTo(){
        return to.toArray(new Integer[0]);
    }

}


class MAXNode {
    public Set<Integer> U;
    public Set<Integer> T;

    public MAXNode(){
        U = new HashSet<Integer>();
        T = new HashSet<Integer>();
    }

    public MAXNode(Set<Integer> U, Set<Integer> T){
        U = new HashSet<Integer>(U);
        T = new HashSet<Integer>(T);
    }
}