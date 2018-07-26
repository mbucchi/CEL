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

    private CEA cea;
    private List<Object> bitVectorOrder;
    private List<EventFilter> filters;
    private List<StreamSchema> streams;
    private List<EventSchema> events;
    private Map<Object, Integer> filterRepetitions;


    public BitVectorSourceGenerator(CEA cea) {
        this.cea = cea;
        makeFilterOrder();
    }

    /* TODO */
    public BitVectorSourceGenerator(CEA cea, List<Object> events) {
        this.cea = cea;
        this.bitVectorOrder = events;
    }

    public String makeSourceCode() {
        StringBuilder src = new StringBuilder();

        src.append("package cel.runtime;\n\n");
        src.append(getImports());
        src.append("public class BitVectorGenerator {\n\n");
//        src.append(getInstanceVariables());
//        src.append(indent(1)).append("public BitVectorGenerator(List<EventFilter> events) {\n");
//        src.append(makeInit());
//        src.append(indent(1)).append("}\n\n");
        src.append(indent(1)).append("public static boolean getBitVector(Event e) {\n");
        src.append(getEvents());
        src.append(indent(2)).append("return vector;\n");
        src.append(indent(1)).append("}\n");
        src.append("}");    

        return src.toString();
    }

    private String getImports() {
        StringBuilder ret = new StringBuilder();

        ret.append("import java.util.*;\n");
        ret.append("import cel.event.Event;\n");
        ret.append("import cel.filter.EventFilter;\n");
        ret.append("\n");

        return ret.toString();
    }

//    private String getInstanceVariables() {
//        StringBuilder ret = new StringBuilder();
//
//        ret.append(indent(1)).append("private List<EventFilter> events;\n");
//        ret.append("\n");
//
//        return ret.toString();
//    }
//
//    private String makeInit() {
//        StringBuilder ret = new StringBuilder();
//
//        ret.append(indent(2)).append("this.events = events;\n");
//
//        return ret.toString();
//    }

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

        for (StreamSchema st : streams) {
            if (st.containsEvent(ev)) {
                if (first) {
                    first = false;
                    ret.append(indent(3)).append("if (e.__stream.equals(").append(st.getName()).append(")) {\n");
                } else {
                    ret.append(" else if (e.__stream.equals(").append(st.getName()).append(") {\n");
                }
                ret.append(indent(4)).append("vector.set(").append(bitVectorOrder.indexOf(st)).append(");\n");
                ret.append(indent(3)).append("}");
            }
        }
        ret.append(" else {\n");
        ret.append(indent(4)).append("throw new Error(\"Unknown stream \" + e.__stream);\n");
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

    private void makeFilterOrder() {
        makeFilterRepetitionsMap();
        Comparator<Object> comp = (o1, o2) -> {
            if (o1.equals(o2)) {
                return 0;
            }
            if (filterRepetitions.get(o1).equals(filterRepetitions.get(o2))) {
                /* TODO: MAKE SORTING DETERMINISTIC */
//                if ()
                return 0;
            }
            if (filterRepetitions.get(o1) > filterRepetitions.get(o2)) {
                return -1;
            }
            return 1;
        };

        bitVectorOrder = new ArrayList<>();
        bitVectorOrder.addAll(filterRepetitions.keySet());
        bitVectorOrder.sort(comp);
    }

    private void makeFilterRepetitionsMap() {
        filterRepetitions = new HashMap<>();
        streams = new ArrayList<>();
        filters = new ArrayList<>();
        events = new ArrayList<>();

        for (Transition t : cea.getTransitions()) {
            Predicate p = t.getPredicate();
            if (p.hasChildren()) {
                for (Predicate child : p.getPredicates()) {
                    putStreamSchema(child.getStreamSchema());
                    putEventSchema(child.getEventSchema());
                    /* Predicates should not have repeated filters inside */
                    for (EventFilter e : child.getFilterCollection()) {
                        putEventFilter(e);
                    }
                }
            } else {
                putStreamSchema(p.getStreamSchema());
                putEventSchema(p.getEventSchema());
                for (EventFilter e : p.getFilterCollection()) {
                    putEventFilter(e);
                }
            }
        }
    }

    private void putEventFilter(EventFilter e) {
        if (filterRepetitions.containsKey(e)) {
            filterRepetitions.put(e, filterRepetitions.get(e) + 1);
        } else if (filterRepetitions.containsKey(e.negate())) {
            filterRepetitions.put(e.negate(), filterRepetitions.get(e.negate()) + 1);
        } else {
            filterRepetitions.put(e, 1);
            filters.add(e);
        }
    }

    private void putStreamSchema(StreamSchema s) {
        if (s == null) {
            return;
        }
        if (filterRepetitions.containsKey(s)) {
            filterRepetitions.put(s, filterRepetitions.get(s) + 1);
        } else {
            filterRepetitions.put(s, 1);
            streams.add(s);
        }
    }

    private void putEventSchema(EventSchema e) {
        if (e == null) {
            return;
        }
        if (filterRepetitions.containsKey(e)) {
            filterRepetitions.put(e, filterRepetitions.get(e) + 1);
        } else {
            filterRepetitions.put(e, 1);
            events.add(e);
        }
    }

}