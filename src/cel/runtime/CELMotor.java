package cel.runtime;

import cel.compiler.CompoundStatementCompiler;
import cel.compiler.DeclarationCompiler;
import cel.compiler.QueryCompiler;
import cel.compiler.errors.CompilerError;
import cel.query.Query;
import cel.runtime.errors.ParseError;
import org.antlr.v4.runtime.misc.ParseCancellationException;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

public class CELMotor {

    private DeclarationCompiler declarationCompiler;
    private QueryCompiler queryCompiler;
    private CompoundStatementCompiler compoundStatementCompiler;

    public CELMotor() {
        declarationCompiler = new DeclarationCompiler();
        queryCompiler = new QueryCompiler();
        compoundStatementCompiler = new CompoundStatementCompiler(declarationCompiler, queryCompiler);
    }

    public void executeDeclaration(String statement) {
        try {
            // Parse and compile
            declarationCompiler.compile(statement);
        } catch (ParseCancellationException exc) {
            throw new ParseError("Can't parse the given string as a correct CEL declaration statement.");
        }
    }

    public QueryResult executeQuery(String query) {

        try {
            // try to parse and compile given the already declared events and streams
            queryCompiler.compile(query);
        } catch (ParseCancellationException exc) {
            throw new ParseError("Can't parse the given string as a correct CEL query statement.");
        }
        return new QueryResult();
    }

    public void executeFromFile(String path) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        String statements = new String(encoded, Charset.defaultCharset());

        try {
            Collection<Query> queries = compoundStatementCompiler.compile(statements);
            for (Query query : queries) {
//                System.out.println(query.getPatternCEA().toString());
            }
        } catch (CompilerError err) {
            System.err.println(err);
        }
    }

    public void sendEvent(String streamName, String eventName, Object... args) {

    }

}
