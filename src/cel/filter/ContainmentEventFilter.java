package cel.filter;

import cel.cea.CEA;
import cel.event.Label;
import cel.values.Attribute;
import cel.values.Literal;
import cel.values.ValueType;

import java.util.ArrayList;
import java.util.Collection;

public class ContainmentEventFilter extends ComplexEventFilter {

    private Collection<Literal> literalCollection;

    private ContainmentEventFilter(Label label,
                                  Attribute attribute,
                                  ValueType valueType,
                                  LogicalOperation logicalOperation){

        super(label, attribute, valueType, logicalOperation);
        if (logicalOperation != LogicalOperation.IN && logicalOperation != LogicalOperation.NOT_IN){
            throw new Error("ContainmentEventFilter can only accept IN and NOT_IT " +
                    "logical operations, not " + logicalOperation.name());
        }
        literalCollection = new ArrayList<>();
    }

    public ContainmentEventFilter(Label label,
                                  Attribute attribute,
                                  ValueType valueType,
                                  LogicalOperation logicalOperation,
                                  Collection<Literal> literalCollection){

        this(label, attribute, valueType, logicalOperation);
        for (Literal literal : literalCollection){
            addLiteral(literal);
        }
    }

    private void addLiteral(Literal literal){
        if (!literal.interoperableWith(valueType)){
            throw new Error("Wrong type of values");
        }
        literalCollection.add(literal);
    }

    @Override
    public EventFilter translateToEventFilter() {
        LogicalOperation equalityOperator;
        switch (logicalOperation){
            case IN:
                equalityOperator = LogicalOperation.EQUALS;
                break;
            case NOT_IN:
                equalityOperator = LogicalOperation.NOT_EQUALS;
                break;
            default:
                throw new Error("Transformation not implemented for " + logicalOperation.name() +
                        " operation in Containment filters");
        }

        Collection<EventFilter> eventFilterCollection = new ArrayList<>();
        for (Literal literal : literalCollection) {
            eventFilterCollection.add(new EqualityEventFilter(label, attribute, equalityOperator, literal));
        }

        return new AndEventFilter(label, eventFilterCollection);
    }

    @Override
    public CEA applyToCEA(CEA cea) {
        translateToEventFilter().applyToCEA(cea);
        return cea;
    }
}
