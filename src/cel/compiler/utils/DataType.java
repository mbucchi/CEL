package cel.compiler.utils;

public class DataType {

    public static Class getClassFor(String dataType){
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
                return null;
        }
    }

}
