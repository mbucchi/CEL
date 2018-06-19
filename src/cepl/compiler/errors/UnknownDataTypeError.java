package cepl.compiler.errors;

public class UnknownDataTypeError extends CompilerError{
    public UnknownDataTypeError(String msg) {
        super("UnknownDataTypeError: " + msg);
    }
}
