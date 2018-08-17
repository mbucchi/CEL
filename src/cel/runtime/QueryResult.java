package cel.runtime;

import cel.queryExecution.CELExecutor;

import java.util.function.Consumer;

public class QueryResult {

    private Consumer<Object> callback;
    private CELExecutor celExecutor;

    QueryResult(){}

    QueryResult(CELExecutor celExecutor){
        this.celExecutor = celExecutor;
    }

    public void setMatchCallback(Consumer<Object> callback) {

    }

    public void stop() {

    }

}
