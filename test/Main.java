import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import cepl.parser.*;
import cepl.motor.*;

public class Main {

    static long outputs = 0;
    static long totalMatches = 0;
    static long totalMem = 0;
    static long initialMem = 0;
    static long compileTime = 0;
    static long enumTime = 0;

    public static void main(String[] args) throws Exception{
        initialMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        CELCompiler compiler = new CELCompiler();
        compiler.addEventDefinition("A", "id", Integer.class);
        compiler.addEventDefinition("B", "id", Integer.class);
        compiler.addEventDefinition("C", "id", Integer.class);
        compiler.addEventDefinition("D", "id", Integer.class);
        compiler.addEventDefinition("E", "id", Integer.class);


        compileTime = System.nanoTime();
        try {
            compiler.compileQuery(args[0]);
        }
        catch (ParserException | WellformedException ex){
            System.err.println(ex.getMessage());
            System.exit(1);
        }
        compileTime = System.nanoTime() - compileTime;
        CELEngine engine = compiler.getEngine();
        
        engine.setDiscardPartials(true);
        engine.setMatchCallback(Main::matchTriggered);

        try {
            FileReader file = new FileReader(args[1]);
            BufferedReader stream = new BufferedReader(file);
            
            String line;
            Event e;

            while ((line = stream.readLine()) != null){
                String values[] = line.split("[\\(\\)]");
                String name = values[0];
                values = values[1].split("=");
                e = Event.newEvent(name, Integer.parseInt(values[1]));
                engine.newValue(e);
            }
        }
        catch (FileNotFoundException ex){
            System.err.println("Unable to open file '" + args[1] + "'");
            System.exit(1);
        }
        catch (IOException ex) {
            System.err.println("Unable to read file '" + args[1] + "'");
            System.exit(1);
        }
        totalMatches = totalMatches > 0 ? totalMatches : 1;
        System.out.println("Query compilation time: " + ((double)compileTime / 1000000000));
        System.out.println("Total outputs: " + outputs);
        System.out.println("Total execution time: " + engine.getExecutionTime());
        System.out.println("Total enumeration time: " + ((double)enumTime / 1000000000));
        System.out.println("Average memory usage before match: " + (totalMem / totalMatches) +  " Bytes.");
    }
    

    static void matchTriggeredPrint(MatchGrouping mg){
        totalMem += Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - initialMem;
        totalMatches++;
        outputs += mg.size();
        System.out.println("\n[" + mg.lastEvent() + "] triggered a total of " + mg.size() + " outputs.");

        long startTime = System.nanoTime();

        for (Match m : mg){
            System.out.print("\t");        
            for (Event e: m){
                System.out.print( e + " ");
            }
            System.out.println("");
        }
        System.out.println("");

        enumTime += System.nanoTime() - startTime;
    }

    static void matchTriggered(MatchGrouping mg){
        totalMem += Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - initialMem;
        totalMatches++;
        outputs += mg.size();

        long startTime = System.nanoTime();

        for (Match m : mg) for (Event e: m);

        enumTime += System.nanoTime() - startTime;
    }
}
