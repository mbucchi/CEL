package cel.compiler.errors;

import org.antlr.v4.runtime.ParserRuleContext;

public class UnknownStatementError extends CompilerError {
    public UnknownStatementError(String msg, ParserRuleContext context) {
        super(msg, context);
    }
}
