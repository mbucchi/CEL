package cel.compiler.visitors;

import cel.compiler.errors.DeclarationError;
import cel.compiler.errors.NameError;
import cel.event.EventSchema;
import cel.parser.CELBaseVisitor;
import cel.parser.CELParser;
import cel.parser.utils.StringCleaner;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

class EventListVisitor extends CELBaseVisitor<Collection<EventSchema>> {

    public Collection<EventSchema> visitEvent_list(CELParser.Event_listContext ctx) {

        Map<String, EventSchema> eventNames = new HashMap<>();

        for (CELParser.Event_nameContext event : ctx.event_name()) {
            String eventName = StringCleaner.tryRemoveQuotes(event.getText());

            // event is declared more than once in the stream declaration
            if (eventNames.containsKey(eventName)) {
                throw new DeclarationError("event `" + eventName +
                        "` is referenced more than once within stream declaration", event);
            }

            EventSchema eventSchema = EventSchema.tryGetSchemaFor(eventName);

            // event has not been declared
            if (eventSchema == null) {
                throw new NameError("event `" + eventName + "` is not defined", event);
            }
            eventNames.put(eventName, eventSchema);
        }

        return eventNames.values();
    }
}
