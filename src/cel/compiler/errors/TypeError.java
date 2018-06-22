package cel.compiler.errors;

import org.antlr.v4.runtime.ParserRuleContext;

public class TypeError extends CompilerError {

    public TypeError(String msg, ParserRuleContext context) {
        super(msg, context);
    }

}
