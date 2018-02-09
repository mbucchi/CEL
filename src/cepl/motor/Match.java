package cepl.motor;

import java.util.Iterator;

public class Match implements Iterable<Event> {
    private LLNode<Event> first, last;

    protected LLNode<Event> push(Event element) {
        LLNode<Event> newNode = new LLNode<Event>();
        newNode.value = element;
        if (first != null){
            newNode.i = first.i + 1;
            newNode.next = first;
            first = newNode;
        }
        else {
            newNode.i = 1;
            first = last = newNode;
        }
        return newNode;
    }

    protected void popUntil(LLNode<Event> node) {
        first = node.next;
    }

    public Iterator<Event> iterator(){
        return new LLNodeIter<Event>(first);
    }

    public int size(){
        return first != null ? first.i : 0;
    }

    protected void clear(){
        first = last = null;
    }

    protected void pop(){
        first = first.next;
    }
}