package cepl.event;

import cepl.event.errors.NoSuchLabelException;

import java.util.*;

public class Label {

    private static Map<String, Label> stringLabelMap = new HashMap<>();

    private Set<EventSchema> eventSchemas;

    public static Label forName(String name, Set<EventSchema> eventSchemaSet){
        Label label = stringLabelMap.get(name);
        if (label == null){
            label = new Label(name);
            stringLabelMap.put(name, label);
        }
        label.eventSchemas.addAll(eventSchemaSet);
        return label;
    }

    private String name;

    private Label(String name) {
        this.name = name;
        this.eventSchemas = new HashSet<>();
    }

    public String getName() {
        return name;
    }

    public static Label get(String name) throws NoSuchLabelException {
        Label label = stringLabelMap.get(name);
        if (label == null){
            throw new NoSuchLabelException("No label defined for name " + name);
        }
        return label;
    }

    public Set<EventSchema> getEventSchemas() {
        return new HashSet<>(eventSchemas);
    }

    public Map<String, Set<Class>> getAttributes(){
        // returns all the attributes and their respective classes defined on eventSchemas
        // within this label.

        Map<String, Set<Class>> attributeClassSetMap = new HashMap<>();

        for (EventSchema eventSchema : eventSchemas ){
            Map<String, Class> evAttMap = eventSchema.getAttributes();
            for (String attrName : evAttMap.keySet()){
                if (!attributeClassSetMap.containsKey(attrName)){
                    attributeClassSetMap.put(attrName, new HashSet<>());
                }
                Class attributeClass = evAttMap.get(attrName);
                attributeClassSetMap.get(attrName).add(attributeClass);
            }
        }

        return attributeClassSetMap;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Label) return name.equals(((Label) obj).name);
        return false;
    }

    @Override
    public String toString() {
        return name;
    }
}
