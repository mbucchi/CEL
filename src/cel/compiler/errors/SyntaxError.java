package cel.compiler.errors;

public class SyntaxError extends CompilerError {

    public SyntaxError(int lineNumber, int columnNumber, String msg){
        super(msg, lineNumber, columnNumber);
    }
}
