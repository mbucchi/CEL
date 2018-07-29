import cel.compiler.CompoundStatementCompiler;
import cel.compiler.DeclarationCompiler;
import cel.compiler.QueryCompiler;
import cel.event.EventSchema;
import cel.query.Query;
import cel.runtime.BitVectorGenerator;
import cel.runtime.event.Event;
import cel.runtime.source.BitVectorSourceGenerator;
import cel.runtime.source.EventSourceGenerator;
import cel.values.ValueType;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.mdkt.compiler.InMemoryJavaCompiler;

public class BitVectorTest {

    private static AtomicInteger idx = new AtomicInteger(0);
    private static int METADATA_ATTRIBUTES = 4;

    public static void main(String[] args) {
        DeclarationCompiler declarationCompiler;
        QueryCompiler queryCompiler;
        CompoundStatementCompiler compoundStatementCompiler;

        try{
            byte[] encoded = Files.readAllBytes(Paths.get(args[0]));
            String statements = new String(encoded, Charset.defaultCharset());
            declarationCompiler = new DeclarationCompiler();
            queryCompiler = new QueryCompiler();
            compoundStatementCompiler = new CompoundStatementCompiler(declarationCompiler, queryCompiler);
            Collection<Query> queries = compoundStatementCompiler.compile(statements);
            Query q = queries.iterator().next();
            testBitVector(q, args[1]);

        } catch (Exception err) {
            System.out.println(err);
            err.printStackTrace();
            System.exit(1);
        }
    }

    private static void testBitVector(Query q, String streamPath) throws Exception {
        InMemoryJavaCompiler javac = InMemoryJavaCompiler.newInstance();
        BitVectorSourceGenerator test = new BitVectorSourceGenerator(q.getPatternCEA());
        String src = test.makeSourceCode();
        System.out.println(src);
        String s;
        for (EventSchema ev : q.getPatternCEA().getEventSchemas()) {
            s = EventSourceGenerator.createEventSource(ev);
            System.out.println(s);
            javac.addSource("cel.runtime.event." + ev.getName(), s);
        }
        javac.addSource("cel.runtime.BVG", src);
        Map<String, Class<?>> binaries = javac.compileAll();
        for (EventSchema ev : q.getPatternCEA().getEventSchemas()) {
            Event.addClass(ev.getName(), binaries.get("cel.runtime.event." + ev.getName()));
        }

        BitVectorGenerator bvg = (BitVectorGenerator) binaries.get("cel.runtime.BVG").getConstructor().newInstance();
        try {
            FileReader file = new FileReader(streamPath);
            BufferedReader stream = new BufferedReader(file);

            String line;
            Event e;

            while ((line = stream.readLine()) != null) {
                e = parseEvent(line, q.getPatternCEA().getEventSchemas());
                System.out.println(e.toString());
                System.out.println(bvg.getBitVector(e));
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Unable to open file '" + streamPath + "'");
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("Unable to read file '" + streamPath + "'");
            System.exit(1);

        }
    }

    private static Event parseEvent(String line, Set<EventSchema> events) {
        String values[] = line.split("[()]");
        String name = values[0];
        values = values[1].split(",");

        Event e;
        Object attrs[] = null;

        for (EventSchema ev : events) {
            if (line.startsWith(ev.getName())) {
                attrs = new Object[ev.getAttributes().size()];
                addMetaData(attrs);
                for (int i = METADATA_ATTRIBUTES; i < ev.getAttributes().size(); i++) {
                    String n = values[i - METADATA_ATTRIBUTES].split("=")[0];
                    String val = values[i - METADATA_ATTRIBUTES].split("=")[1];
                    if (ev.getAttributes().get(n) == ValueType.INTEGER) {
                        attrs[i] = Integer.parseInt(val);
                    } else if (ev.getAttributes().get(n) == ValueType.DOUBLE) {
                        attrs[i] = Double.parseDouble(val);
                    } else if (ev.getAttributes().get(n) == ValueType.LONG) {
                        attrs[i] = Long.parseLong(val);
                    } else {
                        attrs[i] = val;
                    }
                }
                break;
            }
        }
        try {
            e = Event.newEvent(name, attrs);
            return e;

        } catch (Exception err) {
            err.printStackTrace();
        }
        return null;
    }

    private static void addMetaData(Object... attrs) {
        /* stream */
        attrs[0] = 1;
        /* index */
        attrs[1] = (long) idx.getAndIncrement();
        /* timestamp */
        attrs[2] = System.currentTimeMillis();
        /* type */
        attrs[3] = 4;
    }
}
