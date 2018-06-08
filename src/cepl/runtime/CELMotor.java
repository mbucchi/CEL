package cepl.runtime;

import cepl.compiler.DeclarationCompiler;
import cepl.compiler.QueryCompiler;
import cepl.event.EventSchema;
import cepl.runtime.errors.ParseError;
import cepl.stream.StreamSchema;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class CELMotor {

    private DeclarationCompiler declarationCompiler;
    private QueryCompiler queryCompiler;


    public CELMotor(){
        declarationCompiler = new DeclarationCompiler();
        queryCompiler = new QueryCompiler();
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
            queryCompiler.compile(query, EventSchema.getAllSchemas(), StreamSchema.getAllSchemas());
        }
        catch (ParseCancellationException exc) {
            throw new ParseError("Can't parse the given string as a correct CEL query statement.");
        }
        return new QueryResult();
    }

    public void sendEvent(String streamName, String eventName, Object... args){

    }

}
