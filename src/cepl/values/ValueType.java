package cepl.values;

import java.util.EnumSet;
import java.util.Set;

public enum ValueType {
    NUMERIC(Set.of(int.class, double.class, float.class, long.class)),
    STRING(Set.of(String.class));

    public static EnumSet<ValueType> ANY(){
        return EnumSet.allOf(ValueType.class);
    }

    private Set<Class> validDataTypes;
    ValueType(Set<Class> validDataTypes){
        this.validDataTypes = validDataTypes;
    }
    public boolean validForDataType(Class dataType) {
        return validDataTypes.contains(dataType);
    }
}
