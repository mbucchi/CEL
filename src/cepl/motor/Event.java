package cepl.motor;

import java.util.Map;
import java.util.HashMap;


public abstract class Event {
    private static Map<String, Class> types = new HashMap<String, Class>();

    static public void addClass(String name, Class cls){
        types.put(name, cls);
    }
    
    static public Event newEvent(String name, Object... args) throws EventError, Exception, InstantiationException, IllegalAccessException {
        
        if (!types.containsKey(name))
        throw new EventError("Event type \"" + name + "\" was not declared");
        try{
            @SuppressWarnings("unchecked")
            Event e = (Event)types.get(name).getConstructor().newInstance();
            e.setValues(args);
            return e;
        }
        catch (IllegalAccessException err){
            throw new EventError("Wrong field values given for event of type \"" + name + "\"");
        }
    }

    abstract protected void setValues(Object... args);
    abstract public Object getValue(String field);
    abstract public Map<String, Class> getFieldDescriptions();
}

class EventError extends Exception {
    public EventError(String msg){
        super(msg);
    }
}