package cel.util;

class LNode<T> {

    private T value;

    private LNode<T> _next;

    LNode(T value){
        this.value = value;
    }

    T getValue() {
        return value;
    }

    LNode<T> next() {
        return _next;
    }

    void setNext(LNode<T> next) {
        _next = next;
    }
 }
