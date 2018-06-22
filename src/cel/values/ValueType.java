package cel.values;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public enum ValueType {
    NUMERIC(Set.of(int.class, double.class, long.class)),
    INTEGER(Set.of(int.class)),
    LONG(Set.of(long.class)),
    DOUBLE(Set.of(double.class)),
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

    public Set<Class> getDataTypes() {
        return new HashSet<>(validDataTypes);
    }

    public static ValueType getValueFor(String dataType){
        switch (dataType){
            case "double":
                return DOUBLE;
            case "int":
                return INTEGER;
            case "long":
                return LONG;
            case "string":
                return STRING;
            default:
                throw new Error("Unknown data type: " + dataType);
        }
    }

    public boolean interoperableWith(ValueType valueType){
        Set<Class> dataTypes = new HashSet<>(validDataTypes);
        dataTypes.retainAll(valueType.validDataTypes);
        return dataTypes.size() > 0;
    }

    public EnumSet<ValueType> getEnumSet() {
        EnumSet<ValueType> enumSet = EnumSet.noneOf(ValueType.class);
        for (ValueType valueType : ValueType.values()) {
            if (this.interoperableWith(valueType)){
                enumSet.add(valueType);
            }
        }
        return enumSet;
    }
}
