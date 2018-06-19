package cepl.compiler.errors;

public class UnknownStatementError extends CompilerError {
    public UnknownStatementError(String msg){
        super("UnknownStatementError: " + msg);
    }
}
