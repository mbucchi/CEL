package cepl.compiler;

import cepl.cea.CEA;
import cepl.event.Label;
import cepl.event.errors.NoSuchLabelException;
import cepl.compiler.errors.NameError;
import cepl.compiler.errors.UnknownStatementError;
import cepl.compiler.visitors.PatternVisitor;
import cepl.compiler.visitors.TimeSpanVisitor;
import cepl.event.EventSchema;
import cepl.parser.CEPLParser;
import cepl.query.*;
import cepl.stream.StreamSchema;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryCompiler extends BaseCompiler {

    public Query compile(String queryString) throws ParseCancellationException {

        CEPLParser ceplParser = parse(queryString);

        // a query can only be valid if it parses on this parser rule
        CEPLParser.Cel_queryContext tree = ceplParser.cel_query();

        // TODO: implement. The following lines are just a test for the pattern section of a query.


        // selection strategy
        SelectionStrategy selectionStrategy = parseSelectionStrategy(tree.selection_strategy());

        // projected values
        ProjectionList projectionList = parseProjectionList(tree.result_values());

        // streams to use
        Map<String, StreamSchema> definedStreams = parseStreamList(tree.stream_list());
        Collection<String> definedEvents = getEventsForStreams(definedStreams.keySet());

        // pattern
        CEA patternCEA = tree.cel_pattern().accept(new PatternVisitor(definedStreams.keySet(), definedEvents));


        Collection<Partition> partitions = parsePartitionList(tree.partition_list());


        TimeWindow timeWindow = parseTimeWindow(tree.time_window());


        ConsumptionPolicy consumptionPolicy = parseConsumptionPolicy(tree.consumption_policy());


        return new Query(
                selectionStrategy,
                projectionList,
                definedStreams.values(),
                patternCEA,
                partitions,
                timeWindow,
                consumptionPolicy);
    }

    private SelectionStrategy parseSelectionStrategy(CEPLParser.Selection_strategyContext ctx) {
        if (ctx == null ) {
            return SelectionStrategy.getDefault();
        }
        if (ctx instanceof CEPLParser.Ss_allContext) {
            return SelectionStrategy.ALL;
        }
        else if (ctx instanceof CEPLParser.Ss_lastContext) {
            return SelectionStrategy.LAST;
        }
        else if (ctx instanceof CEPLParser.Ss_maxContext) {
            return SelectionStrategy.MAX;
        }
        else if (ctx instanceof CEPLParser.Ss_nextContext) {
            return SelectionStrategy.NEXT;
        }
        else {
            throw new UnknownStatementError("Selection strategy`" + ctx.getText() + "` has not been implemented");
        }
    }

    private ProjectionList parseProjectionList(CEPLParser.Result_valuesContext ctx) {
        if (ctx.STAR() != null) {
            return new ProjectionList();
        }
        else {
             Collection<Label> projectedLabels = ctx.event_name()
                     .stream()
                     .map(event_nameContext -> {
                         try {
                             return Label.get(event_nameContext.getText());
                         }
                         catch (NoSuchLabelException exc) {
                             throw new NameError(exc.getMessage());
                         }
                        })
                        .collect(Collectors.toList());

             return new ProjectionList(projectedLabels);
        }
    }

    private Collection<Partition> parsePartitionList(CEPLParser.Partition_listContext ctx) {
        if (ctx == null){
            return new ArrayList<>();
        }
        return ctx.attribute_list()
                .stream()
                .map(attribute_listContext -> attribute_listContext.attribute_name().stream()
                        .map(CEPLParser.Attribute_nameContext::getText)
                        .collect(Collectors.toList()))
                .map(Partition::new)
                .collect(Collectors.toList());
    }

    private Map<String, StreamSchema> parseStreamList(CEPLParser.Stream_listContext ctx) {
        if (ctx == null){
            return StreamSchema.getAllSchemas();
        }

        Map<String, StreamSchema> streamSchemaMap = ctx.stream_name()
                .stream()
                .map(CEPLParser.Stream_nameContext::getText)
                .collect(Collectors.toMap(s -> s, StreamSchema::tryGetSchemaFor));

        for (String streamName : streamSchemaMap.keySet()) {
            if (streamSchemaMap.get(streamName) == null){
                throw new NameError("Unknown stream of name `" + streamName + "`");
            }
        }
        return streamSchemaMap;
    }

    private Collection<String> getEventsForStreams(Collection<String> streamNames) {
        return streamNames.stream()
                .map(streamName -> StreamSchema.getSchemaFor(streamName).getEvents())
                .flatMap(Collection::stream)
                .map(EventSchema::getName)
                .collect(Collectors.toSet());
    }

    private TimeWindow parseTimeWindow(CEPLParser.Time_windowContext ctx) {
        if (ctx == null)
            return TimeWindow.NONE;
        if (ctx.event_span() != null) {
            long nEvents = Long.parseLong(ctx.event_span().number().getText());
            return new TimeWindow(nEvents, TimeWindow.Kind.EVENTS);
        }
        else if (ctx.time_span() != null) {
            long nSeconds = ctx.time_span().accept(new TimeSpanVisitor());
            return new TimeWindow(nSeconds, TimeWindow.Kind.TIME);
        }
        else {
            throw new UnknownStatementError("Can't parse this kind of time window");
        }

    }

    private ConsumptionPolicy parseConsumptionPolicy(CEPLParser.Consumption_policyContext ctx) {
        if (ctx == null ) {
            return ConsumptionPolicy.getDefault();
        }
        if (ctx instanceof CEPLParser.Cp_anyContext) {
            return ConsumptionPolicy.ANY;
        }
        else if (ctx instanceof CEPLParser.Cp_noneContext) {
            return ConsumptionPolicy.NONE;
        }
        else if (ctx instanceof CEPLParser.Cp_partitionContext) {
            return ConsumptionPolicy.PARTITION;
        }
        else {
            throw new UnknownStatementError("Consumption policy`" + ctx.getText() + "` has not been implemented");
        }
    }

}
