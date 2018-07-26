package cel.compiler;

import cel.cea.CEA;
import cel.compiler.errors.NameError;
import cel.compiler.errors.UnknownStatementError;
import cel.compiler.errors.ValueError;
import cel.compiler.visitors.PatternVisitor;
import cel.compiler.visitors.TimeSpanVisitor;
import cel.event.EventSchema;
import cel.event.Label;
import cel.event.errors.NoSuchLabelException;
import cel.parser.CELParser;
import cel.parser.utils.StringCleaner;
import cel.query.*;
import cel.stream.StreamSchema;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.*;
import java.util.stream.Collectors;

public class QueryCompiler extends BaseCompiler<Query> {

    @Override
    public Query compile(String queryString) throws ParseCancellationException {

        CELParser CELParser = parse(queryString);

        // a query can only be valid if it parses on this parser rule
        CELParser.Cel_queryContext tree = CELParser.cel_query();

        return compileContext(tree);
    }

    @Override
    Query compileContext(ParserRuleContext ctx) {
        if (!(ctx instanceof CELParser.Cel_queryContext)) {
            throw new Error("FATAL ERROR");
        }

        CELParser.Cel_queryContext queryContext = (CELParser.Cel_queryContext) ctx;

        SelectionStrategy selectionStrategy = parseSelectionStrategy(queryContext.selection_strategy());

        // projected values
        ProjectionList projectionList = parseProjectionList(queryContext.result_values());

        // streams to use
        Map<String, StreamSchema> definedStreams = parseStreamList(queryContext.stream_list());
        Collection<String> definedEvents = getEventsForStreams(definedStreams.keySet());

        // pattern
        CEA patternCEA = queryContext.cel_pattern().accept(new PatternVisitor(definedStreams.keySet(), definedEvents));

        // partitions
        Collection<Partition> partitions = parsePartitionList(queryContext.partition_list(), patternCEA.getEventSchemas());

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

    private SelectionStrategy parseSelectionStrategy(CELParser.Selection_strategyContext ctx) {
        if (ctx == null) {
            return SelectionStrategy.getDefault();
        }
        if (ctx instanceof CELParser.Ss_allContext) {
            return SelectionStrategy.ALL;
        } else if (ctx instanceof CELParser.Ss_lastContext) {
            return SelectionStrategy.LAST;
        } else if (ctx instanceof CELParser.Ss_maxContext) {
            return SelectionStrategy.MAX;
        } else if (ctx instanceof CELParser.Ss_nextContext) {
            return SelectionStrategy.NEXT;
        } else {
            throw new UnknownStatementError("This kind of selection strategy has not been implemented", ctx);
        }
    }

    private ProjectionList parseProjectionList(CELParser.Result_valuesContext ctx) {
        if (ctx.STAR() != null) {
            return ProjectionList.ALL_EVENTS;
        } else {
            Collection<Label> projectedLabels = ctx.event_name()
                    .stream()
                    .map(event_nameContext -> {
                        try {
                            return Label.get(event_nameContext.getText());
                        } catch (NoSuchLabelException exc) {
                            throw new NameError("No label or event `" + event_nameContext.getText()
                                    + "` is ever defined on capturing query pattern", event_nameContext);
                        }
                    })
                    .collect(Collectors.toList());

            return new ProjectionList(projectedLabels);
        }
    }

    private Collection<Partition> parsePartitionList(CELParser.Partition_listContext ctx, Set<EventSchema> eventSchemas) {
        if (ctx == null) {
            return new ArrayList<>();
        }

        ArrayList<Partition> partitionList = new ArrayList<>();
        Set<String> definedAttributes = eventSchemas.stream()
                .map(EventSchema::getAttributes)
                .map(Map::keySet)
                .flatMap(Set::stream)
                .collect(Collectors.toSet());

        for (CELParser.Attribute_listContext partitionCtx : ctx.attribute_list()) {
            HashSet<String> attributeNames = new HashSet<>();
            for (CELParser.Attribute_nameContext attrNameCtx : partitionCtx.attribute_name()) {
                String attrName = StringCleaner.tryRemoveQuotes(attrNameCtx.getText());

                if (!definedAttributes.contains(attrName)) {
                    throw new ValueError("Attribute `" + attrName +
                            "` is not defined on any event named on query", attrNameCtx);
                }

                if (attributeNames.contains(attrName)) {
                    throw new ValueError("Attribute `" + attrName +
                            "` is defined more than once on partition list", attrNameCtx);
                }
                attributeNames.add(attrName);
            }

            for (EventSchema eventSchema : eventSchemas) {
                Set<String> schemaAttributes = new HashSet<>(eventSchema.getAttributes().keySet());
                schemaAttributes.retainAll(attributeNames);
                if (schemaAttributes.size() == 0) {
                    throw new ValueError("Partition list does not contain any attribute for events of type "
                            + eventSchema.getName(), partitionCtx);
                }
            }
            partitionList.add(new Partition(attributeNames));
        }
        return partitionList;
    }

    private Map<String, StreamSchema> parseStreamList(CELParser.Stream_listContext ctx) {
        if (ctx == null) {
            return StreamSchema.getAllSchemas();
        }

        Map<String, StreamSchema> streamSchemaMap = new HashMap<>();

        for (CELParser.Stream_nameContext nameContext : ctx.stream_name()) {
            String streamName = nameContext.getText();
            StreamSchema streamSchema = StreamSchema.tryGetSchemaFor(streamName);
            if (streamSchema == null) {
                throw new NameError("Unknown stream of name `" + streamName + "`", nameContext);
            }
            if (streamSchemaMap.containsKey(streamName)) {
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

    private TimeWindow parseTimeWindow(CELParser.Time_windowContext ctx) {
        if (ctx == null)
            return TimeWindow.NONE;
        if (ctx.event_span() != null) {
            long nEvents = Long.parseLong(ctx.event_span().number().getText());
            return new TimeWindow(nEvents, TimeWindow.Kind.EVENTS);
        } else if (ctx.time_span() != null) {
            long nSeconds = ctx.time_span().accept(new TimeSpanVisitor());
            return new TimeWindow(nSeconds, TimeWindow.Kind.TIME);
        } else {
            throw new UnknownStatementError("Can't parse this kind of time window", ctx);
        }

    }

    private ConsumptionPolicy parseConsumptionPolicy(CELParser.Consumption_policyContext ctx) {
        if (ctx == null) {
            return ConsumptionPolicy.getDefault();
        }
        if (ctx instanceof CELParser.Cp_anyContext) {
            return ConsumptionPolicy.ANY;
        } else if (ctx instanceof CELParser.Cp_noneContext) {
            return ConsumptionPolicy.NONE;
        } else if (ctx instanceof CELParser.Cp_partitionContext) {
            return ConsumptionPolicy.PARTITION;
        } else {
            throw new UnknownStatementError("This type of consumption policy has not been implemented", ctx);
        }
    }

}
