package cel.runtime;

import cel.compiler.CompoundStatementCompiler;
import cel.compiler.DeclarationCompiler;
import cel.compiler.QueryCompiler;
import cel.compiler.errors.CompilerError;
import cel.query.Query;
import cel.queryExecution.CeaExecutor;
import cel.runtime.errors.ParseError;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CELMotor {

    private DeclarationCompiler declarationCompiler;
    private QueryCompiler queryCompiler;
    private CompoundStatementCompiler compoundStatementCompiler;
    private List<CeaExecutor> ceaExecutorList;

    public CELMotor(){
        declarationCompiler = new DeclarationCompiler();
        queryCompiler = new QueryCompiler();
        compoundStatementCompiler = new CompoundStatementCompiler(declarationCompiler, queryCompiler);
        ceaExecutorList = new ArrayList<>();
    }

    public void executeDeclaration(String statement){
        try {
            // Parse and compile
            declarationCompiler.compile(statement);
        }
        catch (ParseCancellationException exc){
            throw new ParseError("Can't parse the given string as a correct CEL declaration statement.");
        }
    }

    public QueryResult executeQuery(String query){

        try {
            // try to parse and compile given the already declared events and streams
            Query compiledQuery = queryCompiler.compile(query);
            CeaExecutor ceaExecutor = new CeaExecutor(compiledQuery);
            ceaExecutorList.add(ceaExecutor);
            return new QueryResult(ceaExecutor);
        }
        catch (ParseCancellationException exc) {
            throw new ParseError("Can't parse the given string as a correct CEL query statement.");
        }
    }

    public void executeFromFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        String statements = new String(encoded, Charset.defaultCharset());

        try {
            Collection<Query> queries = compoundStatementCompiler.compile(statements);
            for (Query query : queries) {
                System.out.println(query.getPatternCEA().toString());
            }
        }
        catch (CompilerError err) {
            System.err.println(err);
        }
    }

    public void sendEvent(String streamName, String eventName, Object... args){

    }

}
