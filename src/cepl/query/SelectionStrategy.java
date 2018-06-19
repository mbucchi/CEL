package cepl.query;

public enum SelectionStrategy {
    ALL,
    MAX,
    NEXT,
    LAST,
    STRICT;

    public static SelectionStrategy getDefault() {
        return ALL;
    }
}
