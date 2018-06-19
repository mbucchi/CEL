package cepl.query;

import cepl.event.Label;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class ProjectionList {
    private Set<Label> labels;
    private boolean matchAll;

    public ProjectionList() {
        matchAll = true;
    }

    public ProjectionList(Collection<Label> labels) {
        matchAll = false;
        this.labels = new HashSet<>(labels);
    }

    public boolean contains(Label label) {
        if (matchAll) return true;
        return labels.contains(label);
    }

}
