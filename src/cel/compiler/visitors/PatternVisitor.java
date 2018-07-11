package cel.compiler.visitors;

import cel.cea.*;
import cel.compiler.errors.UnknownStatementError;
import cel.event.Label;
import cel.compiler.errors.NameError;
import cel.filter.PatternFilter;
import cel.parser.CELBaseVisitor;
import cel.parser.CELParser;
import cel.event.EventSchema;
import cel.parser.utils.StringCleaner;
import cel.stream.StreamSchema;

import java.util.*;

public class PatternVisitor extends CELBaseVisitor<CEA> {

    private Collection<String> definedStreams;
    private Collection<String> definedEvents;

    public PatternVisitor(Collection<String> definedStreams, Collection<String> definedEvents){
        this.definedStreams = definedStreams;
        this.definedEvents = definedEvents;
    }


    @Override
    public CEA visitPar_cel_pattern(CELParser.Par_cel_patternContext ctx) {
        // The context remains the same, we just ignore the parenthesis.
        // Get the child and visit without the need to instantiate a new visitor.
        return ctx.cel_pattern().accept(this);
    }


    @Override
    public CEA visitBinary_cel_pattern(CELParser.Binary_cel_patternContext ctx) {
        // the context remains the same, no need for creating a new visitor
        PatternVisitor newVisitor = this;

        // get the left and right patterns

        CEA left = ctx.cel_pattern(0).accept(newVisitor);
        CEA right = ctx.cel_pattern(1).accept(newVisitor);

        // binary could be either `OR` or `;`.

        if (ctx.K_OR() != null){
            return new OrCEA(left, right);
        }
        else if (ctx.SEMICOLON() != null){
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
        Label label = Label.forName(newLabel, inner.getEventSchemas());

        return new AssignCEA(inner, label);
    }

    @Override
    public CEA visitEvent_cel_pattern(CELParser.Event_cel_patternContext ctx) {

        // Event can either be named by itself or within the scope of a stream.
        // First check which is the case and deal with each of them separately
        String eventName = StringCleaner.tryRemoveQuotes(ctx.s_event_name().event_name().getText());

        // Check if a stream is defined
        if (ctx.s_event_name().stream_name() != null){
            String streamName = StringCleaner.tryRemoveQuotes(ctx.s_event_name().stream_name().getText());

            if (!definedStreams.contains(streamName)){
                throw new NameError("Stream `" + streamName + "` is not defined", ctx.s_event_name().stream_name());
            }
            Set<StreamSchema> streamSchema = new HashSet<>();
            StreamSchema inner = StreamSchema.getSchemaFor(streamName);
            streamSchema.add(inner);

            if (!inner.containsEvent(eventName)){
                throw new NameError("Event `" + eventName + "` is not defined within stream `" + streamName + "`",
                        ctx.s_event_name().event_name());
            }

            // Create a selection CEA that filters for the given stream
            Set<EventSchema> eventSchema = new HashSet<>();
            eventSchema.add(EventSchema.tryGetSchemaFor(eventName));

            if (eventSchema.size() == 0){
                throw new NameError("Event `" + eventName + "` is not defined", ctx.s_event_name().event_name());
            }
            return new SelectionCEA(streamSchema, eventSchema);
        }
        else {
            // no stream is defined, just check that the event is declared within the scope of the
            // query
            if (!definedEvents.contains(eventName)) {
                throw new NameError("Event `" + eventName + "` is not defined within any of the query streams",
                       ctx.s_event_name());
            }

            // Create a selection CEA with no filters
            Set<EventSchema> eventSchema = new HashSet<>();
            eventSchema.add(EventSchema.tryGetSchemaFor(eventName));

            if (eventSchema.size() == 0){
                throw new NameError("Event `" + eventName + "` is not defined", ctx.s_event_name());
            }
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
        PatternFilter patternFilter = ctx.filter().accept(new FilterVisitor());
        return patternFilter.applyToCEA(cea);
    }

}
