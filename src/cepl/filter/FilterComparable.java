package cepl.filter;

public interface FilterComparable {
    public boolean equivalentTo(FilterComparable filter);
    public boolean dominates(FilterComparable filter);
}
