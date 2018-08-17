import cel.query.Query;
import cel.runtime.event.Event;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Map;

public class BitVectorTest {

    public static void main(String[] args) throws Exception {
        InMemoryJavaCompiler javac = InMemoryJavaCompiler.newInstance();
        Query q = QueryGetter.getQuery(args[0]);
        String src = BitVectorGeneratorCodeGetter.getBitVectorGeneratorCode(q);
//        System.out.println("Bit Vector Order: " + test.getBitVectorOrder().toString());
        System.out.println(src);
        for (Object o : EventsCodeGetter.getEventsCode(q).entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            javac.addSource("cel.runtime.event." + pair.getKey(), (String) pair.getValue());
//            System.out.println(pair.getValue());
        }
        javac.addSource("cel.runtime.BVG", src);
        Map<String, Class<?>> binaries = javac.compileAll();
        for (String s : EventsCodeGetter.getEventsCode(q).keySet()) {
            Event.addClass(s, binaries.get("cel.runtime.event." + s));
        }

        BitVectorGenerator bvg = (BitVectorGenerator) binaries.get("cel.runtime.BVG").getConstructor().newInstance();
        try {
            FileReader file = new FileReader(args[1]);
            BufferedReader stream = new BufferedReader(file);

            String line;
            Event e;

            while ((line = stream.readLine()) != null) {
                e = EventParser.parseEvent(line, q.getPatternCEA().getEventSchemas());
                assert e != null;
                System.out.println(e.toString());
                System.out.println(bvg.getBitVector(e));
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Unable to open file '" + args[1] + "'");
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("Unable to read file '" + args[1] + "'");
            System.exit(1);

        }
    }
}
