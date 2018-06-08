package cepl.cea.utils;

import java.util.HashMap;
import java.util.Map;

public class Label {

    private static Map<String, Label> stringLabelMap = new HashMap<>();

    public static Label forName(String name){
        Label label = stringLabelMap.get(name);
        if (label == null){
            label = new Label(name);
            stringLabelMap.put(name, label);
        }
        return label;
    }

    private String name;

    private Label(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
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
