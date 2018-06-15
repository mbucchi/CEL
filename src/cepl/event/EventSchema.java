package cepl.event;

import cepl.cea.utils.Label;
import cepl.cea.utils.NoSuchLabelException;

import java.util.*;


public class EventSchema {

    private static Map<String, EventSchema> allSchemas = new HashMap<>();
    private static final String TIMESTAMP_ATTR = "__ts";
    private static final String STREAM_ID_ATTR = "__stream";
    private static final String INDEX_ATTR = "__idx";
    private static final String TYPE_ATTR = "__type";

    private static final Map<String, Class> restrictedAttributes = Map.ofEntries(
            Map.entry(TIMESTAMP_ATTR, long.class),      // timestamp
            Map.entry(STREAM_ID_ATTR, int.class),       // stream from which the event is coming
            Map.entry(INDEX_ATTR, long.class),          // event position in event feed
            Map.entry(TYPE_ATTR, int.class)             // event type as int
    );

    public static EventSchema tryGetSchemaFor(String eventName) {
        // can return null
        return allSchemas.get(eventName);
    }

    public static EventSchema getSchemaFor(String eventName) {
        EventSchema eventSchema = allSchemas.get(eventName);
        if (eventSchema == null) {
            throw new Error("There is no existing schema for event of name \"" + eventName + "\"");
        }
        return eventSchema;
    }

    public static Map<String, EventSchema> getAllSchemas() {
        return new HashMap<>(allSchemas);
    }

    private static void ensureUnique(String name) throws EventException {
        if (allSchemas.keySet().contains(name)) {
            throw new EventException("Event of name \"" + name + "\" has already been declared");
        }
    }

    private static void checkAttributes(Set<String> attrNames, String evName) throws EventException {
        Set<String> intersection = new HashSet<>(restrictedAttributes.keySet());
        intersection.retainAll(attrNames);

        if (intersection.size() > 0) {
            String restricted = String.join("\", \"", intersection);
            throw new EventException("Event of name \"" + evName + "\" " +
                    "declares attributes with restricted names " +
                    "(\"" + restricted + "\")");
        }

    }

    private String name;
    private Map<String, Class> attributes;
    private int eventType;

    public EventSchema(String name, Map<String, Class> attributes) throws EventException {

        // perform some needed checks
        ensureUnique(name);
        checkAttributes(attributes.keySet(), name);

        this.name = name;

        // copy the map so that we can modify it
        this.attributes = new HashMap<>(attributes);

        this.attributes.putAll(restrictedAttributes);
        allSchemas.put(name, this);
        eventType = allSchemas.size();

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

    public Map<String, Class> getAttributes() {
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

