import cel.event.EventSchema;
import cel.runtime.event.Event;
import cel.values.ValueType;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class EventParser {

    private static AtomicInteger idx = new AtomicInteger(0);
    private static int METADATA_ATTRIBUTES = 4;

    public static Event parseEvent(String line, Set<EventSchema> events) {
        String values[] = line.split("[()]");
        String name = values[0];
        values = values[1].split(",");

        Event e;
        Object attrs[] = null;

        for (EventSchema ev : events) {
            if (line.startsWith(ev.getName())) {
                attrs = new Object[ev.getAttributes().size()];
                addMetaData(attrs);
                for (int i = METADATA_ATTRIBUTES; i < ev.getAttributes().size(); i++) {
                    String n = values[i - METADATA_ATTRIBUTES].split("=")[0];
                    String val = values[i - METADATA_ATTRIBUTES].split("=")[1];
                    if (ev.getAttributes().get(n) == ValueType.INTEGER) {
                        attrs[i] = Integer.parseInt(val);
                    } else if (ev.getAttributes().get(n) == ValueType.DOUBLE) {
                        attrs[i] = Double.parseDouble(val);
                    } else if (ev.getAttributes().get(n) == ValueType.LONG) {
                        attrs[i] = Long.parseLong(val);
                    } else {
                        attrs[i] = val;
                    }
                }
                break;
            }
        }
        try {
            e = Event.newEvent(name, attrs);
            return e;

        } catch (Exception err) {
            err.printStackTrace();
        }
        return null;
    }

    private static void addMetaData(Object... attrs) {
        /* stream */
        attrs[0] = 1;
        /* index */
        attrs[1] = (long) idx.getAndIncrement();
        /* timestamp */
        attrs[2] = System.currentTimeMillis();
        /* type */
        attrs[3] = 4;
    }
}
