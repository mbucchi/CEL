package cel.compiler.visitors;

import cel.compiler.errors.DeclarationError;
import cel.compiler.errors.NameError;
import cel.event.EventSchema;
import cel.parser.CEPLBaseVisitor;
import cel.parser.CEPLParser;

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
                throw new DeclarationError("Event `" + eventName +
                        "` is referenced more than once within stream declaration", event);
            }

            EventSchema eventSchema = EventSchema.tryGetSchemaFor(eventName);

            // Event has not been declared
            if (eventSchema == null){
                throw new NameError("Event `" + eventName + "` is not defined", event);
            }
            eventNames.put(eventName, eventSchema);
        }

        return eventNames.values();
    }
}
