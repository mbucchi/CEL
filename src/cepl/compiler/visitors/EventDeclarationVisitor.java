package cepl.compiler.visitors;

import cepl.compiler.utils.DataType;
import cepl.event.EventException;
import cepl.event.EventSchema;
import cepl.parser.CEPLBaseVisitor;
import cepl.parser.CEPLParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EventDeclarationVisitor extends CEPLBaseVisitor<EventSchema> {

    public EventSchema visitEvent_declaration(CEPLParser.Event_declarationContext ctx) {
        String eventName = ctx.event_name().getText();
        Map<String, Class> attributeMap = ctx.attribute_dec_list().accept(new AttributeDeclarationVisitor());

        try {
            return new EventSchema(eventName, attributeMap);
        }
        catch (EventException exc){
            // TODO: throw detailed error
            throw new Error(exc.getMessage());
        }
    }
}

