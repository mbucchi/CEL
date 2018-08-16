import cel.compiler.CompoundStatementCompiler;
import cel.compiler.DeclarationCompiler;
import cel.compiler.QueryCompiler;
import cel.query.Query;

import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;

public class QueryGetter {

    static Query getQuery(String queryPath) {
        DeclarationCompiler declarationCompiler;
        QueryCompiler queryCompiler;
        CompoundStatementCompiler compoundStatementCompiler;
        Query q = null;
        try {
            byte[] encoded = Files.readAllBytes(Paths.get(queryPath));
            String statements = new String(encoded, Charset.defaultCharset());
            declarationCompiler = new DeclarationCompiler();
            queryCompiler = new QueryCompiler();
            compoundStatementCompiler = new CompoundStatementCompiler(declarationCompiler, queryCompiler);
            Collection<Query> queries = compoundStatementCompiler.compile(statements);
            q = queries.iterator().next();

        } catch (Exception err) {
            System.out.println(err);
            err.printStackTrace();
            System.exit(1);
        }
        return q;
    }
}
