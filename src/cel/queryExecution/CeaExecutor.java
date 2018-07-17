package cel.queryExecution;

import cel.event.Event;

public abstract class CeaExecutor {

    abstract void newValue(Event event);
}
