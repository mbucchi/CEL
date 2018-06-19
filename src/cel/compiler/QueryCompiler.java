package cel.compiler;

import cel.cea.CEA;
import cel.compiler.errors.ValueError;
import cel.event.Label;
import cel.event.errors.NoSuchLabelException;
import cel.compiler.errors.NameError;
import cel.compiler.errors.UnknownStatementError;
import cel.compiler.visitors.PatternVisitor;
import cel.compiler.visitors.TimeSpanVisitor;
import cel.event.EventSchema;
import cel.parser.CEPLParser;
import cel.query.*;
import cel.stream.StreamSchema;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class QueryCompiler extends BaseCompiler<Query> {

    @Override
    public Query compile(String queryString) throws ParseCancellationException {

        CEPLParser ceplParser = parse(queryString);

        // a query can only be valid if it parses on this parser rule
        CEPLParser.Cel_queryContext tree = ceplParser.cel_query();

        return compileContext(tree);
    }

    @Override
    Query compileContext(ParserRuleContext ctx){
        if (!(ctx instanceof CEPLParser.Cel_queryContext)){
            throw new Error("FATAL ERROR");
        }

        CEPLParser.Cel_queryContext queryContext = (CEPLParser.Cel_queryContext) ctx;

        SelectionStrategy selectionStrategy = parseSelectionStrategy(queryContext.selection_strategy());

        // projected values
        ProjectionList projectionList = parseProjectionList(queryContext.result_values());

        // streams to use
        Map<String, StreamSchema> definedStreams = parseStreamList(queryContext.stream_list());
        Collection<String> definedEvents = getEventsForStreams(definedStreams.keySet());

        // pattern
        CEA patternCEA = queryContext.cel_pattern().accept(new PatternVisitor(definedStreams.keySet(), definedEvents));

        // partitions
        Collection<Partition> partitions = parsePartitionList(queryContext.partition_list());

        // time window
        TimeWindow timeWindow = parseTimeWindow(queryContext.time_window());

        // consumption policy
        ConsumptionPolicy consumptionPolicy = parseConsumptionPolicy(queryContext.consumption_policy());


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
            throw new UnknownStatementError("This kind of selection strategy has not been implemented", ctx);
        }
    }

    private ProjectionList parseProjectionList(CEPLParser.Result_valuesContext ctx) {
        if (ctx.STAR() != null) {
            return ProjectionList.ALL_EVENTS;
        }
        else {
             Collection<Label> projectedLabels = ctx.event_name()
                     .stream()
                     .map(event_nameContext -> {
                         try {
                             return Label.get(event_nameContext.getText());
                         }
                         catch (NoSuchLabelException exc) {
                             throw new NameError("No label or event `" + event_nameContext.getText()
                                     + "` is ever defined on capturing query pattern", event_nameContext);
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

        Map<String, StreamSchema> streamSchemaMap = new HashMap<>();

        for (CEPLParser.Stream_nameContext nameContext : ctx.stream_name()) {
            String streamName = nameContext.getText();
            StreamSchema streamSchema = StreamSchema.tryGetSchemaFor(streamName);
            if (streamSchema == null){
                throw new NameError("Unknown stream of name `" + streamName + "`", nameContext);
            }
            if (streamSchemaMap.containsKey(streamName)){
                throw new ValueError("Stream `" + streamName + "` declared more than once on query", nameContext);
            }
            streamSchemaMap.put(streamName, streamSchema);
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
            throw new UnknownStatementError("Can't parse this kind of time window", ctx);
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
            throw new UnknownStatementError("This type of consumption policy has not been implemented", ctx);
        }
    }

}
