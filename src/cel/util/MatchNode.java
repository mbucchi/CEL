package cel.util;

import java.util.Iterator;

class MatchNode {
    private MatchNodeList next;
    private int i;
    private long totalMatches = 1L;
    private Iterator<MatchNode> it;

    private MatchNode(){
        i = -1;
    }

    static MatchNode Empty = new MatchNode();

    public MatchNode(int i, MatchNodeList next){
        this.i = i;
        this.next = next;
        if (next != null && next.getTotalMatches() > 0){
            totalMatches = next.getTotalMatches();
        }
    }

    public long getTotalMatches() {
        return totalMatches;
    }

    public void beginIter(){
        if (i > -1) it = next.iterator();
    }

    public boolean hasNext() {
        return i > - 1 && it.hasNext();
    }

    public MatchNode next(){
        return it.next();
    }

    public boolean isEmpty(){
        return i == -1;
    }
}
