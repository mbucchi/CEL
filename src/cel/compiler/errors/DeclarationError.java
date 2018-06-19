package cel.compiler.errors;

import org.antlr.v4.runtime.ParserRuleContext;

public class DeclarationError extends CompilerError {
    public DeclarationError(String msg, ParserRuleContext context) {
        super(msg, context);
    }
}
