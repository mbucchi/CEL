import cel.event.EventSchema;
import cel.query.Query;
import cel.runtime.source.EventSourceGenerator;

import java.util.HashMap;
import java.util.Map;

public class EventsCodeGetter {

    public static Map<String, String> getEventsCode(Query q) {
        Map<String, String> ret = new HashMap<>();
        for (EventSchema ev : q.getPatternCEA().getEventSchemas()) {
            ret.put(ev.getName(), EventSourceGenerator.createEventSource(ev));
        }
        return ret;
    }

}
