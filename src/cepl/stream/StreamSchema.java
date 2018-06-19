package cepl.stream;

import cepl.event.EventSchema;
import cepl.stream.errors.StreamException;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class StreamSchema {

    // this is the ID that represents NO stream in particular
    public static int ANY_STREAM_ID = 0;

    private static Map<String, StreamSchema> allSchemas = new HashMap<>();

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
        if (allSchemas.keySet().contains(name)){
            throw new StreamException("Stream of name \"" + name + "\" has already been declared");
        }
    }

    public StreamSchema(String name, Collection<EventSchema> events) throws StreamException {
        ensureUnique(name);
        this.name = name;
        this.events = events;
        allSchemas.put(name, this);
        streamID = allSchemas.size();
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

    public boolean containsEvent(EventSchema e){
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
