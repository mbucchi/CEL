package cel.compiler;

import cel.parser.CELParser;
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

        CELParser CELParser = parse(statements);
        CELParser.ParseContext ctx = CELParser.parse();

        return compileContext(ctx);
    }

    @Override
    Collection<Query> compileContext(ParserRuleContext ctx) {

        if (!(ctx instanceof CELParser.ParseContext)){
            throw new Error("FATAL ERROR!");
        }

        CELParser.ParseContext parseContext = (CELParser.ParseContext) ctx;

        Collection<Query> queries = new ArrayList<>();

        for (CELParser.Cel_stmtContext statementCtx : parseContext.cel_stmt()){
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
