package cel.compiler.errors;

import org.antlr.v4.runtime.ParserRuleContext;

public class NameError extends CompilerError {

    public NameError(String msg, ParserRuleContext context) {
        super(msg, context);
    }
}
