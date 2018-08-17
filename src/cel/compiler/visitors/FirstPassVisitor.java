package cel.compiler.visitors;

import cel.compiler.errors.NameError;
import cel.event.EventSchema;
import cel.event.errors.EventException;
import cel.parser.CELBaseVisitor;
import cel.parser.CELParser;
import cel.parser.utils.StringCleaner;
import cel.stream.StreamSchema;
import cel.stream.errors.StreamException;

import java.util.HashSet;
import java.util.Set;

public class FirstPassVisitor extends CELBaseVisitor<Void> {

    private Set<StreamSchema> streamSchemas;
    private Set<EventSchema> eventSchemas;
    private int nAtomicPredicates;

    public FirstPassVisitor(){
        streamSchemas = new HashSet<>();
        eventSchemas = new HashSet<>();
        nAtomicPredicates = 0;
    }

    public Set<EventSchema> getEventSchemas() {
        return eventSchemas;
    }

    public Set<StreamSchema> getStreamSchemas() {
        return streamSchemas;
    }

    public int getnAtomicPredicates() {
        return nAtomicPredicates;
    }

    @Override
    public Void visitS_event_name(CELParser.S_event_nameContext ctx) {
        if (ctx.stream_name() != null) {
            String streamName = StringCleaner.tryRemoveQuotes(ctx.stream_name().getText());
            StreamSchema streamSchema;

            try {
                streamSchema = StreamSchema.getSchemaFor(streamName);
            } catch (StreamException e) {
                throw new NameError("Stream `" + streamName + "` is not defined", ctx.stream_name());
            }
            streamSchemas.add(streamSchema);
        }

        String eventName = StringCleaner.tryRemoveQuotes(ctx.event_name().getText());
        EventSchema eventSchema;
        try{
            eventSchema = EventSchema.getSchemaFor(eventName);
        }
        catch (EventException e){
            throw new NameError("event `" + eventName + "` is not defined", ctx.event_name());
        }

        // Add the newly found schema to the set of visited schemas
        eventSchemas.add(eventSchema);
        return null;
    }

    @Override
    public Void visitEvent_filter(CELParser.Event_filterContext ctx) {
        nAtomicPredicates++;
        return super.visitEvent_filter(ctx);
    }
}
