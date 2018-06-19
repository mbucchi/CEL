package cel.compiler;

import cel.parser.CEPLParser;
import cel.query.Query;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.util.ArrayList;
import java.util.Collection;

public class CompoundStatementCompiler extends BaseCompiler<Collection<Query>> {

    private DeclarationCompiler declarationCompiler;
    private QueryCompiler queryCompiler;

    public CompoundStatementCompiler(DeclarationCompiler declarationCompiler, QueryCompiler queryCompiler) {
        this.declarationCompiler = declarationCompiler;
        this.queryCompiler = queryCompiler;
    }

    public Collection<Query> compile(String statements){

        CEPLParser ceplParser = parse(statements);
        CEPLParser.ParseContext ctx = ceplParser.parse();

        return compileContext(ctx);
    }

    @Override
    Collection<Query> compileContext(ParserRuleContext ctx) {

        if (!(ctx instanceof CEPLParser.ParseContext)){
            throw new Error("FATAL ERROR!");
        }

        CEPLParser.ParseContext parseContext = (CEPLParser.ParseContext) ctx;

        Collection<Query> queries = new ArrayList<>();

        for (CEPLParser.Cel_stmtContext statementCtx : parseContext.cel_stmt()){
            if (statementCtx.cel_declaration() != null){
                declarationCompiler.compileContext(statementCtx.cel_declaration());
            }
            else if (statementCtx.cel_query() != null){
                queries.add(queryCompiler.compileContext(statementCtx.cel_query()));
            }
            else {
                throw new ParseCancellationException("Error trying to parse given CEL string");
            }
        }

        return queries;
    }
}
