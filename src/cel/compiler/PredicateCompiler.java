package cel.compiler;

import cel.parser.CELParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class PredicateCompiler extends BaseCompiler<Void> {
    @Override
    public Void compile(String queryString) throws ParseCancellationException {
        CELParser CELParser = parse(queryString);

        // a query can only be valid if it parses on this parser rule
        CELParser.Cel_queryContext tree = CELParser.cel_query();

        return compileContext(tree);
    }

    @Override
    Void compileContext(ParserRuleContext ctx) {
        return null;
    }
}
