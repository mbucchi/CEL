package cel.compiler.visitors;

import cel.event.Label;
import cel.event.errors.NoSuchLabelException;
import cel.compiler.errors.NameError;
import cel.filter.*;
import cel.parser.CEPLBaseVisitor;
import cel.parser.CEPLParser;

public class FilterVisitor extends CEPLBaseVisitor<PatternFilter> {

    @Override
    public PatternFilter visitPar_filter(CEPLParser.Par_filterContext ctx) {
        return ctx.filter().accept(this);
    }

    @Override
    public PatternFilter visitAnd_filter(CEPLParser.And_filterContext ctx) {
        PatternFilter left = ctx.filter(0).accept(this);
        PatternFilter right = ctx.filter(1).accept(this);
        return new AndPatternFilter(left, right);
    }

    @Override
    public PatternFilter visitEvent_filter(CEPLParser.Event_filterContext ctx) {
        String labelName = ctx.event_name().getText();
        try {
            Label label = Label.get(labelName);
            EventFilter eventFilter = ctx.bool_expr().accept(new BoolExprVisitor(label));
            return eventFilter;
        }
        catch (NoSuchLabelException exc){
            throw new NameError("event or label '" + labelName + "' was never declared", ctx);
        }
    }

    @Override
    public PatternFilter visitOr_filter(CEPLParser.Or_filterContext ctx) {
        PatternFilter left = ctx.filter(0).accept(this);
        PatternFilter right = ctx.filter(1).accept(this);
        return new OrPatternFilter(left, right);
    }
}


