package cel.compiler.visitors;

import cel.compiler.errors.DeclarationError;
import cel.event.EventSchema;
import cel.parser.CEPLBaseVisitor;
import cel.parser.CEPLParser;
import cel.stream.errors.StreamException;
import cel.stream.StreamSchema;

import java.util.*;

public class StreamDeclarationVisitor extends CEPLBaseVisitor<StreamSchema> {

    public StreamSchema visitStream_declaration(CEPLParser.Stream_declarationContext ctx){
        String streamName = ctx.stream_name().getText();
        Collection<EventSchema> eventSchemas = ctx.event_list().accept(new EventListVisitor());

        try {
            return new StreamSchema(streamName, eventSchemas);
        }
        catch (StreamException exc){
            // TODO: throw detailed error
            throw new DeclarationError(exc.getMessage(), ctx);
        }
    }
}


