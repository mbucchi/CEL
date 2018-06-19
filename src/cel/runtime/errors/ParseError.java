package cel.runtime.errors;

public class ParseError extends Error {
    public ParseError(String msg){
        super(msg);
    }
}
