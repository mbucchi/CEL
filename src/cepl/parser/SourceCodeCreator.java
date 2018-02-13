package cepl.parser;

import java.util.LinkedList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
            fieldDescription.append("entry(\"" + fName + "\", " + fieldTypes[i].getName() + ".class)");
            properties.append("    protected " + fieldTypes[i].getName() + " " + fName + ";\n");
            setters.append("        " + fName + " = (" + fieldTypes[i].getName() + ")args[" + i + "];\n");
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
        engine.append("        set" + automata.getSemantic() + "();\n");
        engine.append("    }\n");


        engine.append("    @Override\n");
        engine.append("    protected int[] black_transition(int state, Event e){\n");

        /* black transitions */
        List<Map<String, List<Transition>>> blackList = unifyTransitions(automata.getBlackTransitions(), stateN);
        boolean moreThanOne = false;
        for (int q = 0; q < stateN; q ++ ){
            Map<String, List<Transition>> stateMap = blackList.get(q);
            if (stateMap.size() == 0) continue;

            if (!moreThanOne) engine.append("        if (state == " + q + "){\n");
            else engine.append("        else if (state == " + q + "){\n");
            moreThanOne = true;

            String evTypes[] = stateMap.keySet().toArray(new String[0]);
            engine.append("            ");
            for (int idx = 0; idx < evTypes.length; idx++){
                String evType = evTypes[idx];
                engine.append("if (e instanceof " + evType + ") {\n"); 
                for (Transition t: stateMap.get(evType)){
                    engine.append("                ");
                    String formula = t.getFormula();
                    if (formula.length() > 0) engine.append("if (" + formula + ") ");
                    engine.append("return new int[]{");
                    Integer[] to = t.getTo();
                    for (int i = 0; i < to.length; i++){
                        engine.append(to[i]);
                        if (i < to.length - 1) engine.append(", ");
                    }
                    engine.append("};\n");
                }       
                engine.append("            }\n");
                engine.append("            ");
                if (idx < evTypes.length - 1) engine.append("else ");
            }
            engine.append("return new int[0];\n");
            engine.append("        }\n");            
        }

        engine.append("        return new int[0];\n");
        engine.append("    }\n");



        engine.append("    @Override\n");
        engine.append("    protected int[] white_transition(int state, Event e){\n");

        /* white transitions */
        List<Map<String, List<Transition>>> whiteList = unifyTransitions(automata.getWhiteTransitions(), stateN);
        moreThanOne = false;
        for (int q = 0; q < stateN; q ++ ){
            Map<String, List<Transition>> stateMap = whiteList.get(q);
            if (stateMap.size() == 0) continue;

            if (!moreThanOne) engine.append("        if (state == " + q + "){\n");
            else engine.append("        else if (state == " + q + "){\n");
            moreThanOne = true;

            String evTypes[] = stateMap.keySet().toArray(new String[0]);
            engine.append("            ");
            for (int idx = 0; idx < evTypes.length; idx++){
                String evType = evTypes[idx];
                engine.append("if (e instanceof " + evType + ") {\n"); 
                for (Transition t: stateMap.get(evType)){
                    engine.append("                ");
                    String formula = t.getFormula();
                    if (formula.length() > 0) engine.append("if (" + formula + ") ");
                    engine.append("return new int[]{");
                    Integer[] to = t.getTo();
                    for (int i = 0; i < to.length; i++){
                        engine.append(to[i]);
                        if (i < to.length - 1) engine.append(", ");
                    }
                    engine.append("};\n");
                }       
                engine.append("            }\n");
                engine.append("            ");
                if (idx < evTypes.length - 1) engine.append("else ");
            }
            engine.append("return new int[0];\n");
            engine.append("        }\n");            
        }

        engine.append("        return new int[0];\n");
        engine.append("    }\n");
    
        engine.append("}");

        return engine.toString();
    }

    private static List<Map<String, List<Transition>>> unifyTransitions(List<Transition> transitions, int stateN){
        List<Map<String, List<Transition>>> stateTrans = new ArrayList<Map<String, List<Transition>>>(stateN);

        for (int q = 0; q < stateN; q++){
            stateTrans.add(new HashMap<String, List<Transition>>());
        }

        for (Transition t: transitions) {
            boolean collapsed = false;
            Map<String, List<Transition>> stateMap = stateTrans.get(t.from);
            if (!stateMap.containsKey(t.evType)){
                stateMap.put(t.evType, new ArrayList<Transition>());
            }
            List<Transition> stateEventList = stateMap.get(t.evType);

            for (Transition t2: stateEventList){
                if (t.getEvType().equals(t2.getEvType()) && t.getFormula() == t2.getFormula()){
                    t2.to.addAll(t.to);
                    collapsed = true;
                    break;
                }
            }

            if (!collapsed){
                stateEventList.add(0, t);
            }
        }
        return stateTrans;
    }
}