package cepl.compiler.utils;

import cepl.compiler.errors.UnknownDataTypeError;

public class DataType {

    public static Class getTypeFor(String dataType){
        switch (dataType){
            case "int":
                return int.class;
            case "long":
                return long.class;
            case "string":
                return String.class;
            case "double":
                return double.class;
            default:
                throw new UnknownDataTypeError("Invalid dataType: `" + dataType + "`");
        }
    }

}
