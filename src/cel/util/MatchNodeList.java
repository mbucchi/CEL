package cel.util;

public class MatchNodeList extends ExtensibleList<MatchNode> {
    private long totalMatches;

    public MatchNodeList() {
        super();
    }

    public MatchNodeList(MatchNode value){
        super(value);
    }


    @Override
    public void add(MatchNode element) {
        super.add(element);
        totalMatches += element.getTotalMatches();
    }

    @Override
    void extend(ExtensibleList<MatchNode> other) {
        // TODO: specific error
        throw new Error("woops... shouldn't have gotten here");
    }

    void extend(MatchNodeList other) {
        super.extend(other);
        totalMatches += other.totalMatches;
    }

    long getTotalMatches() {
        return totalMatches;
    }
}
