package cepl.compiler.errors;

public class NameError extends CompilerError {

    public NameError(String msg) {
        super("NameError: " + msg);
    }
}
