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
import java.util.Map;

public class NewTest {

    static long outputs = 0;
    static long totalMatches = 0;
    static long totalMem = 0;
    static long initialMem = 0;
    static long compileTime = 0;
    static long enumTime = 0;

    public static void main(String[] args) throws Exception {
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
        NewEngine engine = new NewEngine(cea);

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

        engine.setMatchCallback(NewTest::matchTriggeredPrint);

        System.out.println(q.getPatternCEA().toString());

        try {
            FileReader file = new FileReader(args[1]);
            BufferedReader stream = new BufferedReader(file);

            String line;
            Event e;

            while ((line = stream.readLine()) != null) {
                e = EventParser.parseEvent(line, q.getPatternCEA().getEventSchemas());
                assert e != null;
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
        System.out.println("Query compilation time: " + ((double) compileTime / 1000000000));
        System.out.println("Total outputs: " + outputs);
        System.out.println("Total execution time: " + engine.getExecutionTime());
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

        for (Match m : mg)
            for (Event e : m)
                ;

        enumTime += System.nanoTime() - startTime;
    }
}
