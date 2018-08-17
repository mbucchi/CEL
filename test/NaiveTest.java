import cel.event.EventSchema;
import cel.query.ConsumptionPolicy;
import cel.query.Query;
import cel.query.SelectionStrategy;
import cel.runtime.*;
import cel.runtime.cea.ExecutableCEA;
import cel.runtime.event.Event;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Map;


public class NaiveTest {

    static long outputs = 0;
    static long totalMatches = 0;
    static long totalMem = 0;
    static long initialMem = 0;
    static long compileTime = 0;
    static long enumTime = 0;
    static long totalTime = 0;
    static long eventParsingTime = 0;

    public static void main(String[] args) throws Exception {
        totalTime = System.nanoTime();
        compileTime = System.nanoTime();
        initialMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();

        InMemoryJavaCompiler javac = InMemoryJavaCompiler.newInstance();
        Query q = QueryGetter.getQuery(args[0]);
        String src = BitVectorGeneratorCodeGetter.getBitVectorGeneratorCode(q);
//        System.out.println("Bit Vector Order: " + test.getBitVectorOrder().toString());
//        System.out.println(src);
        for (Object o : EventsCodeGetter.getEventsCode(q).entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            javac.addSource("cel.runtime.event." + pair.getKey(), (String) pair.getValue());
//            System.out.println(pair.getValue());
        }
        javac.addSource("cel.runtime.BVG", src);

        String ceaSrc = CEACodeGetter.getCEACode(q);
        javac.addSource("cel.runtime.cea.CEA", ceaSrc);

        Map<String, Class<?>> binaries = javac.compileAll();
        for (String s : EventsCodeGetter.getEventsCode(q).keySet()) {
            Event.addClass(s, binaries.get("cel.runtime.event." + s));
        }
        BitVectorGenerator bvg = (BitVectorGenerator) binaries.get("cel.runtime.BVG").getConstructor().newInstance();
        ExecutableCEA cea = (ExecutableCEA) binaries.get("cel.runtime.cea.CEA").getConstructor().newInstance();

        compileTime = System.nanoTime() - compileTime;

        OldEngine engine = new OldEngine(cea);
        SelectionStrategy sel = q.getSelectionStrategy();

        if (sel.equals(SelectionStrategy.ALL)) {
            engine.setANY();
        } else if (sel.equals(SelectionStrategy.MAX)) {
            engine.setMAX();
        } else if (sel.equals(SelectionStrategy.LAST)) {
            engine.setLAST();
        } else if (sel.equals(SelectionStrategy.NEXT)) {
            engine.setNXT();
        } else if (sel.equals(SelectionStrategy.STRICT)) {
            engine.setSTRICT();
        }

        if (q.getConsumptionPolicy().equals(ConsumptionPolicy.NONE)) {
            engine.setDiscardPartials(false);
        } else {
            engine.setDiscardPartials(true);
        }

        Map<String, EventSchema> events = new HashMap<>();
        for (EventSchema ev : q.getPatternCEA().getEventSchemas()) {
            events.put(ev.getName(), ev);
        }

        engine.setMatchCallback(NaiveTest::matchTriggered);

        System.out.println(q.getPatternCEA().toString());

        try {
            FileReader file = new FileReader(args[1]);
            BufferedReader stream = new BufferedReader(file);

            String line;
            Event e;

            while ((line = stream.readLine()) != null) {
                long start = System.nanoTime();
                e = EventParser.parseEvent(line, events);
                assert e != null;
                eventParsingTime += System.nanoTime() - start;
//                System.out.println(e.toString());
                BitSet vector = (bvg.getBitVector(e));
                engine.newValue(e, vector);
            }
        } catch (FileNotFoundException ex) {
            System.err.println("Unable to open file '" + args[1] + "'");
            System.exit(1);
        } catch (IOException ex) {
            System.err.println("Unable to read file '" + args[1] + "'");
            System.exit(1);

        }

        totalMatches = totalMatches > 0 ? totalMatches : 1;
        System.out.println("Total time: " + ((double) (System.nanoTime() - totalTime) / 1000000000));
        System.out.println("Query compilation time: " + ((double) compileTime / 1000000000));
        System.out.println("Event parsing time: " + ((double) eventParsingTime / 1000000000));
        System.out.println("Parsing time: " + ((double) EventParser.parseTime / 1000000000));
        System.out.println("Other parsing time: " + ((double) EventParser.otherTime / 1000000000));
        System.out.println("Total outputs: " + outputs);
        System.out.println("------ Total execution time: " + engine.getExecutionTime() + " ------");
        System.out.println("Total enumeration time: " + ((double) enumTime / 1000000000));
        System.out.println("Average memory usage before match: " + (totalMem / totalMatches) + " Bytes.");
    }

    private static void matchTriggeredPrint(MatchGrouping mg) {
        totalMem += Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - initialMem;
        totalMatches++;
        outputs += mg.size();
        System.out.println("\n[" + mg.lastEvent() + "] triggered a total of " + mg.size() + " outputs.");

        long startTime = System.nanoTime();

        for (Match m : mg) {
            System.out.print("\t");
            for (Event e : m) {
                System.out.print(e + " ");
            }
            System.out.println("");
        }
        System.out.println("");

        enumTime += System.nanoTime() - startTime;
    }

    private static void matchTriggered(MatchGrouping mg) {
        totalMem += Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - initialMem;
        totalMatches++;
        outputs += mg.size();

        long startTime = System.nanoTime();

//        for (Match m : mg)
//            for (Event e : m)
//                ;

        enumTime += System.nanoTime() - startTime;
    }

}