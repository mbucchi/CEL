package cepl.filter;

public interface FilterComparable {
    boolean equivalentTo(FilterComparable filter);
    boolean dominates(FilterComparable filter);
}
