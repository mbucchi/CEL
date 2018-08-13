package cel.runtime;

import cel.runtime.event.Event;

import java.util.*;

public class MatchGrouping implements Iterable<Match> {
    private LinkedList<ExtensibleList> final_lists;
    private NXTNode final_node;
    private long totalMatches;
    private Semantic semantic;
    private Hashtable<Integer, Event> usefulValues;
    private Event lastEvent;

    MatchGrouping(Semantic semantic, Hashtable<Integer, Event> usefulValues, int i){
        this.semantic = semantic;
        this.usefulValues = usefulValues;
        this.lastEvent = usefulValues.get(i);
        totalMatches = 0;
        if (semantic == Semantic.NXT || semantic == Semantic.LAST) {
            final_node = NXTNode.Empty;
        }
        else if (semantic == Semantic.ANY || semantic == Semantic.MAX || semantic == Semantic.STRICT){
            final_lists = new LinkedList<ExtensibleList>();
        }
    }

    void addFinal(ExtensibleList final_list){
        final_lists.add(final_list);
        totalMatches += final_list.totalMatches;
    }

    void addFinal(NXTNode final_node){
        this.final_node = final_node;
        this.totalMatches = 1;
    }

    public long size(){
        return totalMatches;
    }

    public Event lastEvent(){
        return lastEvent;
    }

    public Iterator<Match> iterator(){
        if (semantic == Semantic.NXT || semantic == Semantic.LAST){
            return new MatchGroupingIterator(final_node, usefulValues);
        }
        else if (semantic == Semantic.ANY || semantic == Semantic.MAX || semantic == Semantic.STRICT ){
            return new MatchGroupingIterator(final_lists, usefulValues);
        }
        // this should not be happening ever
        return null;
    }
}

class MatchGroupingIterator implements Iterator<Match> {
    private Match matchStack;
    private Semantic semantic;
    private Hashtable<Integer, Event> usefulValues;

    private NXTNode final_node;

    private Iterator<ExtensibleList> final_list_iter;
    private Iterator<Node> curr_final_list;
    private Stack<Iterator<Node>> nodeStack;
    private Stack<LLNode<Event>> jumpStack;
    private LLNode<Event> lastEv;
    private Node aux, current;
    private Iterator<Node> it;

    private boolean first_part;


    protected MatchGroupingIterator(NXTNode final_node, Hashtable<Integer, Event> usefulValues){
        this.usefulValues = usefulValues;
        this.final_node = final_node;
        matchStack = new Match();
        semantic = Semantic.NXT;
    }

    protected MatchGroupingIterator(LinkedList<ExtensibleList> final_lists, Hashtable<Integer, Event> usefulValues){
        this.usefulValues = usefulValues;
        final_list_iter = final_lists.iterator();
        curr_final_list = final_list_iter.next().iterator();
        semantic = Semantic.ANY;
        matchStack = new Match();
        nodeStack = new Stack<Iterator<Node>>();
        jumpStack = new Stack<LLNode<Event>>();

    }

    public boolean hasNext(){
        if (semantic == Semantic.NXT){
            return !final_node.isEmpty();
        }
        if (semantic == Semantic.ANY){
            return !nodeStack.isEmpty() || curr_final_list.hasNext() || final_list_iter.hasNext() ;
        }
        return false;
    }

    public Match next(){

        if (semantic == Semantic.ANY) {
            // Any enumeration algorithm

            if (nodeStack.empty()){
                if (!curr_final_list.hasNext()){
                    curr_final_list = final_list_iter.next().iterator();
                }
                current = curr_final_list.next();
                matchStack.clear();
                jumpStack.clear();
                lastEv = matchStack.push(usefulValues.get(current.i));
                first_part = true;
            }


            if (!first_part){
                matchStack.popUntil(lastEv);
                if (!nodeStack.empty()){
                    it = nodeStack.pop();
                    lastEv = jumpStack.pop();
                    current = it.next();
                    if (current.isEmpty()){
                        return matchStack;
                    }
                    matchStackPush();
                    while (true){
                        it = current.next.iterator();
                        current = it.next();
                        if (current.isEmpty()){
                            return matchStack;
                        }
                        matchStackPush();
                    }
                }
            }

            while (first_part){
                it = current.next.iterator();
                current = it.next();
                if (current.isEmpty()){
                    first_part = false;
                    return matchStack;
                }
                matchStackPush();
            }
        }
        else if (semantic == Semantic.NXT){
            // Nxt enumeration algorithm
            for (; !final_node.isEmpty(); final_node = final_node.next() ){
                matchStack.push(usefulValues.get(final_node.i));
            }
        }
        return matchStack;
    }

    private void matchStackPush() {
        if (it.hasNext()){
            nodeStack.push(it);
            jumpStack.push(lastEv);
            lastEv = matchStack.push(usefulValues.get(current.i));
        }
        else {
            matchStack.push(usefulValues.get(current.i));
        }
    }
}