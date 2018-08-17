package cel.util;

import java.util.Iterator;

public class ExtensibleList<T> implements Iterable<T> {
    private LNode<T> first, last;

    public T peekFirst() {
        return first.getValue();
    }

    public ExtensibleList(){
        first = last = null;
    }

    ExtensibleList(T value){
        first = last = new LNode<>(value);
    }

    public void add(T element){
        LNode<T> newNode = new LNode<>(element);
        if (first != null){
            newNode.setNext(first);
            first = newNode;
        }
        else {
            first = last = newNode;
        }
    }

    void extend(ExtensibleList<T> other){
        if (last != null){
            last.setNext(other.first);
            if (other.last != null){
                last = other.last;
            }
        }
        else {
            first = other.first;
            last = other.last;
        }
    }

    public Iterator<T> iterator(){
        return new ExtensibleListIterator<>(first);
    }
}