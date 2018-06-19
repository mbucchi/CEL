package cepl.query;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Partition {

    private Set<String> attributes;

    public Partition(Collection<String> attributes) {
        this.attributes = new HashSet<>(attributes);
    }
}
