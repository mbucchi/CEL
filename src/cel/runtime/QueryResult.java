package cel.runtime;

import cel.queryExecution.CeaExecutor;

import java.util.function.Consumer;

public class QueryResult {

    private Consumer<Object> callback;
    private CeaExecutor ceaExecutor;

    QueryResult(CeaExecutor ceaExecutor){
        this.ceaExecutor = ceaExecutor;
    }

    public void setMatchCallback(Consumer<Object> callback){

    }

    public void stop(){

    }

}
