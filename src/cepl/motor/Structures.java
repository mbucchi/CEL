package cepl.motor;
import java.util.*;

class LLNode<T> {
    T value;
    LLNode<T> next;
    int i;
}

class Node {
    ExtensibleList next = null;
    int i;
    Long totalMatches = 1l;
    private Iterator<Node> it;

    private Node(){
        i = -1;
    }

    static Node Empty = new Node();
    
    public Node(int i, ExtensibleList next){
        this.i = i;
        this.next = next;
        if (next != null && next.totalMatches > 0){
            totalMatches = next.totalMatches;
        }
    }

    public void beginIter(){
        if (i > -1) it = next.iterator();
    }

    public boolean hasNext() {
        return i > - 1 && it.hasNext();
    }

    public Node next(){
        return it.next();
    }
    
    public boolean isEmpty(){
        return i == -1;
    }
}

class NXTNode {
    private NXTNode next = null;
    int i;

    private NXTNode(){
        i = -1;
    }

    static NXTNode Empty = new NXTNode();
    
    public NXTNode(int i, NXTNode next){
        this.i = i;
        this.next = next;
    }


    public NXTNode next(){
        return next;
    }
    
    public boolean isEmpty(){
        return i == -1;
    }
}

class ExtensibleList implements Iterable<Node> {
    private LLNode<Node> first, last;
    public long totalMatches = 0;


    public Node peekFirst() {
        return first.value;
    }

    public ExtensibleList(){
        first = last = null;
    }

    public ExtensibleList(Node value){
        first = last = new LLNode<Node>();
        first.value = value;
        totalMatches = value.totalMatches;
    }

    void add(Node element){
        LLNode<Node> newNode = new LLNode<Node>();
        newNode.value = element;
        totalMatches += element.totalMatches;
        if (first != null){
            newNode.next = first;
            first = newNode;
        }
        else {
            first = last = newNode;
        }
    }

    void extend(ExtensibleList other){
        totalMatches += other.totalMatches;
        if (last != null){
            last.next = other.first;
            if (other.last != null){
                last = other.last;
            }
        }
        else {
            first = other.first;
            last = other.last;
        }
    }

    public Iterator<Node> iterator(){
        return new LLNodeIter<Node>(first);
    }
}

class LLNodeIter<T> implements Iterator<T> {
    LLNode<T> current;
    public LLNodeIter(LLNode<T> first) {
        current = first;
    }

    public boolean hasNext() {
        return current != null;
    }

    public T next() {
        T val = current.value;
        current = current.next;
        return val;
    }

}

class Match implements Iterable<Event> {
    private LLNode<Event> first, last;

    public LLNode<Event> push(Event element) {
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

    public void popUntil(LLNode<Event> node) {
        first = node.next;
    }

    public Iterator<Event> iterator(){
        return new LLNodeIter<Event>(first);
    }

    public int size(){
        return first != null ? first.i : 0;
    }

    public void clear(){
        first = last = null;
    }

    public void pop(){
        first = first.next;
    }
}

class Order extends LinkedList<LinkedList<Integer>> {
    private boolean[] state_is_present;
    private LinkedList<Integer> states;

    public Order(int stateN){
        super();
        state_is_present = new boolean[stateN];
        states = new LinkedList<Integer>();
    }

    public boolean add(LinkedList<Integer> new_states){
        super.add(new_states);
        for (int q: new_states){
            state_is_present[q] = true;
            states.add(q);
        }
        return true;
    }

    public boolean containsState(int q){
        return state_is_present[q];
    }

    public LinkedList<Integer> getStates(){
        return states;
    }
}