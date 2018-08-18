package cel.runtime.errors;

public class DoubleInitializationError extends Error {
    public DoubleInitializationError(String msg) {
            super(msg);
        }
}
