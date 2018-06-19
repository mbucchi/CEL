package cepl.compiler.errors;

public class ValueError extends CompilerError {
    public ValueError(String msg) {
        super("ValueError: " + msg);
    }
}
