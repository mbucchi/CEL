package cel.compiler.visitors;

import cel.compiler.errors.NameError;
import cel.event.Label;
import cel.event.errors.NoSuchLabelException;
import cel.filter.AndPatternFilter;
import cel.filter.EventFilter;
import cel.filter.OrPatternFilter;
import cel.filter.PatternFilter;
import cel.parser.CELBaseVisitor;
import cel.parser.CELParser;
import cel.parser.utils.StringCleaner;

public class FilterVisitor extends CELBaseVisitor<PatternFilter> {

    @Override
    public PatternFilter visitPar_filter(CELParser.Par_filterContext ctx) {
        return ctx.filter().accept(this);
    }

    @Override
    public PatternFilter visitAnd_filter(CELParser.And_filterContext ctx) {
        PatternFilter left = ctx.filter(0).accept(this);
        PatternFilter right = ctx.filter(1).accept(this);
        return new AndPatternFilter(left, right);
    }

    @Override
    public PatternFilter visitEvent_filter(CELParser.Event_filterContext ctx) {
        String labelName = StringCleaner.tryRemoveQuotes(ctx.event_name().getText());
        EventFilter eventFilter;
        try {
            Label label = Label.get(labelName);
            eventFilter = ctx.bool_expr().accept(new BoolExprVisitor(label));
        } catch (NoSuchLabelException exc) {
            throw new NameError("event or label '" + labelName + "' was never declared", ctx);
        }
        return eventFilter;
    }

    @Override
    public PatternFilter visitOr_filter(CELParser.Or_filterContext ctx) {
        PatternFilter left = ctx.filter(0).accept(this);
        PatternFilter right = ctx.filter(1).accept(this);
        return new OrPatternFilter(left, right);
    }
}


