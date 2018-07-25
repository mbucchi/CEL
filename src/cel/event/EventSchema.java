package cel.event;

import cel.event.errors.EventException;
import cel.event.errors.NoSuchLabelException;
import cel.values.ValueType;

import java.util.*;


public class EventSchema {

    private static Map<String, EventSchema> allSchemas = new HashMap<>();
    private static final String TIMESTAMP_ATTR = "__ts";
    private static final String STREAM_ID_ATTR = "__stream";
    private static final String INDEX_ATTR = "__idx";
    private static final String TYPE_ATTR = "__type";

    private static final Map<String, ValueType> restrictedAttributes = Map.ofEntries(
            Map.entry(TIMESTAMP_ATTR, ValueType.LONG),      // timestamp
            Map.entry(STREAM_ID_ATTR, ValueType.INTEGER),   // stream from which the event is coming
            Map.entry(INDEX_ATTR, ValueType.LONG),          // event position in event feed
            Map.entry(TYPE_ATTR, ValueType.INTEGER)         // event type as int
    );

    private static EventSchema _ANY = new EventSchema();

    public static EventSchema ANY() {
        return _ANY;
    }

    public boolean isAny() {
        return _ANY.equals(this);
    }

    public static EventSchema tryGetSchemaFor(String eventName) {
        // can return null
        return allSchemas.get(eventName);
    }

    public static EventSchema getSchemaFor(String eventName) throws EventException {
        EventSchema eventSchema = allSchemas.get(eventName);
        if (eventSchema == null) {
            throw new EventException("There is no existing schema for event of name `" + eventName + "`");
        }
        return eventSchema;
    }

    public static Map<String, EventSchema> getAllSchemas() {
        return new HashMap<>(allSchemas);
    }

    private static void ensureUnique(String name) throws EventException {
        if (allSchemas.keySet().contains(name)) {
            throw new EventException("Event of name `" + name + "` has already been declared");
        }
    }

    private static void checkAttributes(Set<String> attrNames, String evName) throws EventException {
        Set<String> intersection = new HashSet<>(restrictedAttributes.keySet());
        intersection.retainAll(attrNames);

        if (intersection.size() > 0) {
            String restricted = String.join("`, `", intersection);
            throw new EventException("Event of name `" + evName + "` " +
                    "declares attributes with restricted names " +
                    "(`" + restricted + "`)");
        }

    }

    private String name;
    private Map<String, ValueType> attributes;
    private int eventType;

    private EventSchema() {
        this.name = "__ANY_EVENT";

        // copy the map so that we can modify it
        this.attributes = new HashMap<>();
        this.attributes.putAll(restrictedAttributes);
        eventType = allSchemas.size();
        allSchemas.put(name, this);
        Label.forName(name, Set.of(this));
    }

    public EventSchema(String name, Map<String, ValueType> attributes) throws EventException {

        // perform some needed checks
        ensureUnique(name);
        checkAttributes(attributes.keySet(), name);

        this.name = name;

        // copy the map so that we can modify it
        this.attributes = new HashMap<>(attributes);

        this.attributes.putAll(restrictedAttributes);
        eventType = allSchemas.size();
        allSchemas.put(name, this);
        Label.forName(name, Set.of(this));
    }

    public EventSchema(String name) throws EventException {
        this(name, new HashMap<>());
    }


    public String getName() {
        return name;
    }

    public Label getNameLabel() {
        try {
            return Label.get(name);
        }
        catch (NoSuchLabelException exc) {
            return Label.forName(name, Set.of(this));
        }
    }

    public int getEventType() {
        return eventType;
    }

    public Map<String, ValueType> getAttributes() {
        return attributes;
    }

    @Override
    public String toString() {
        return "EventSchema{" +
                "name='" + name + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}

