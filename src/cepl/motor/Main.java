package cepl.motor;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Main {

    static long outputs = 0;
    static long totalMatches = 0;
    static long totalMem = 0;
    static long initialMem = 0;

    static long enumTime = 0;


    public static void main(String[] args) {
        initialMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        
        StreamMatcher matcher = new StreamMatcher();
        matcher.setSemantic(Semantic.ANY);
        matcher.setDiscardPartials(true);
        matcher.setMatchCallback(Main::matchTriggered);

        int i = 0;
        for (String filename: args){
            try {
                FileReader file = new FileReader(filename);
                BufferedReader stream = new BufferedReader(file);
                
                String line;
                int id;

                while ((line = stream.readLine()) != null){
                    id = Integer.parseInt(line.substring(5, line.length() - 1));
                    Event e = new Event(line.charAt(0), id);                 
                    matcher.newValue(e);
                }
            }
            catch (FileNotFoundException ex){
                System.out.println("Unable to open file '" + args[0] + "'");
            }
            catch (IOException ex) {
                System.out.println("Unable to read file '" + args[0] + "'");
            }
        }
        System.out.println("Total outputs: " + outputs);
        System.out.println("Total execution time: " + matcher.getExecutionTime());
        System.out.println("Total enumeration time: " + ((double)enumTime / 1000000000));
        System.out.println("Average memory usage before match: " + (totalMem / totalMatches) +  " Bytes.");
    }

    static void matchTriggered(MatchGrouping mg){
        totalMem += Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() - initialMem;
        totalMatches++;
        outputs += mg.size();
        // System.out.println("[" + mg.lastEvent() + "] triggered a total of " + mg.size() + " outputs.");

        long startTime = System.nanoTime();

        for (Match m : mg){
            // System.out.print("\t");        
            for (Event e: m){
                // System.out.print( e + " ");
            }
            // System.out.println("");
        }

        enumTime += System.nanoTime() - startTime;
    }
}
