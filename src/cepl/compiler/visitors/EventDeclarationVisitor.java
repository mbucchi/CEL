package cepl.compiler.visitors;

import cepl.compiler.errors.DeclarationError;
import cepl.event.errors.EventException;
import cepl.event.EventSchema;
import cepl.parser.CEPLBaseVisitor;
import cepl.parser.CEPLParser;

import java.util.Map;

public class EventDeclarationVisitor extends CEPLBaseVisitor<EventSchema> {

    public EventSchema visitEvent_declaration(CEPLParser.Event_declarationContext ctx) {
        String eventName = ctx.event_name().getText();
        Map<String, Class> attributeMap = ctx.attribute_dec_list().accept(new AttributeDeclarationVisitor());

        try {
            return new EventSchema(eventName, attributeMap);
        }
        catch (EventException exc){
            throw new DeclarationError(exc.getMessage());
        }
    }
}

