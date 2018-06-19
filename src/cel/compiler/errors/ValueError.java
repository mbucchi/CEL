package cel.compiler.errors;

import org.antlr.v4.runtime.ParserRuleContext;

public class ValueError extends CompilerError {
    public ValueError(String msg, ParserRuleContext context) {
        super(msg, context);
    }
}
