package cel.query;

public class TimeWindow {
    public enum Kind {
        EVENTS,
        TIME,
        NONE
    }

    private Kind kind;
    private long span;

    private TimeWindow(){
        kind = Kind.NONE;
    }

    public TimeWindow(long span, Kind kind){
        if (kind == Kind.NONE){
            throw new Error("use `TimeWindow.NONE` instead");
        }
        this.span = span;
        this.kind = kind;
    }

    public static final TimeWindow NONE = new TimeWindow();
}
