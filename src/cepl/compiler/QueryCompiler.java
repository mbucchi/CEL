package cepl.compiler;

import cepl.cea.CEA;
import cepl.compiler.visitors.PatternVisitor;
import cepl.event.EventSchema;
import cepl.parser.CEPLParser;
import cepl.stream.StreamSchema;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.Collection;
import java.util.Map;

public class QueryCompiler extends BaseCompiler {

    public void compile(String query,
                        Map<String, EventSchema> eventSchemaMap,
                        Map<String, StreamSchema> streamSchemaMap) throws ParseCancellationException {

        CEPLParser ceplParser = parse(query);

        // a query can only be valid if it parses on this parser rule
        CEPLParser.Cel_queryContext tree = ceplParser.cel_query();

        // TODO: implement. The following lines are just a test

        Collection<String> definedStreams, definedEvents;
        definedStreams = StreamSchema.getAllSchemas().keySet();
        definedEvents = EventSchema.getAllSchemas().keySet();

        CEA cea = tree.cel_pattern().accept(new PatternVisitor(definedStreams, definedEvents));
    }
}
