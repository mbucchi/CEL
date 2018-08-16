package cel.compiler;

import cel.compiler.errors.ErrorListener;
import cel.parser.CELLexer;
import cel.parser.CELParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class BaseCompiler<T> {

    private static String input;

    public static String getLatestInput() {
        if (input == null) return "";
        return input;
    }

    public static String getLatestInput(int lineNumber) {
        if (input == null)
            return "";

        String[] lines = input.split("\n\r?");

        if (lineNumber > lines.length)
            return "";

        return lines[lineNumber - 1];
    }

    CELParser parse(String cmd) {
        input = cmd;
        CharStream charStream = CharStreams.fromString(cmd);
        CELLexer CELLexer = new CELLexer(charStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(CELLexer);
        CELParser CELParser = new CELParser(commonTokenStream);

        CELParser.removeErrorListeners();
        CELParser.addErrorListener(new ErrorListener());

        return CELParser;
    }

    public abstract T compile(String statement);

    abstract T compileContext(ParserRuleContext ctx);
}
