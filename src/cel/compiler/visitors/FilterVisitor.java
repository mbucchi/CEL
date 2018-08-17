package cel.compiler.visitors;

import cel.compiler.errors.NameError;
import cel.event.Label;
import cel.event.errors.NoSuchLabelException;
import cel.filter.AndFilter;
import cel.filter.AtomicFilter;
import cel.filter.Filter;
import cel.filter.OrFilter;
import cel.parser.CELBaseVisitor;
import cel.parser.CELParser;
import cel.parser.utils.StringCleaner;
import cel.predicate.Predicate;

public class FilterVisitor extends CELBaseVisitor<Filter> {

    @Override
    public Filter visitPar_filter(CELParser.Par_filterContext ctx) {
        return ctx.filter().accept(this);
    }

    @Override
    public Filter visitAnd_filter(CELParser.And_filterContext ctx) {
        Filter left = ctx.filter(0).accept(this);
        Filter right = ctx.filter(1).accept(this);
        return new AndFilter(left, right);
    }

    @Override
    public Filter visitEvent_filter(CELParser.Event_filterContext ctx) {
        String labelName = StringCleaner.tryRemoveQuotes(ctx.event_name().getText());
        Label label;
        try {
            label = Label.get(labelName);
        } catch (NoSuchLabelException exc) {
            throw new NameError("event or label '" + labelName + "' was never declared", ctx);
        }
        Predicate predicate = ctx.bool_expr().accept(new PredicateVisitor(label));
        return new AtomicFilter(label, predicate);
    }

    @Override
    public Filter visitOr_filter(CELParser.Or_filterContext ctx) {
        Filter left = ctx.filter(0).accept(this);
        Filter right = ctx.filter(1).accept(this);
        return new OrFilter(left, right);
    }
}


