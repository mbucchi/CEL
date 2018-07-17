package cel.util;

import java.util.Iterator;

public class ExtensibleListIterator<T> implements Iterator<T> {
    private LNode<T> current;

    ExtensibleListIterator(LNode<T> first) {
        current = first;
    }

    public boolean hasNext() {
        return current != null;
    }

    public T next() {
        T val = current.getValue();
        current = current.next();
        return val;
    }
}
