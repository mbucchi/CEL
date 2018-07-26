package cel.runtime.event;

public class Event {

    public long __ts;
    public int __stream;
    public long __idx;
//    public int __type;

    public Event(long __ts, int __stream, long __idx) {
        this.__ts = __ts;
        this.__stream = __stream;
        this.__idx = __idx;
    }

}