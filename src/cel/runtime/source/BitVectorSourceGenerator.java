package cel.runtime.source;

import cel.cea.CEA;
import cel.cea.predicate.Predicate;
import cel.cea.transition.Transition;
import cel.event.EventSchema;
import cel.filter.*;
import cel.stream.StreamSchema;
import cel.values.Attribute;
import cel.values.Literal;
import cel.values.Value;
import cel.values.operations.*;

import java.util.*;

public class BitVectorSourceGenerator {

    private static int WIDTH = 4;

    private List<Object> bitVectorOrder;
    private List<EventFilter> filters;
    private List<StreamSchema> streams;
    private List<EventSchema> events;

    public BitVectorSourceGenerator(List<Object> filters) {
        this.bitVectorOrder = filters;
        getData();
    }

    private void getData() {
        filters = new ArrayList<>();
        streams = new ArrayList<>();
        events = new ArrayList<>();

        for (Object o : bitVectorOrder) {
            if (o instanceof EventFilter) {
                filters.add((EventFilter) o);
            } else if (o instanceof StreamSchema) {
                streams.add((StreamSchema) o);
            } else if (o instanceof EventSchema) {
                events.add((EventSchema) o);
            }
        }
    }

    public List<Object> getBitVectorOrder() {
        return bitVectorOrder;
    }

    public String makeSourceCode() {
        StringBuilder src = new StringBuilder();

        src.append("package cel.runtime;\n\n");
        src.append(getImports());
        src.append("public class BVG extends BitVectorGenerator {\n\n");
        src.append(getInit());
        src.append(indent(1)).append("public BitSet getBitVector(Event e) {\n");
        src.append(getEvents());
        src.append(indent(2)).append("return vector;\n");
        src.append(indent(1)).append("}\n");
        src.append("}");

        return src.toString();
    }

    private String getInit() {
        StringBuilder ret = new StringBuilder();
        ret.append(indent(1)).append("public BVG() {\n").append(indent(1)).append("}\n\n");
        return ret.toString();
    }

    private String getImports() {
        StringBuilder ret = new StringBuilder();

        ret.append("import java.util.*;\n");
        ret.append("import cel.runtime.event.*;\n");
        ret.append("import cel.filter.EventFilter;\n");
        ret.append("\n");

        return ret.toString();
    }

    private String getEvents() {
        StringBuilder ret = new StringBuilder();
        ret.append(indent(2)).append("BitSet vector = new BitSet(").append(bitVectorOrder.size()).append(");\n\n");
        boolean firstEvent = true;
        if (events.isEmpty()) {
//            throw ("");
            return "";
        }
        for (EventSchema ev : events) {
            if (firstEvent) {
                firstEvent = false;
                ret.append(indent(2)).append("if (e instanceof ");
            } else {
                ret.append(" else if (e instanceof ");

            }
            ret.append(ev.getName()).append(") {\n");
            ret.append(indent(3)).append("vector.set(").append(bitVectorOrder.indexOf(ev)).append(");\n");
            ret.append(getStreams(ev));
            ret.append(getFilters(ev));
            ret.append(indent(2)).append("}");
        }
        ret.append(" else {\n");
        ret.append(indent(3)).append("throw new Error(\"Unknown event \" + e.toString());\n");
        ret.append(indent(2)).append("}\n");

        return ret.toString();
    }

    private String getStreams(EventSchema ev) {
        StringBuilder ret = new StringBuilder();
        boolean first = true;
        if (streams.isEmpty()) {
            return "";
        }
        for (StreamSchema st : streams) {
            if (st.containsEvent(ev)) {
                if (first) {
                    first = false;
                    ret.append(indent(3)).append("if (e.__stream == ").append(st.getStreamID()).append(") {\n");
                } else {
                    ret.append(" else if (e.__stream == ").append(st.getStreamID()).append(") {\n");
                }
                ret.append(indent(4)).append("vector.set(").append(bitVectorOrder.indexOf(st)).append(");\n");
                ret.append(indent(3)).append("}");
            }
        }
        ret.append(" else {\n");
        ret.append(indent(4)).append("throw new Error(\"Unknown stream \" + e.__stream + \" for event ").append(ev.getName()).append("\");\n");
        ret.append(indent(3)).append("}\n");

        return ret.toString();
    }

    private String getFilters(EventSchema ev) {
        StringBuilder ret = new StringBuilder();

        for (EventFilter f : filters) {
            if (f.getLabel().getEventSchemas().contains(ev)) {
                ret.append(indent(3)).append("if (");
                ret.append(getFilter(f, ev));
                ret.append(") {\n");
                ret.append(indent(4)).append("vector.set(").append(bitVectorOrder.indexOf(f)).append(");\n");
                ret.append(indent(3)).append("}\n");
            }
        }
        return ret.toString();
    }

    private String getFilter(EventFilter f, EventSchema ev) {
        StringBuilder ret = new StringBuilder();

        boolean first = true;
        if (f instanceof AndEventFilter) {
            for (EventFilter af : ((AndEventFilter) f).getEventFilterCollection()) {
                if (first) {
                    first = false;
                } else {
                    ret.append(" && ");
                }
                ret.append(getFilter(af, ev));
            }
        } else if (f instanceof OrEventFilter) {
            for (EventFilter af : ((OrEventFilter) f).getEventFilterCollection()) {
                if (first) {
                    first = false;
                } else {
                    ret.append(" || ");
                }
                ret.append(getFilter(af, ev));
            }
        } else if (f instanceof AtomicEventFilter) {
            ret.append(getAtomicFilter((AtomicEventFilter) f, ev));
        }
        return ret.toString();
    }

    private String getAtomicFilter(AtomicEventFilter f, EventSchema ev) {
        StringBuilder ret = new StringBuilder();

        ret.append(getValue(f.getLhs(), ev));
        ret.append(" ");
        ret.append(f.getLogicalOperation().toString());
        ret.append(" ");
        ret.append(getValue(f.getRhs(), ev));

        return ret.toString();
    }

    private String getValue(Value v, EventSchema ev) {
        StringBuilder ret = new StringBuilder();

        ret.append("(");
        if (v instanceof BinaryOperation) {
            ret.append(getValue(((BinaryOperation) v).getLhs(), ev));
            if (v instanceof Addition) {
                ret.append(" + ");
            } else if (v instanceof Division) {
                ret.append(" / ");
            } else if (v instanceof Modulo) {
                ret.append(" % ");
            } else if (v instanceof Multiplication) {
                ret.append(" * ");
            } else if (v instanceof Subtraction) {
                ret.append(" - ");
            }
            ret.append(getValue(((BinaryOperation) v).getRhs(), ev));
        } else if (v instanceof UnaryOperation) {
            if (v instanceof Negation) {
                ret.append("-");
                ret.append(getValue(((Negation) v).getInner(), ev));
            }
        } else if (v instanceof Literal) {
            if (v instanceof Attribute) {
                ret.append("((").append(ev.getName()).append(")e).").append(v.toString());
            } else {
                ret.append(v.toString());
            }
        }

        ret.append(")");
        return ret.toString();
    }

    private String indent(int level) {
        return new String(new char[level * WIDTH]).replace("\0", " ");
    }
}
