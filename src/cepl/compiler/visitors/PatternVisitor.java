package cepl.compiler.visitors;

import cepl.cea.CEA;
import cepl.cea.utils.Label;
import cepl.filter.PatternFilter;
import cepl.parser.CEPLBaseVisitor;
import cepl.parser.CEPLParser;
import cepl.event.EventSchema;
import cepl.stream.StreamSchema;

import java.util.Collection;

public class PatternVisitor extends CEPLBaseVisitor<CEA> {

    private Collection<String> definedStreams;
    private Collection<String> definedEvents;

    public PatternVisitor(PatternVisitor copyFrom){
        definedStreams = copyFrom.definedStreams;
        definedEvents = copyFrom.definedEvents;
    }

    public PatternVisitor(Collection<String> definedStreams, Collection<String> definedEvents){
        this.definedStreams = definedStreams;
        this.definedEvents = definedEvents;
    }


    @Override
    public CEA visitPar_cel_pattern(CEPLParser.Par_cel_patternContext ctx) {
        // The context remains the same, we just ignore the parenthesis.
        // Get the child and visit without the need to instantiate a new visitor.
        return ctx.cel_pattern().accept(this);
    }


    @Override
    public CEA visitBinary_cel_pattern(CEPLParser.Binary_cel_patternContext ctx) {
        // the context remains the same, no need for creating a new visitor
        PatternVisitor newVisitor = this;

        // get the left and right patterns

        CEA left = ctx.cel_pattern(0).accept(newVisitor);
        CEA right = ctx.cel_pattern(1).accept(newVisitor);

        // binary could be either `OR` or `;`.

        if (ctx.K_OR() != null){
            return CEA.createOrCEA(left, right);
        }
        else {
            return CEA.createSeqCEA(left, right);
        }
    }

    @Override
    public CEA visitKleene_cel_pattern(CEPLParser.Kleene_cel_patternContext ctx) {
        // the context remains the same, no need for creating a new visitor
        PatternVisitor newVisitor = this;

        CEA inner = ctx.cel_pattern().accept(newVisitor);

        return CEA.createKleeneCEA(inner);
    }

    @Override
    public CEA visitAssign_cel_pattern(CEPLParser.Assign_cel_patternContext ctx) {
        // the context remains the same, no need for creating a new visitor
        PatternVisitor newVisitor = this;

        String newLabel = ctx.event_name().getText();
        Label label = Label.forName(newLabel);
        CEA inner = ctx.cel_pattern().accept(newVisitor);

        return inner.relabelCEA(label);
    }

    @Override
    public CEA visitEvent_cel_pattern(CEPLParser.Event_cel_patternContext ctx) {

        // Event can either be named by itself or within the scope of a stream.
        // First check which is the case and deal with each of them separately
        String eventName = ctx.s_event_name().event_name().getText();

        // Check if a stream is defined
        if (ctx.s_event_name().stream_name() != null){
            String streamName = ctx.s_event_name().stream_name().getText();

            if (!definedStreams.contains(streamName)){
                // TODO: once decided how to deal with errors, throw a
                // TODO: `NameError: Stream <name> is not defined`
            }
            StreamSchema streamSchema = StreamSchema.getSchemaFor(streamName);

            if (!streamSchema.containsEvent(eventName)){
                // TODO: once decided how to deal with errors, throw a
                // TODO: `NameError: Event <name> is not defined within Stream <name>`
            }

            // Create a selection CEA that filters for the given stream
            return CEA.createSelectionCEA(streamSchema, EventSchema.getSchemaFor(eventName));
        }
        else {
            // no stream is defined, just check that the event is declared within the scope of the
            // query
            if (!definedEvents.contains(eventName)) {
                // TODO: once decided how to deal with errors, throw a
                // TODO: `NameError: Event <name> is not defined within any of the query streams`
            }
            // Create a selection CEA with no filters
            return CEA.createSelectionCEA(EventSchema.getSchemaFor(eventName));
        }
    }


    @Override
    public CEA visitFilter_cel_pattern(CEPLParser.Filter_cel_patternContext ctx) {
        // First visit cel_pattern and extract variables. Then visit old_filter and
        // extract variables. Ensure that the variables defined within the old_filter are applied
        // over a subset of the variables defined in the pattern.
        // Push down the old_filter over the cel_pattern node

        CEA cea = ctx.cel_pattern().accept(this);
        PatternFilter patternFilter = ctx.filter().accept(new FilterVisitor());
        // TODO: check that filter variables are valid
        return patternFilter.applyToCEA(cea);
    }

}
