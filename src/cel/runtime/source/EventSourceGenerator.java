package cel.runtime.source;

import cel.event.EventSchema;
import cel.values.ValueType;

import java.util.Map;

public class EventSourceGenerator {

    private static int WIDTH = 4;

    public static String createEventSource(EventSchema ev) {
        StringBuilder ret = new StringBuilder();

        ret.append(makeHeaders());
        ret.append(makeBody(ev));

        return ret.toString();
    }

    private static String makeHeaders() {
        StringBuilder ret = new StringBuilder();

        ret.append("package cel.runtime.event;\n\n");
        ret.append("import cel.runtime.event.Event;\n\n");

        return ret.toString();
    }

    private static String makeBody(EventSchema ev) {
        StringBuilder ret = new StringBuilder();

        ret.append("public class ").append(ev.getName()).append(" extends Event {\n\n");
        ret.append(getVariables(ev));
        ret.append("\n");
        ret.append(getInit(ev));
        ret.append("\n");
        ret.append(getSetter(ev));
        ret.append("\n");
//        ret.append(getGetter(ev));
        ret.append(getToString(ev));
        ret.append("}");

        return ret.toString();
    }

    private static String getVariables(EventSchema ev) {
        StringBuilder ret = new StringBuilder();

        for (Object o : ev.getAttributes().entrySet()) {
            Map.Entry pair = (Map.Entry) o;

            /* These values are already defined in Event class */
            if (pair.getKey().equals("__ts") || pair.getKey().equals("__idx") || pair.getKey().equals("__stream")) {
                continue;
            }
            ret.append(indent(1));
            ret.append("public ");
            getType(ret, pair);
            ret.append(" ");
            ret.append(pair.getKey());
            ret.append(";\n");
        }

        return ret.toString();
    }

    private static String getInit(EventSchema ev) {
        StringBuilder ret = new StringBuilder();

        ret.append(indent(1)).append("public ").append(ev.getName()).append("() {\n");
        ret.append(indent(2)).append("super();\n");
        ret.append(indent(1)).append("}\n");

        return ret.toString();
    }

    private static void getType(StringBuilder ret, Map.Entry pair) {
        if (pair.getValue().equals(ValueType.LONG)) {
            ret.append("long");
        } else if (pair.getValue().equals(ValueType.INTEGER)) {
            ret.append("int");
        } else if (pair.getValue().equals(ValueType.DOUBLE)) {
            ret.append("double");
        } else if (pair.getValue().equals(ValueType.STRING)) {
            ret.append("String");
        }
    }

    private static String getSetter(EventSchema ev) {
        StringBuilder ret = new StringBuilder();

        ret.append(indent(1)).append("public void setValues(Object... args) throws IllegalAccessError { \n");
        ret.append(indent(2)).append("if (args.length != ").append(ev.getAttributes().size()).append(") throw new IllegalAccessError(\"Tried to assign \" + args.length + \" fields on event of type \\\"").append(ev.getName()).append("\\\", expected ").append(ev.getAttributes().size()).append("\");\n");

        ret.append(indent(2)).append("this.__stream = (int) args[0];\n");
        ret.append(indent(2)).append("this.__idx = (long) args[1];\n");
        ret.append(indent(2)).append("this.__ts = (long) args[2];\n");
        ret.append(indent(2)).append("this.__type = (int) args[3];\n");

        int i = 4;
        for (Object o : ev.getAttributes().entrySet()) {
            Map.Entry pair = (Map.Entry) o;

            if (pair.getKey().equals("__ts") || pair.getKey().equals("__idx") || pair.getKey().equals("__stream") || pair.getKey().equals("__type")) {
                continue;
            }

            ret.append(indent(2));
            ret.append("this.");
            ret.append(pair.getKey());
            ret.append(" = (");
            getType(ret, pair);
            ret.append(") args[").append(i).append("]");
            ret.append(";\n");
            i++;
        }



        ret.append(indent(1)).append("}\n");
        return ret.toString();
    }

    private static String getGetter(EventSchema ev) {
        StringBuilder ret = new StringBuilder();

        ret.append(indent(1)).append("public Object getValues(String field) throws IllegalAccessError { \n");
        for (Object o : ev.getAttributes().entrySet()) {
            Map.Entry pair = (Map.Entry) o;

            ret.append(indent(2)).append("if (field.equals(\"").append(pair.getKey()).append("\")) {\n");
            ret.append(indent(3)).append("return ").append(pair.getKey()).append("\n");
            ret.append(indent(2)).append("}\n");

        }
        ret.append(indent(2));
        ret.append("throw new IllegalAccessError(\"No field of name \\\"\" + field + \"\\\" present on event of type \\\"").append(ev.getName()).append("\\\"\");\n");
        ret.append(indent(1)).append("}\n");

        return ret.toString();
    }

    private static String getToString(EventSchema ev) {
        StringBuilder ret = new StringBuilder();

        ret.append(indent(1)).append("public String toString() {\n");
        ret.append(indent(2)).append("return \"");
        ret.append(ev.getName()).append("(");
        boolean first = true;
        for (Object o : ev.getAttributes().entrySet()) {
            Map.Entry pair = (Map.Entry) o;

            if (first) {
                first = false;
            } else {
                ret.append(", ");
            }

            ret.append(pair.getKey()).append("=\" + ").append(pair.getKey()).append(" + \"");

        }
        ret.append(")\";\n");
        ret.append(indent(1)).append("}\n");

        return ret.toString();
    }

    private static String indent(int level) {
        return new String(new char[level * WIDTH]).replace("\0", " ");
    }

}
