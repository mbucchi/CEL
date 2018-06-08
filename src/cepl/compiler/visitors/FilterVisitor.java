package cepl.compiler.visitors;

import cepl.cea.utils.Label;
import cepl.filter.*;
import cepl.parser.CEPLBaseVisitor;
import cepl.parser.CEPLParser;

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
        Label label = Label.forName(ctx.event_name().getText());
        EventFilter eventFilter = ctx.bool_expr().accept(new BoolExprVisitor(label));

        return eventFilter;
    }

    @Override
    public PatternFilter visitOr_filter(CEPLParser.Or_filterContext ctx) {
        PatternFilter left = ctx.filter(0).accept(this);
        PatternFilter right = ctx.filter(1).accept(this);
        return new OrPatternFilter(left, right);
    }
}


