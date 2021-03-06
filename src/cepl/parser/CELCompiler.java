package cepl.parser;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mdkt.compiler.InMemoryJavaCompiler;
import cepl.motor.*;

public class CELCompiler {

    private String fileName;
    private Parser parser;
    private ASTNode root;
	private Map<String, String> varRelationMap;
    private boolean hasErrors;

	private CELEngine engine;

	private Map<String, Set<String>> events;
	private Map<String, String[]> fieldNames;
	private Map<String, Class[]> fieldTypes;

	private int stateN;
	private int qInit;
	private boolean[] isFinal;

	private InMemoryJavaCompiler javac;


	public CELCompiler(){
		events = new HashMap<String, Set<String>>();
		fieldNames = new HashMap<String, String[]>();
		fieldTypes = new HashMap<String, Class[]>();
		javac = InMemoryJavaCompiler.newInstance();
	}

	public void addEventDefinition(String name, Object... fieldDefinitions) throws EventDefinitionError {
		if (events.containsKey(name)){
			throw new EventDefinitionError("An event of name \"" + name + "\" has already been declared.");
		}

		int i = 0;
		ArrayList<String> names = new ArrayList<String>();
		ArrayList<Class> types = new ArrayList<Class>();
		try {
			while(i < fieldDefinitions.length){
				String pname = (String)fieldDefinitions[i++];
				if (names.contains(name)){
					throw new EventDefinitionError("Defining \"" + pname + "\" twice on adittion of event \"" + name + "\"");
				}
				names.add(pname);
				types.add((Class)fieldDefinitions[i++]);
			}
			fieldNames.put(name, names.toArray(new String[0]));
			fieldTypes.put(name, types.toArray(new Class[0]));
		}
		catch (ArrayIndexOutOfBoundsException err){
			throw new EventDefinitionError("Missing type of field \"" + fieldDefinitions[i - 2] 
										 + "\" on adittion of event \"" + name + "\".");
		}
		catch (ClassCastException err) {
			if (--i % 2 == 0){
				// string expected
				throw new EventDefinitionError("Name of field on adittion of event \"" + name + "\" must be of type <String>.");
			}
			else {
				// class expected
				throw new EventDefinitionError("Type of field \"" + fieldDefinitions[--i] + "\" on adittion of event \"" 
											   + name + "\" must be of type <Class>.");				
			}
		}

		events.put(name, new HashSet<String>(names));

		for (Class cls : types){
			if (cls.isPrimitive()){
				throw new EventDefinitionError("Field types on adittion of events cannot be primitive types. " 
											   + "Use the wrapper class for type <" + cls.getName() + "> instead.");
			}
		}
	
	}

    public CELEngine compileQuery(String fileName) throws IOException, ParserException, WellformedException         /* temporal */, Exception {
		this.fileName = fileName;
		parser = new Parser(fileName, events);
		root = parser.parse();
		varRelationMap = parser.getVarRelations();
		root.lpNormalize();
		makeSourceCodes();
		compileSourceCodes();
		System.gc();		
		return engine;
	}

	private void makeSourceCodes() throws Exception {

		// Adding event types source codes
		for (String eventName: events.keySet()){
			String source = SourceCodeCreator.createEvent(eventName, fieldNames.get(eventName), fieldTypes.get(eventName));
			javac.addSource("cepl.motor." + eventName, source);
		}

		javac.addSource("cepl.motor.Engine", SourceCodeCreator.createEngine(
			new Automata(root, varRelationMap, events)
		));
	}

	private void compileSourceCodes() throws Exception {


		Map<String, Class<?>> binaries = javac.compileAll();
		for (String eventName: events.keySet()){
			Event.addClass(eventName, binaries.get("cepl.motor." + eventName));
		}
		
		this.engine = (CELEngine)binaries.get("cepl.motor.Engine").getConstructor().newInstance();
    }
	
    public CELEngine getEngine() throws Exception{
        return engine;
    }
}