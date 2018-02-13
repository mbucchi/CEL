# _CEL_
_CEL_, a framework for Complex Event Processing implemented in Java.

1. [Building](#building)
    - [Requirements](#requirements)
    - [Process](#process)
2. [Using _CEL_ on your program](#using-cel-on-your-program)
    - [Initializing a new engine](#initializing-a-new-engine)
    - [Declaring event types](#declaring-event-types)
    - [Compiling your query](#compiling-your-query)
    - [Declaring a result callback function](#declaring-a-result-callback-function)
    - [Feeding events](#feeding-events)
    - [Consuming results](#consuming-results)
3. [Compiling a program that uses _CEL_ dependencies](#compiling-a-program-that-uses-cel-dependencies)
4. [Creating _CEL_ queries](#creating-cel-queries)
    - [Assignment](#assignment)
    - [Operators](#operators)
        - [Concatenation](#concatenation)
        - [Disjunction](#disjunction-or)
        - [Kleene Closure](#kleene-closure)
    - [Filters](#filters)
        - [Operators](#filter-operators)
        - [Scope](#filter-scope)
    - [Selection Strategies](#selection-strategies)
        - [ANY](#any-default)
        - [NXT](#nxt)
        - [LAST](#last)
        - [STRICT](#strict)
        - [MAX](#max)
    - [Consumption Policies](#consumption-policies)
5. [References](#references)

## Building
### Requirements
As of now, the only requirement is having the Java SDK 9

### Process
Because no special requirements are needed, there is no specific and correct way to build. Just make sure to compile together all `.java` files within the `src` and `lib` folders. Once compiled, be sure to include the folder containing all the `.class` files into the classpath.

#### Example
1. Be sure the `CEL` repository is your `pwd`.
2. Go into the `src` directory. 

    `$ cd src`
3. Compile all the `.java` files in the `src` and `lib` folders into the `build` folder. 

     `$ javac cepl/motor/*.java cepl/parser/*.java ../lib/org/mdkt/compiler/*.java -d ../build`
4. Go into de `build` folder. 

     `$ cd ../build`
5. Build a jar that contains all the `.class` files. 

     `$ jar cvf CEL.jar *`

      Be sure that the build folder only contains the files generated in step 3 or this step could fail.


## Using _CEL_ on your program

In order to use _CEL_ in your program, be sure to import `cepl.motor` and `cepl.parser`. To see a full example of a simple program that initialices the _CEL_ engine, take a look [here](https://github.com/mbucchi/CEL/blob/master/test/Main.java).

### Initializing a new engine
First of all, you must initialize a compiler instance. This object will allow you to declare the nature of the stream and compile your query.
```Java
CELCompiler compiler = new CELCompiler();
```
### Declaring event types
Before compiling a _CEL_ query, the stream nature must be declared to the compiler. This allows for the compiler to check the query makes sense.

Besides that, the `cepl` package will create new classes for each of the events you declare. This will allow for you to easily get information from possible results.

In order to declare a new event, simply call the \
`void addEventDefinition(String EventName [, String fieldName, Class<?> fieldType [...]])`\
method on your compiler instance. This method recieves the event name together with an arbitrary number of property definitions. Each property is defined by both a name and a type. The types **cannot** be Java primitive types, so be sure to use wrappers should they be necesary.

For example, all of the following are valid calls which would allow for the use of all those event types in your queries.
```Java
compiler.addEventDefinition("Temperature", "celcius", Integer.class, "sensor_name", String.class);
compiler.addEventDefinition("Humidity", "relative", Double.class);

compiler.addEventDefinition("A");
compiler.addEventDefinition("B");
compiler.addEventDefinition("C");
```

Internally, each call will also generate an internal class that exposes the following methods: (the following example is for the first call shown above)
```Java
public class Temperature extends cepl.motor.Event {
    /* 
    Returns the value for the given field name. If such field does not exist, it throws IllegalAccessError.
    For this specific example, valid field names are "celcius" and "sensor_name".
    */
    public Object getValue(String field) throws IllegalAccessError;

    /*
    Returns a map that describes the fields on this specific Event type.
    For this specific example, this call would return the following map:
        {
            "celcius"     : Integer.class,
            "sensor_name" : Double.class
        }
    */
    public Map<String, Class> getFieldDescriptions();

    /*
    Allows for pretty printing events without the need to analyze the instance.
    For this specific example, the returned value would be:

        "Temperature(celcius=<value>, sensor_name=<value>)"
    */
    public String toString();
}
```


### Compiling your query

Once your events have been declared, you can now compile a _CEL_ query over those event types. In order to do so, simply call the `CELEngine compileQuery(String fileName)` method on your compiler instance.

```Java
CELEngine engine = compiler.compileQuery("filename.cel");
```
Now that you have your engine, you can decide your consumption policy by using the `void setDiscardPartials(boolean value)` method on your engine. You can read more about consumption policies [here](#consumption-policies). The default value is `true`, so would you like to change it to false, simply call
```Java
engine.setDiscardPartials(false);
```
### Declaring a result callback function
Now that your engine before feeding it the stream events, it is helpful to give the engine a callback function that will recieve results upon matching. For this, you can use the\
`void setMatchCallback(Consumer<cepl.motor.MatchGrouping> callback)`\
method on your engine instance.

Let's say that somewhere on your program you have this method declared (more info on result consuming [here](#consuming-results)):
```Java
class MyResultConsumerClass {
    public void matchTriggered(MatchGrouping matches){
        ...
    }
}
```

Which is then instanced into an object called `myResultConsumer`, you would then have to do:

```Java
engine.setMatchCallback(myResultConsumer::matchTriggered);
```

And voila! Your engine is ready to recieve events and hand you your results!

### Feeding events 

In order to give freedom to users, the engine completely ignores the nature of the stream. The understanding and processing of the stream is left completely up to the user defined program. Therefore, the engine only provides an interface for recieving instantiated events (the ones declared previously to the compiler).

First of all, you must create `Event` instances, and for this, `CEL` provides the following static method within the `cepl.motor.Event` class:

`Event newEvent(String eventName [, Object fieldValue1 [...]])` \

The field values should match the property descriptions given upon the declaration of the `"eventName"` event. For example. if we consider the `Temperature` event declared in the previous section, the call would look like this:


```Java
Event e = Event.newEvent("Temperature", 123, "s123");
```

Now that you have an event, you simply feed it to the machine through the `void newValue(Event e)` method, as follows:

```Java
engine.newValue(e);
```
If such event were to trigger a result, then the engine would call the declared callback function. If such function was not declared, **no** notice will be given to the user.

### Consuming results
In order to consume results you must understand the `MatchGrouping`, `Match` and `Event` classes defined within the `cepl.motor` package.

`MatchGrouping` objects are used to hand you a group of matches (quite a surprise, right?) upon a query match on you engine. The class exposes the following methods:
```Java
public class MatchGrouping implements Iterable<cepl.motor.Match> {
    /* Returns the amount of matches that form part on this grouping */
    public long size();
    
    /* Returns the specific event that caused the query completion */
    public cepl.motor.Event lastEvent();
    
    /* Returns a match iterator that allows the user to get the matches that form part of this grouping */
    public Iterator<cepl.motor.Match> iterator();
}
```

`Match` objects represent a collection of events that fit the _CEL_ query which is compiled on the engine. The class exposes the following methods:

```Java
public class Match implements Iterable<cepl.motor.Event> {
    /* Returns the length of this match. (i.e. the amount of Events that form part of it) */
    public int size();

    /* Returns an event iterator that allows the user to get the events that form part of the match */
    public Iterator<cepl.motor.Event> iterator();
}
```

`Event` objects represent each and every event on the stream as declared by you to the compiler. An example can be seen [here](#declaring-events). The class exposes the following methods (besides a few others that are not useful for this section):

```Java
public abstract class Event {
    abstract public Object getValue(String field) throws IllegalAccessError;
    abstract public Map<String, Class> getFieldDescriptions();
}
```

#### Code example
Now that the underlying objects of a result are no mistery, the following example will make sense:

```Java
public void matchTriggered(MatchGrouping mg){
        System.out.println("\n[" + mg.lastEvent() + "] triggered a total of " + mg.size() + " outputs.");

        for (Match m : mg){
            System.out.print("\t");        
            for (Event e: m){
                System.out.print( e + " ");
            }
            System.out.println("");
        }
    }
```

Which could print something like this upon the query `Temperature AS t + ; Humidity AS h`

```bash

[Humidity(relative=0.70)] triggered a total of 3 outputs.
    Temperature(celcius=20, sensor_name=s123) Temperature(celcius=21, sensor_name=s123) Humidity(relative=0.70)
    Temperature(celcius=21, sensor_name=s123) Humidity(relative=0.70)
    Temperature(celcius=20, sensor_name=s123) Humidity(relative=0.70)
```

## Compiling a program that uses _CEL_ dependencies
Once you've written a Java program that uses _CEL_, all you need to do is compile it adding the files generated in the previous step to the classpath.

Continuing with the example set before, one option is compiling referencing the `jar` in the class path option:

`$ javac -cp CEL.jar <all-your-program-java-files>`

Another option is adding the `jar` built on the previous step to the `CLASSPATH` variable. If doing this just after doing the building of the `jar` file on the building steps example, you could just run (while still at the `build` directory)

`$ export CLASSPATH=$(pwd)/CEL.jar:$CLASSPATH`

which would allow you to compile your program with no need to reference `CEL.jar` in the `cp` argument.


## Creating _CEL_ queries

Before creating a _CEL_ query, you must now the nature of the stream that will be fed into the _CEL_ engine. All you need to know are the properties and names of the events that form part of the stream. These should be the same as the events you declared to the engine instance (see previous section).

### Assignment
The most important part within a _CEL_ queries are assignments. These define what events will be selected from the stream to form part of a complex event. The syntax is pretty straight forward:

`<event> AS <variable>`

 Say you want to capture a `Temperature` event that occurs on the stream, then your query will look like:

 `Temperature AS t`

Note that the only restrictions on event and variable naming are that **event names** must start with an **uppercase** letter while **variable names** must start with a **lowercase letter**. The use of variable names will become apparent when you learn about _CEL_ filters.

### Operators

In order to make more interesting complex event queries, _CEL_ offers you three operators to use together with assignments.

#### Concatenation ( `;` )

The concatenation operator allows to ask for two different queries that should occur in succession. The syntax is as follows:

`<query a> ; <query b>`

where _query a_ and _query b_ are both normal _CEL_ queries that can use any of the operators, filters and assignments. 

Say you want to capture a `Temperature` followed by a `Humidity` event, that would be done by concatenating two assignment statements:

`Temperature AS t ; Humidity AS h`

#### Disjunction ( `OR` )

This operator allows you to ask for _any_ of two queries to form part of your complex event. The syntax is as follows:

`<query a> OR <query b>`

where _query a_ and _query b_ are both normal _CEL_ queries that can use any of the operators, filters and assignments. This query would create a match in a stream that fits either of the queries (or both).

Say you want to capture two kinds of events: `Temperature` and `Humidity`, with no necesary relation between both. Then instead of running two different engines over the same stream, you could run both queries in the same engine:

`Temperature AS t OR Humidity AS h`

#### Kleene Closure ( `+` )

The Kleene closure operator comes in handy when you want to capture multiple succesful matches of a given query, or in other words concatenate a query with itself an arbitrary number of times. The syntax is as follows:

`<query> +`

where _query_ is a normal _CEL_ query that can use any of the operators, filters and assignments. Note that this kind of query matches when your inner query is triggered *at least* for the first time. That is, multiple means one or more time. 

Say you want to capture multiple `Temperature` events until a `Humidity` event occurs, then your query would look as follows:

`Temperature AS t + ; Humidity as h`

### Filters

Filters allow to restrict the possible matches of queries made with the operators shown above. Note that unlike previous operators, this does not allow to create new kind of queries, but rather only restrict the outputs of unfiltered _CEL_ queries. 

Currently _CEL_ only supports **unary** filters because of efficiency. This means that it is not possible to filter over a _CEL_ query by performing comparisons between two or more assigned variables. Filters supported are all those that compare an event property to a static value. The syntax is as follows:

`<query> FILTER <variable>.<property> <operator> <value>`

where:
- _query_ is a normal _CEL_ query that can use operators, filters and assignments
- _variable_ is the name of a variable bound in an assignment within the same scope as the filter (more info about scopes below)
- _property_ is a property present in the event bound to the variable. This property must have been declared to the engine as shown in the previous section.
- _operator_ is any comparison operator (`<`, `<=`, `=`, `>=`, `>`)
- _value_ is any static value defined at compile time on the query.

#### Filter operators
More complex filter formulas can be created with `and` & `or` logical operators. Note that this does not add any extra complexity as this just allows avoiding nesting multiple filters or disjuncting the same query with different filters.

Besides that, the `not` logical operator allows to create more semantically readable filters, and introduces the use of _inequality_.

a few examples:

`Temperature AS t FILTER t.celsius > 100`\
`Humidity AS h FILTER h.relative > 0.5 and h.relative < 0.7`\
`( Temperature AS t1 ; Temperature AS t2 ) FILTER ( t1.celsius = 100 or not (t2.celsius = 100) )`

#### Filter scope
In order for filters work, the variables named within the filter function must be bound within the same scope as the `FILTER` statement.

This does not mean that all variables must be bound in the query that precedes the filter operator. In order for a variable to be correctly defined within the scope of a filter it needs to fulfill the following conditions:

- A variable `x` is defined at a certain level if there exists a query at the same, or higher level in which variable `x` is bound.
- To be bound is defined recursively as follows:
    - `x` is bound on `<REL> AS <var>` iff `<var> = x`
    - `x` is bound on `<Q1> OR <Q2>` iff `x` is bound in both `<Q1>` and `<Q2>`
    - `x` is bound on `<Q1> ; <Q2>` iff `x` is bound in either `<Q1>` or `<Q2>`
    - `x` is bound on `<Q1> FILTER <filter>` iff `x` is bound in `<Q1>`
    - no variable is bound in `<Q> +`

In simple words, you can filter over any variable on your query, at any level **unless** the target variable is defined within a kleene closure. In that case, any filter over such variable must be also defined within that kleene closure.


### Selection Strategies
Selection strategies allow for specifying what results should be handed back to you. Many times you wouldn't want to recieve absolutely all results, but maybe just the latest or the ones that encompass the most information possible. For that, CEL defines five different selection strategies (as of right now). 

Note that the first four selection strategies have exactly the same complexity, therefore running times of the engine should stay similar regardless of the strategy. The final selection strategy (`MAX`) has a cuadratic blowup in runtime as an upper bound, but in practice the runtime will stay similar.

#### ANY (default)
This is basically the lack of a selection strategy. A query executed with the `ANY` semantic will return _all_ posible matching complex events, which is most of the time (depending on the complexity of the query) an exponential number of results. This is the default selection strategy, so to use this semantic, simply just execute a plain query, or add the `ANY` operator around it:

`ANY ( <CEL query> )` or just `<CEL query>`

#### NXT
This selection strategy will return upon a match, only the _earliest_ complex event. This is well defined through a total ordering as follows: given two possible matches `M1` and `M2` we say that `M1` is a better `NXT` result than `M2` iff the earliest event that occurs in only one of the matches, is part of `M1`. If no such event exists, the `M1` is as good as `M2` because they both include exactly the same events. To use this selection strategy simply wrap your query with the `NXT` operator:

`NXT ( <CEL query> )`

#### LAST

Almost identical to `NXT` but in the other direction. This selection strategy will return upon a match, only the _latest_ complex event. The total ordering is defined as follows: given two possible matches `M1` and `M2` we say that `M1` is a better `LAST` than result `M2` iff the latest event that occurs in only one of the matches, is part of `M1`. To use this selection strategy simply wrap your query with the `LAST` operator:

`LAST ( <CEL query> )`

#### STRICT
This selection strategy will only return matches that occur in strict continuity within the stream. Unlike all previous strategies, the engine won't ignore any events on the stream and the query must match exactly a run of events in the stream. 

For example the query `A AS a; B AS b` would match both of the following streams: `A B C` and `A C B`, because in the second case, the engine will completely ignore the `C` event. This would not be the case for `STRICT ( A AS a; B AS b )` which would only match the first stream.

#### MAX
This selection strategy will return all possible maximal matches. That is, given any two pair of matches `M1` and `M2` neither `M1` will be a subset of `M2` nor `M2` a substet of `M1`. This is incredibly useful when used in conjunction with the kleene closure operator, as you will just get the most informative results without losing any information and avoiding an exponential blowup in the number of results. To use this selection strategy simply wrap your query with the `MAX` operator:

`MAX ( <CEL query> )`


### Consumption Policies
As of right now, on a _CEL_ engine you can choose whether upon a match you would like to discard all partial matches and restart or continue with partial matches. 

For example the query `A AS a ; B AS b` over the stream `A B C B` would generate only one match if you were to execute with discarding partial matches. That is because, upon the matching on the first `B`, the engine would forget about the first `A`. On the other hand, if not discarding partials, it would generate two matches, one with the first `B` and another one for the second.

## References