package cel.runtime.source;

import cel.event.EventSchema;
import cel.values.ValueType;

import java.util.Iterator;
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
            ret.append(pair.getKey());
            ret.append(";\n");
        }

        return ret.toString();
    }

    private static String getInit(EventSchema ev) {
        StringBuilder ret = new StringBuilder();

        ret.append(indent(1)).append("public ").append(ev.getName()).append("(long __ts, int __stream, long __idx");
        for (Object o : ev.getAttributes().entrySet()) {
            Map.Entry pair = (Map.Entry) o;

            /* These values are already defined in Event class */
            if (pair.getKey().equals("__ts") || pair.getKey().equals("__idx") || pair.getKey().equals("__stream")) {
                continue;
            }
            ret.append(", ");
            getType(ret, pair);
            ret.append(pair.getKey());
        }
        ret.append(") {\n");
        ret.append(indent(2)).append("super(__ts, __stream, __idx);\n");
        for (Object o : ev.getAttributes().entrySet()) {
            Map.Entry pair = (Map.Entry) o;

            /* These values are already defined in Event class */
            if (pair.getKey().equals("__ts") || pair.getKey().equals("__idx") || pair.getKey().equals("__stream")) {
                continue;
            }
            ret.append(indent(2)).append("this.").append(pair.getKey()).append(" = ").append(pair.getKey());
            ret.append(";\n");
        }
        ret.append(indent(1)).append("}\n");

        return ret.toString();
    }

    private static void getType(StringBuilder ret, Map.Entry pair) {
        if (pair.getValue().equals(ValueType.LONG)) {
            ret.append("long ");
        } else if (pair.getValue().equals(ValueType.INTEGER)) {
            ret.append("int ");
        } else if (pair.getValue().equals(ValueType.DOUBLE)) {
            ret.append("double ");
        } else if (pair.getValue().equals(ValueType.STRING)) {
            ret.append("String ");
        }
    }

    private static String indent(int level) {
        return new String(new char[level * WIDTH]).replace("\0", " ");
    }

}
