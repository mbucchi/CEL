package cel.runtime;

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

    Node(int i, ExtensibleList next){
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

    boolean isEmpty(){
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

    NXTNode(int i, NXTNode next){
        this.i = i;
        this.next = next;
    }


    public NXTNode next(){
        return next;
    }

    boolean isEmpty(){
        return i == -1;
    }
}

class ExtensibleList implements Iterable<Node> {
    private LLNode<Node> first;
    LLNode<Node> last;
    long totalMatches = 0;


    public Node peekFirst() {
        return first.value;
    }

    ExtensibleList(){
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
        return new LLNodeIter<Node>(first, this);
    }
}

class LLNodeIter<T> implements Iterator<T> {
    private LLNode<T> current;
    private ExtensibleList list;

    LLNodeIter(LLNode<T> first, ExtensibleList list) {
        current = first;
        this.list = list;
    }

    LLNodeIter(LLNode<T> first) {
        current = first;
    }

    public boolean hasNext() {
        if (list != null)
            // return current != null && (list.last.next == null || !list.last.next.equals(current));
            return current != null && !current.equals(list.last.next);
        return current != null;
    }

    public T next() {
        T val = current.value;
        current = current.next;
        return val;
    }

}

class Order extends LinkedList<LinkedList<Integer>> {
    private boolean[] state_is_present;
    private LinkedList<Integer> states;

    Order(int stateN){
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

    boolean containsState(int q){
        return state_is_present[q];
    }

    LinkedList<Integer> getStates(){
        return states;
    }
}

class MaxTuple {
    public final Set<Integer> T;
    final Set<Integer> U;
    boolean isFinal;

    MaxTuple(Set<Integer> T, Set<Integer> U){
        this.T = new HashSet<>();
        if (T != null) {
            this.T.addAll(T);
        }

        this.U = new HashSet<>();
        if (U != null) {
            this.U.addAll(U);
        }
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

    @Override
    public final String toString(){
        return "MaxTuple(T=" + T + ", U=" + U + ")";
    }
}

class DetNode {
    public final Set<Integer> T;
    boolean isFinal;

    DetNode(Set<Integer> T){
        this.T = new HashSet<>(T);
    }

    @Override
    public final int hashCode(){
        return this.T.hashCode();
    }

    @Override
    public final boolean equals(final Object obj){
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final DetNode other = (DetNode) obj;
        return T.equals(other.T);
    }

    @Override
    public final String toString(){
        return "DetNode(T=" + T + ")";
    }
}