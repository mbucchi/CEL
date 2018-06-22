package cel.event;

import cel.event.errors.NoSuchLabelException;
import cel.values.ValueType;

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

    public Map<String, EnumSet<ValueType>> getAttributes(){
        // returns all the attributes and their respective ValueTypes defined on eventSchemas
        // within this label.

        Map<String, EnumSet<ValueType>> attributeValueTypes = new HashMap<>();

        for (EventSchema eventSchema : eventSchemas ){
            Map<String, ValueType> evAttMap = eventSchema.getAttributes();
            for (String attrName : evAttMap.keySet()){
                if (!attributeValueTypes.containsKey(attrName)){
                    attributeValueTypes.put(attrName, EnumSet.noneOf(ValueType.class));
                }
                ValueType valueType = evAttMap.get(attrName);
                attributeValueTypes.get(attrName).add(valueType);
            }
        }

        return attributeValueTypes;
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
