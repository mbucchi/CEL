package cel.queryExecution;


import java.util.function.Consumer;

public abstract class CELExecutor {

    public abstract void newValue(Object event);

    public abstract void setMatchCallback(Consumer<Object> callback);
}
