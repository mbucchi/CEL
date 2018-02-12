# _CEL_
_CEL_, a framework for Complex Event Processing.

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


## Compiling a program that uses _CEL_ dependencies
Once you've written a Java program that uses _CEL_, all you need to do is compile it adding the files generated in the previous step to the classpath.

Continuing with the example set before, one option is compiling referencing the `jar` in the class path option:

`$ javac -cp CEL.jar <all-your-program-java-files>`

Another option is adding the `jar` built on the previous step to the `CLASSPATH` variable. If doing this just after doing the building of the `jar` file on the building steps example, you could just run (while still at the `build` directory)

`$ export CLASSPATH=$(pwd)/CEL.jar:$CLASSPATH`

which would allow you to compile your program with no need to reference `CEL.jar` in the `cp` argument.


## Using _CEL_ on your program

### Initializing a new engine
```Java
CELCompiler compiler = new CELCompiler();
```
### Declaring event types
```Java
compiler.addEventDefinition("Temperature", "celcius", int.class, "sensor_name", String.class);
compiler.addEventDefinition("Humidity", "relative", double.class);
```

```Java
compiler.addEventDefinition("A");
compiler.addEventDefinition("B");
compiler.addEventDefinition("C");
```


### Compiling your query

```Java
compiler.compileQuery("filename.cel");
CELEngine matcher = compiler.getEngine();
matcher.setDiscardPartials(true);
```
### Declaring a result callback function
```Java
Class CELExample {
    static void matchTriggered(MatchGrouping matches){
        ...
    }
}
```

```Java
matcher.setMatchCallback(CELExample::matchTriggered);
```
### Consuming results
```Java
static void matchTriggered(MatchGrouping mg){
        System.out.println("\n[" + mg.lastEvent() + "] triggered a total of " + mg.size() + " outputs.");

        for (Match m : mg){
            System.out.print("\t");        
            for (Event e: m){
                System.out.print( e + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }
```
```bash
[Humidity(relative=0.70)] triggered a total of 3 outputs.
    Temperature(celcius=20) Temperature(celcius=21) Humidity(relative=0.70)
    Temperature(celcius=21) Humidity(relative=0.70)
    Temperature(celcius=20) Humidity(relative=0.70)
```


## Creating _CEL_ queries

Before creating a _CEL_ query, you must now the nature of the stream that will be fed into the _CEL_ engine. All you need to know are the properties and names of the events that form part of the stream. These should be the same as the events you declared to the engine instance (see previous section).

### Assignment
The most important part within a _CEL_ query are the assignments. This defines what events will be selected from the stream to form part of a complex event. The syntax is pretty straight forward:

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

This operator allows for you to ask for _any_ of two queries to form part of your complex event. The syntax is as follows:

`<query a> OR <query b>`

where _query a_ and _query b_ are both normal _CEL_ queries that can use any of the operators, filters and assignments. This query would create a match in a stream that fits either of the queries (or both).

Say you want to capture two kinds of events: `Temperature` and `Humidity`, with no necesary relation between both. Then instead of running two different engines over the same stream, you could run both queries in the same engine:

`Temperature AS t OR Humidity AS h`

#### Kleene Closure ( `+` )

The Kleene closure operator comes in handy when you want to capture multiple succesful matches of a given query, or in other words concatenate a query with itself an arbitrary number of times. The syntax is as follows:

`<query> +`

where _query_ is a normal _CEL_ queries that can use any of the operators, filters and assignments. Note that this kind of query matches when your inner query is triggered *at least* for the first time. That is, multiple means one or more time. 

Say you want to capture multiple `Temperature` events until a `Humidity` event occurs, then your query would look as follows:

`Temperature AS t + ; Humidity as h`

### Filters

Filters allow to restrict possible matches to queries made with the operators shown above. Note that unlike previous operators, this does not allow to create new kind of queries, but rather only restrict the outputs of unfiltered _CEL_ queries. 

Currently _CEL_ only supports **unary** filters because of efficiency. This means it is not possible to filter over a _CEL_ query by performing comparisons between two or more assigned variables. Filters supported are all those that compare an event property to a static value. The syntax is as follows:

`<query> FILTER <variable>.<property> <operator> <value>`

where:
- _query_ is a normal _CEL_ queries that can use operators, filters and assignments
- _variable_ is the name of a variable bound in an assignment within the same scope as the filter (more info about scopes below)
- _property_ is a property present in the event bound to the variable. This property must have been declared to the engine as shown in the previous section.
- _operator_ is any comparison operator (`<`, `<=`, `=`, `>=`, `>`)
- _value_ is any static value defined at compile time on the query.

#### Filter operators
More complex filter formulas can be created with `and` & `or` logical operators. Note that this does not add any extra complexity as this just allows avoiding nesting multiple filters or disjuncting the same query with different filters.

Besides that, the `not` logical operator is also allowed to create more semantically readable filters, and for allowing the use of _inequality_.

a few examples:

`Temperature as t FILTER t.celsius > 100`\
`Humidity as h FILTER h.relative > 0.5 and h.relative < 0.7`\
`Temperature t1 ; Temperature t2 FILTER t1.celsius = 100 or not (t2.celsius = 100)`

#### Filter scope
In order for filters work, the variables named within the filter function must be bound within the same scope as the `FILTER` statement.

This does not mean that all variables must be bound in the query that precedes the filter operator. In order for a variable to be correctly defined within the scope of a filter it needs to fulfill the following conditions:

- A variable `x` is defined at a certain level if there exists a query at the same, or higher level in which variable `x` is bound.
- To be bound is defined recursively as follows:
    - `x` is bound on `<REL> AS <var>` iff `<var> = x`
    - `x` is bound on `<Q1> OR <Q2>` if `x` is bound in both `<Q1>` and `<Q2>`
    - `x` is bound on `<Q1> ; <Q2>` if `x` is bound in either `<Q1>` or `<Q2>`
    - `x` is bound on `<Q1> FILTER <filter>` if `x` is bound in `<Q1>`
    - no variable is bound in `<Q> +`

In simple words, you can filter over any variable on your query, at any level **unless** the target variable is defined within a kleene closure. In that case, any filter over such variable must be also within that kleene closure.


### Selection Strategies
Selection strategies allow for specifying what results should be handed back to you. Many times you wouldn't want to recieve absolutely all results, but maybe just the latest or the one that encompasses most information possible. For that, CEL defines five different selection strategies (as of right now).

#### ANY (default)
This is basically the lack of a selection strategy. A query executed with the `ANY` semantic will return _all_ posible matching complex events, which is most of the time (depending on the complexity of the query) and exponential number of results. This is the default selection strategy, so to use this semantic, simply just execute a plain query, or add the `ANY` operator around it:

`ANY ( <CEL query> )` or just `<CEL query>`

#### NXT
This selection strategy will return upon a match, only the _earliest_ complex event. This is well defined through a total ordering as follows: given two possible matches `M1` and `M2` we say that `M1` is a better `NXT` result than `M2` iff the earliest event that occurs in only one of the matches, is part of `M1`. If no such event exists, the `M1` is as good as `M2` because they both include exactly the same events. To use this selection strategy simply wrap your query with the `NXT` operator:

`NXT ( <CEL query> )`

#### LAST

Almost identical to `NXT` but in the other direction. This selection strategy will return upon a match, only the _latest_ complex event. The total ordering is defined as follows: given two possible matches `M1` and `M2` we say that `M1` is a better `LAST` than result `M2` iff the latest event that occurs in only one of the matches, is part of `M1`. To use this selection strategy simply wrap your query with the `LAST` operator:

`LAST ( <CEL query> )`

#### MAX
This selection strategy will return all possible maximal matches. That is, given any two pair of matches `M1` and `M2` neither `M1` will be a subset of `M2` nor `M2` a substet of `M1`. This is incredibly useful when used in conjunction with the kleene closure operator, as you will just get the most informative results without losing any information and avoiding an exponential blowup in the number of results. To use this selection strategy simply wrap your query with the `MAX` operator:

`MAX ( <CEL query> )`

#### STRICT
This selection strategy will only return matches that occur in strict continuity within the stream. Unlike all previous strategies, the engine won't ignore any events on the stream and the query must match exactly a run of events in the stream. 

For example the query `A AS a; B AS b` would match both of the following streams: `A B C` and `A C B`, because in the second case, the engine will completely ignore the `C` event. This would not be the case for `STRICT ( A AS a; B AS b )` which would only match the first stream.

### Consumption Policies
As of right now, on a _CEL_ engine you can choose whether upon a match you would like to discard all partial matches and restart or continue with partial matches. 

For example the query `A AS a ; B AS b` over the stream `A B C B` would generate only one match if you were to execute with discarding partial matches. That is because, upon the matching on the first `B`, the engine would forget about the first `A`. On the other hand, if not discarding partials, it would generate two matches, one with the first `B` and another one for the second.