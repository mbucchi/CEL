package cel.event.errors;

public abstract class EventError extends Error {
    public EventError(String msg) {
        super(msg);
    }
}
