package cel.compiler.visitors;

import cel.compiler.errors.DeclarationError;
import cel.event.errors.EventException;
import cel.event.EventSchema;
import cel.parser.CELBaseVisitor;
import cel.parser.CELParser;
import cel.parser.utils.StringCleaner;
import cel.values.ValueType;

import java.util.Map;

public class EventDeclarationVisitor extends CELBaseVisitor<EventSchema> {

    public EventSchema visitEvent_declaration(CELParser.Event_declarationContext ctx) {
        String eventName = StringCleaner.tryRemoveQuotes(ctx.event_name().getText());
        Map<String, ValueType> attributeMap = ctx.attribute_dec_list().accept(new AttributeDeclarationVisitor());

        try {
            return new EventSchema(eventName, attributeMap);
        }
        catch (EventException exc){
            throw new DeclarationError(exc.getMessage(), ctx);
        }
    }
}

