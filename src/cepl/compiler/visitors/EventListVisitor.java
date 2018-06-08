package cepl.compiler.visitors;

import cepl.event.EventSchema;
import cepl.parser.CEPLBaseVisitor;
import cepl.parser.CEPLParser;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class EventListVisitor extends CEPLBaseVisitor<Collection<EventSchema>> {

    public Collection<EventSchema> visitEvent_list(CEPLParser.Event_listContext ctx){

        Map<String, EventSchema> eventNames = new HashMap<>();

        for (CEPLParser.Event_nameContext event : ctx.event_name()) {
            String eventName = event.getText();

            // Event is declared more than once in the stream declaration
            if (eventNames.containsKey(eventName)) {
                // TODO: throw detailed error
                continue;
            }

            EventSchema eventSchema = EventSchema.getSchemaFor(eventName);

            // Event has not been declared
            if (eventSchema == null){

            }

            eventNames.put(eventName, EventSchema.getSchemaFor(eventName));
        }

        return eventNames.values();
    }
}
