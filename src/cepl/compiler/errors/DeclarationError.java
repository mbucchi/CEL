package cepl.compiler.errors;

public class DeclarationError extends CompilerError {
    public DeclarationError(String msg) {
        super("DeclarationError: " + msg);
    }
}
