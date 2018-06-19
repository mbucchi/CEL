package cel.compiler.errors;

import org.antlr.v4.runtime.ParserRuleContext;

public class UnknownDataTypeError extends CompilerError{
    public UnknownDataTypeError(String msg, ParserRuleContext context) {
        super(msg, context);
    }
}
