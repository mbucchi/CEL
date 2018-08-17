package cel.util;

import java.util.Iterator;

public class ExecutionNode {
    private ExtensibleList<ExecutionNode> next = null;
    private int i;
    private Long totalMatches;
    private Iterator<ExecutionNode> it;

    private ExecutionNode(){
        i = -1;
        totalMatches = 1L;
    }

    static ExecutionNode Empty = new ExecutionNode();

    public ExecutionNode(int i, ExtensibleList next){
        this.i = i;
        this.next = next;
        totalMatches = 1L;
//        if (next != null && next.totalMatches > 0){
//            totalMatches = next.totalMatches;
//        }
    }

    public void beginIter(){
        if (i > -1) it = next.iterator();
    }

    public boolean hasNext() {
        return i > - 1 && it.hasNext();
    }

    public ExecutionNode next(){
        return it.next();
    }

    public boolean isEmpty(){
        return i == -1;
    }
}
