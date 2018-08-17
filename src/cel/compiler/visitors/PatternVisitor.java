package cel.compiler.visitors;

import cel.cea.*;
import cel.compiler.errors.NameError;
import cel.compiler.errors.UnknownStatementError;
import cel.event.EventSchema;
import cel.event.Label;
import cel.event.errors.EventException;
import cel.filter.Filter;
import cel.parser.CELBaseVisitor;
import cel.parser.CELParser;
import cel.parser.utils.StringCleaner;
import cel.stream.StreamSchema;
import cel.stream.errors.StreamException;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class PatternVisitor extends CELBaseVisitor<CEA> {

    private Collection<String> definedStreams;
    private Collection<String> definedEvents;

    private Set<EventSchema> eventSchemas;

    public PatternVisitor(Collection<String> definedStreams, Collection<String> definedEvents) {
        this.definedStreams = definedStreams;
        this.definedEvents = definedEvents;
        eventSchemas = new HashSet<>();
    }


    @Override
    public CEA visitPar_cel_pattern(CELParser.Par_cel_patternContext ctx) {
        // The context remains the same, we just ignore the parenthesis.
        // Get the child and visit without the need to instantiate a new visitor.
        return ctx.cel_pattern().accept(this);
    }


    @Override
    public CEA visitBinary_cel_pattern(CELParser.Binary_cel_patternContext ctx) {
        PatternVisitor visitorLeft = new PatternVisitor(definedStreams, definedEvents);
        PatternVisitor visitorRight = new PatternVisitor(definedStreams, definedEvents);

        // get the left and right patterns

        CEA left = ctx.cel_pattern(0).accept(visitorLeft);
        CEA right = ctx.cel_pattern(1).accept(visitorRight);

        // add the newly found event schemas

        eventSchemas.addAll(visitorLeft.eventSchemas);
        eventSchemas.addAll(visitorRight.eventSchemas);

        // binary could be either `OR` or `;`.

        if (ctx.K_OR() != null) {
            return new OrCEA(left, right);
        } else if (ctx.SEMICOLON() != null) {
            return new SequenceCEA(left, right);
        } else {
            throw new UnknownStatementError("Binary operator unknown", ctx);
        }
    }

    @Override
    public CEA visitKleene_cel_pattern(CELParser.Kleene_cel_patternContext ctx) {
        // the context remains the same, no need for creating a new visitor
        PatternVisitor newVisitor = this;

        CEA inner = ctx.cel_pattern().accept(newVisitor);

        return new KleeneCEA(inner);
    }

    @Override
    public CEA visitAssign_cel_pattern(CELParser.Assign_cel_patternContext ctx) {
        // the context remains the same, no need for creating a new visitor
        PatternVisitor newVisitor = this;

        CEA inner = ctx.cel_pattern().accept(newVisitor);

        String newLabel = StringCleaner.tryRemoveQuotes(ctx.event_name().getText());
        Label label = Label.forName(newLabel, eventSchemas);

        return new AssignCEA(inner, label);
    }

    @Override
    public CEA visitEvent_cel_pattern(CELParser.Event_cel_patternContext ctx) {

        // event can either be named by itself or within the scope of a stream.
        // First check which is the case and deal with each of them separately
        String eventName = StringCleaner.tryRemoveQuotes(ctx.s_event_name().event_name().getText());

        // Check if a stream is defined
        if (ctx.s_event_name().stream_name() != null) {
            String streamName = StringCleaner.tryRemoveQuotes(ctx.s_event_name().stream_name().getText());
            StreamSchema streamSchema;
            EventSchema eventSchema;

            try {
                streamSchema = StreamSchema.getSchemaFor(streamName);
            }
            catch (StreamException e) {
                throw new NameError("Stream `" + streamName + "` is not defined", ctx.s_event_name().stream_name());

            }
            if (!streamSchema.containsEvent(eventName)) {
                throw new NameError("event `" + eventName + "` is not defined within stream `" + streamName + "`",
                        ctx.s_event_name().event_name());
            }

            // Create a selection CEA that filters for the given stream
            try{
                eventSchema = EventSchema.getSchemaFor(eventName);
            }
            catch (EventException e){
                throw new NameError("event `" + eventName + "` is not defined", ctx.s_event_name().event_name());
            }

            // Add the newly found schema to the set of visited schemas
            eventSchemas.add(eventSchema);

            return new SelectionCEA(streamSchema, eventSchema);
        } else {
            // no stream is defined, just check that the event is declared within the scope of the
            // query
            if (!definedEvents.contains(eventName)) {
                throw new NameError("event `" + eventName + "` is not defined within any of the query streams",
                        ctx.s_event_name());
            }

            // Create a selection CEA with no filters
            EventSchema eventSchema;
            try{
                eventSchema = EventSchema.getSchemaFor(eventName);
            }
            catch (EventException e){
                throw new NameError("event `" + eventName + "` is not defined", ctx.s_event_name().event_name());
            }

            // Add the newly found schema to the set of visited schemas
            eventSchemas.add(eventSchema);

            return new SelectionCEA(eventSchema);
        }
    }


    @Override
    public CEA visitFilter_cel_pattern(CELParser.Filter_cel_patternContext ctx) {
        // First visit cel_pattern and extract variables. Then visit old_filter and
        // extract variables. Ensure that the variables defined within the old_filter are applied
        // over a subset of the variables defined in the pattern.
        // Push down the old_filter over the cel_pattern node

        CEA cea = ctx.cel_pattern().accept(this);
        Filter patternFilter = ctx.filter().accept(new FilterVisitor());
        return patternFilter.applyToCEA(cea);
    }

}
