package cepl.compiler.visitors;

import cepl.compiler.utils.DataType;
import cepl.event.EventSchema;
import cepl.parser.CEPLBaseVisitor;
import cepl.parser.CEPLParser;
import cepl.stream.StreamException;
import cepl.stream.StreamSchema;

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
            throw new Error(exc.getMessage());
        }
    }
}


