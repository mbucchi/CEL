import cel.event.EventSchema;
import cel.runtime.event.Event;
import cel.values.ValueType;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class EventParser {

    private static AtomicInteger idx = new AtomicInteger(0);
    private static int METADATA_ATTRIBUTES = 4;
    public static long parseTime = 0;
    public static long otherTime = 0;

    public static Event parseEvent(String line, Map<String, EventSchema> events) {
        long t = System.nanoTime();
        String values[] = line.substring(0, line.length() - 1).split("\\(");
//        String values[] = line.split("[()]"); /* This is 3-5 times slower compared to the code above */
        String name = values[0];
        values = values[1].split(",");

        Event e;

        EventSchema ev = events.get(name);

        otherTime += System.nanoTime() - t;
        t = System.nanoTime();

        Object[] attrs = new Object[ev.getAttributes().size()];
        addMetaData(attrs);
        for (int i = METADATA_ATTRIBUTES; i < ev.getAttributes().size(); i++) {
            String[] temp = values[i - METADATA_ATTRIBUTES].split("=");
            String n = temp[0];
            String val = temp[1];
            ValueType valueType = ev.getAttributes().get(n);
            if (valueType == ValueType.INTEGER) {
                attrs[i] = Integer.parseInt(val);
            } else if (valueType == ValueType.DOUBLE) {
                attrs[i] = Double.parseDouble(val);
            } else if (valueType == ValueType.LONG) {
                attrs[i] = Long.parseLong(val);
            } else {
                attrs[i] = val;
            }
        }
        try {
            e = Event.newEvent(name, attrs);
            parseTime += System.nanoTime() - t;
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
        /* type (currently unused) */
        attrs[3] = 4;
    }
}
