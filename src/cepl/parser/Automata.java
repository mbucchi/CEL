package cepl.parser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
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
    private  Map<String, Set<String>> relations;


    public Automata(ASTNode node, Map<String, String> varRelationMap, Map<String, Set<String>> relations) throws Exception{
        q0 = stateN = 0;
        finalStates = new HashSet<Integer>();
        blacks = new ArrayList<Transition>();
        whites = new ArrayList<Transition>();
        this.varRelationMap = varRelationMap;
        this.relations = relations;

        switch(node.type){
            case MAX:
                copyFrom(new Automata(node.children.getFirst(), varRelationMap, relations));
                semantic = NodeType.ANY;
                maxify();
                break;
            case ANY:
            case NXT:
            case LAST:
            case STRICT:
                copyFrom(new Automata(node.children.getFirst(), varRelationMap, relations));
                semantic = node.type;
                break;
            case OR:
                ORAutomata(new Automata(node.children.getFirst(), varRelationMap, relations), 
                           new Automata(node.children.getLast(), varRelationMap, relations));
                break;
            case SEQ:
                SEQAutomata(new Automata(node.children.getFirst(), varRelationMap, relations), 
                            new Automata(node.children.getLast(), varRelationMap, relations));
                break;
            case KLEENE:
                KleeneAutomata(new Automata(node.children.getFirst(), varRelationMap, relations));
                break;
            case FILTER:
                FilterAutomata(new Automata(node.children.getFirst(), varRelationMap, relations), node.children.getLast());
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

    private Map<Integer, Set<Integer>> getReachableFromMap(){

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

        return reachableFrom;
    }

    private void removeUselessStates(){
        // calculate reachable states
        Map<Integer, Set<Integer>> reachableFrom = getReachableFromMap();

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
                Transition posibleNew = new Transition(newNames[t.from], t.evType);
                posibleNew.formula = t.formula;
                for (int q: t.to){
                    if (usefulStates.contains(q)) posibleNew.addTo(newNames[q]);
                }
                if (posibleNew.to.size() > 0) newBlacks.add(posibleNew);
            }
        }

        for (Transition t: whites){
            if (usefulStates.contains(t.from)){
                Transition posibleNew = new Transition(newNames[t.from], t.evType);
                posibleNew.formula = t.formula;                
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
                Transition newT = new Transition(0, t.evType, t.to);
                newT.formula = t.formula;
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
                Transition newT = new Transition(0, t.evType, t.to);
                newT.formula = t.formula;
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

        for (String ev: relations.keySet()){
            whites.add(new Transition(0, ev, 0));
        }

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
        for (String ev: relations.keySet()){
            whites.add(new Transition(0, ev, 0));
        }
        Transition prev = left.blacks.get(0);        
        prev.addFormula(getPredicate(predicate));
        blacks.add(prev);
    }

    private void AssignAutomata(ASTNode node) throws Exception{
        stateN = 2;
        q0 = 0;
        finalStates.add(1);
        String evType = node.children.getFirst().value.sequence;
        blacks.add(new Transition(0, evType, 1));
        for (String ev: relations.keySet()){
            whites.add(new Transition(0, ev, 0));
        }
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
                Token property = first.children.getLast().value;
                String var = first.children.getFirst().value.sequence;
                String rel = varRelationMap.get(var);
                String value = last.value.sequence;
                String op = predicate.children.get(1).value.sequence;

                if (!relations.get(rel).contains(property.sequence)){
                    property.throwParseError("AttributeError: Event of type \"" + rel + "\" has no attribute \"" + property.sequence + "\"");
                }
                
                if (op.equals("=")){
                    return "((" + rel +  ")e)." + property.sequence + ".equals(" + value + ")";
                }
                else if (op.equals("!=")){
                    return "!((" + rel +  ")e)." + property.sequence + ".equals(" + value + ")";
                }
                else {
                    return "((" + rel +  ")e)." + property.sequence + " " + op + " " + value;
                }
            default:
                throw new Exception("Error building filter formula!");
        }
    }

    private void maxify(){
        Set<MaxTuple> visited = new HashSet<MaxTuple>();
        List<MaxTuple> current = new LinkedList<MaxTuple>();
        List<MaxTuple> states = new LinkedList<MaxTuple>();
        
        MaxTuple init = new MaxTuple(new HashSet<Integer>(), new HashSet<Integer>());
        init.T.add(q0);
        
        current.add(init);
        states.add(init);
        
        Map<String, Map<String, List<Transition>>> blackMap = new HashMap<String, Map<String, List<Transition>>>();
        List<MaxTuple> finals = new LinkedList<MaxTuple>();

        Set<String> evTypes = relations.keySet();

        List<MaxTransition> maxBlacks = new LinkedList<MaxTransition>();
        List<MaxTransition> maxWhites = new LinkedList<MaxTransition>();

        for (String ev: evTypes){
            blackMap.put(ev, new HashMap<String, List<Transition>>());
            blackMap.get(ev).put("", new LinkedList<Transition>());
        }

        for (Transition t: blacks){
            Map<String, List<Transition>> evMap = blackMap.get(t.evType);
            if (!evMap.containsKey(t.getFormula())){
                evMap.put(t.getFormula(), new LinkedList<Transition>());
            }
            evMap.get(t.getFormula()).add(t);
        }

        while (current.size() > 0){
            MaxTuple curr = current.remove(0);
            if (curr.isFinal){
                finals.add(curr);
                continue;
            }

            Set<Integer> TW = new HashSet<Integer>();
            Set<Integer> UW = new HashSet<Integer>();

            for (Transition wt: whites){
                if (curr.T.contains(wt.from)){
                    TW.addAll(wt.to);
                }
                if (curr.U.contains(wt.from)){
                    UW.addAll(wt.to);
                }
            }

            for (String ev: evTypes){
                
                Map<String, List<Transition>> evMap = blackMap.get(ev);
                
                for (String form: evMap.keySet()){
                    Set<Integer> TB = new HashSet<Integer>();
                    Set<Integer> UB = new HashSet<Integer>();

                    for (Transition bt: evMap.get(form)){
                        if (curr.T.contains(bt.from)){
                            TB.addAll(bt.to);
                        }
                        if (curr.U.contains(bt.from)){
                            UB.addAll(bt.to);
                        }
                    }

                    Set<Integer> newUB = new HashSet<Integer>(UB);
                    Set<Integer> newTB = new HashSet<Integer>(TB);
                    newTB.removeAll(newUB);

                    if (newTB.size() > 0){
                        MaxTuple newTup = new MaxTuple(newTB, newUB);
                        
                        for (int q: newTup.T){
                            if (finalStates.contains(q)){
                                newTup.isFinal = true;
                                break;
                            }
                        }

                        if (!visited.contains(newTup)){
                            visited.add(newTup);
                            current.add(newTup);
                            states.add(newTup);
                        }

                        maxBlacks.add(new MaxTransition(curr, ev, form, newTup));
                    }

                    Set<Integer> newUW = new HashSet<Integer>(UB);
                    newUW.addAll(UW);
                    newUW.addAll(TB);
                    Set<Integer> newTW = new HashSet<Integer>(TW);
                    newTW.removeAll(newUW);
        
                    if (newTW.size() > 0){
                        MaxTuple newTup = new MaxTuple(newTW, newUW);
                        
                        for (int q: newTup.T){
                            if (finalStates.contains(q)){
                                newTup.isFinal = true;
                                break;
                            }
                        }
    
                        if (!visited.contains(newTup)){
                            visited.add(newTup);
                            current.add(newTup);
                            states.add(newTup);
                        }
                        maxWhites.add(new MaxTransition(curr, ev, form, newTup)); 
                    }
                }
            }
        }

        stateN = states.size();
        q0 = 0;
        finalStates = new HashSet<Integer>();


        for (MaxTuple mt: finals){
            finalStates.add(states.indexOf(mt));
        }

        blacks = new ArrayList<Transition>();
        whites = new ArrayList<Transition>();

        for (MaxTransition mt: maxBlacks){
            Transition trans = new Transition(states.indexOf(mt.from), mt.evType, states.indexOf(mt.to));
            trans.formula = mt.formula;
            blacks.add(trans);
        }

        for (MaxTransition mt: maxWhites){
            Transition trans = new Transition(states.indexOf(mt.from), mt.evType, states.indexOf(mt.to));
            trans.formula = mt.formula;            
            whites.add(trans);
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
    String evType;

    public Transition(int p, String evType, int q){
        to = new HashSet<Integer>();
        from = p;
        this.evType = evType;
        to.add(q);
    }

    public Transition(int p, String evType, Set<Integer> to){
        this.to = to;
        from = p;
        this.evType = evType;
    }

    public Transition(int p, String evType){
        to = new HashSet<Integer>();
        from = p;
        this.evType = evType;
    }

    public Transition(int p, int q){
        this(p, null, q);
    }
    public Transition(int p){
        to = new HashSet<Integer>();
        from = p;
        formula = null;
        evType = null;
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

    public void addFormula(String formula){
        if (this.formula == null){
            this.formula = formula;
        }
        else {
            this.formula += " && " + formula;
        }
    }

    public String getFormula(){
        return formula == null ? "" : formula;
    }

    public String getEvType(){
        return evType == null ? "" : evType;
    }

}


class MaxTuple {
    public final Set<Integer> T;        
    public final Set<Integer> U;
    public boolean isFinal;

    public MaxTuple(Set<Integer> T, Set<Integer> U){
        this.T = new HashSet<Integer>(T);
        this.U = new HashSet<Integer>(U);
    }

    @Override
    public final int hashCode(){
        return 31 + this.T.hashCode() + 17 * this.U.hashCode();
    }

    @Override
    public final boolean equals(final Object obj){
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final MaxTuple other = (MaxTuple) obj;
        return T.equals(other.T) && U.equals(other.U);
    }
}

class MaxTransition {
    MaxTuple from;
    String evType;
    String formula;
    MaxTuple to;

    public MaxTransition(MaxTuple from, String evType, String formula, MaxTuple to){
        this.from = from;
        this.to = to;
        this.evType = evType;
        this.formula = formula;
    }
}