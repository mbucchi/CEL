package cel.stream;

import cel.event.EventSchema;
import cel.stream.errors.StreamException;

import java.util.*;

public class StreamSchema {

    // this is the ID that represents NO stream in particular
    private static int ANY_STREAM_ID = 0;

    private static Map<String, StreamSchema> allSchemas = new HashMap<>();

    // This stream represents all streams
    private static StreamSchema _ANY = new StreamSchema();

    public static StreamSchema ANY() {
        return _ANY;
    }

    public boolean isAny() {
        return _ANY.equals(this);
    }

    public static StreamSchema tryGetSchemaFor(String streamName) {
        // may return null
        return allSchemas.get(streamName);
    }

    public static StreamSchema getSchemaFor(String streamName) {
        StreamSchema streamSchema = allSchemas.get(streamName);
        if (streamSchema == null) {
            throw new Error("There is no existing schema for event of name \"" + streamName + "\"");
        }
        return streamSchema;
    }

    public static Map<String, StreamSchema> getAllSchemas() {
        return new HashMap<>(allSchemas);
    }

    private String name;
    private Collection<EventSchema> events;
    private int streamID;

    private void ensureUnique(String name) throws StreamException {
        if (allSchemas.keySet().contains(name)) {
            throw new StreamException("Stream of name \"" + name + "\" has already been declared");
        }
    }

    private StreamSchema() {
        this.name = "ANY";
        this.events = List.of(EventSchema.ANY());
        allSchemas.put(name, this);
        streamID = ANY_STREAM_ID;
    }

    public StreamSchema(String name, Collection<EventSchema> events) throws StreamException {
        ensureUnique(name);
        this.name = name;
        this.events = events;
        streamID = allSchemas.size();
        allSchemas.put(name, this);
    }

    public StreamSchema(String name) throws StreamException {
        this(name, new HashSet<>());
    }


    public boolean containsEvent(String eventName) {
        for (EventSchema ev : events) {
            if (ev.getName().equals(eventName)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsEvent(EventSchema e) {
        return containsEvent(e.getName());
    }

    public Collection<EventSchema> getEvents() {
        return events;
    }

    public String getName() {
        return name;
    }

    public int getStreamID() {
        return streamID;
    }
}
