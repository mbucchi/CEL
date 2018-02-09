package cepl.parser;

import java.util.Map;
import java.util.Set;

class SourceCodeCreator {

    public static String createEvent(String name, String[] fieldNames, Class[] fieldTypes){
        StringBuilder event = new StringBuilder();
        StringBuilder fieldDescription = new StringBuilder();
        StringBuilder properties = new StringBuilder();
        StringBuilder setters = new StringBuilder();
        StringBuilder getters = new StringBuilder();
        StringBuilder toString = new StringBuilder();

        event.append("package cepl.motor;\n");
        event.append("import java.util.Map;\n");
        event.append("import static java.util.Map.entry;\n");
        event.append("public class " + name + " extends Event {\n");
        event.append("    private static Map<String, Class> fieldDescriptions = Map.ofEntries(");
        for (int i = 0; i < fieldNames.length; i++){
            String fName = fieldNames[i];
            fieldDescription.append("entry(\"" + fName + "\", " + fieldTypes[i] + ".class)");
            properties.append("    " + fieldTypes[i] + " " + fName + ";\n");
            setters.append("        " + fName + " = (" + fieldTypes[i] + ")args[" + i + "];\n");
            getters.append("        if (field.equals(\"" + fName + "\")) return " + fName + ";\n");
            toString.append(fName + "=\" + " + fName + " + \"");
            if (i < fieldNames.length - 1){
                fieldDescription.append(", ");
                toString.append(", ");
            }
        }
        event.append(fieldDescription.toString() + ");\n");
        event.append(properties.toString());
        event.append("    public " + name + "(){super();}\n");

        event.append("    protected void setValues(Object... args) throws IllegalAccessError {\n");
        event.append("        if (args.length > " + fieldNames.length + ") throw new IllegalAccessError(\"Tried to assign more fields than declared on event of type \\\"" + name + "\\\"\");\n");
        event.append(setters.toString());
        event.append("    }\n");

        event.append("    public Object getValue(String field) throws IllegalAccessError {\n");
        event.append(getters.toString());
        event.append("        throw new IllegalAccessError(\"No field of name \\\"\" + field + \"\\\" present on event of type \\\"" + name + "\\\"\");\n");        
        event.append("    }\n");     
        
        event.append("    public String toString(){\n");
        event.append("        return \"" + name + "(");
        event.append(toString.toString());
        event.append(")\";\n");
        event.append("    }\n");


        event.append("    public Map<String, Class> getFieldDescriptions(){\n");
        event.append("        return fieldDescriptions;\n");
        event.append("    }\n");

        event.append("}");

        return event.toString();
    }


    public static String createEngine(Automata automata){
        
        Set<Integer> finalStates = automata.getFinalState();
        int stateN = automata.getStateN();
        int q0 = automata.getQ0();

        StringBuilder engine = new StringBuilder();
        engine.append("package cepl.motor;\n");
        engine.append("public class Engine extends CELEngine {\n");

        engine.append("    public Engine(){\n");
        engine.append("        super(");
        engine.append(stateN + ", ");
        engine.append(q0 + ", ");
        engine.append("new boolean[]{");
        
        for (int q = 0; q < stateN ; q++){
            if (finalStates.contains(q)) engine.append("true");
            else engine.append("false");
            
            if (q < stateN - 1) engine.append(", ");
        }
        engine.append("});\n");
        engine.append("        set" + automata.getSemantic() + "();");
        engine.append("    }\n");


        engine.append("    @Override\n");
        engine.append("    protected int[] black_transition(int state, Event e){\n");

        /* black transitions */
        for (Transition t: automata.getBlackTransitions()){
            engine.append("        if (state == " + t.from + " " + t.formula + ") return new int[]{");
            Integer[] to = t.getTo();
            for (int i = 0; i < to.length; i++){
                engine.append(to[i]);
                if (i < to.length - 1) engine.append(", ");
            }
            engine.append("};\n");
        }

        engine.append("        return new int[0];\n");
        engine.append("    }\n");



        engine.append("    @Override\n");
        engine.append("    protected int[] white_transition(int state, Event e){\n");

        /* white transitions */
        for (Transition t: automata.getWhiteTransitions()){
            engine.append("        if (state == " + t.from + ") return new int[]{");
            Integer[] to = t.getTo();
            for (int i = 0; i < to.length; i++){
                engine.append(to[i]);
                if (i < to.length - 1) engine.append(", ");
            }
            engine.append("};\n");
        }
        engine.append("        return new int[0];\n");
        engine.append("    }\n");
    
        engine.append("}");
        return engine.toString();
    }
}