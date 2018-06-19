package cel.compiler.visitors;

import cel.compiler.errors.DeclarationError;
import cel.event.errors.EventException;
import cel.event.EventSchema;
import cel.parser.CEPLBaseVisitor;
import cel.parser.CEPLParser;

import java.util.Map;

public class EventDeclarationVisitor extends CEPLBaseVisitor<EventSchema> {

    public EventSchema visitEvent_declaration(CEPLParser.Event_declarationContext ctx) {
        String eventName = ctx.event_name().getText();
        Map<String, Class> attributeMap = ctx.attribute_dec_list().accept(new AttributeDeclarationVisitor());

        try {
            return new EventSchema(eventName, attributeMap);
        }
        catch (EventException exc){
            throw new DeclarationError(exc.getMessage(), ctx);
        }
    }
}

