package cel.compiler.visitors;

import cel.compiler.errors.ValueError;
import cel.parser.CELBaseVisitor;
import cel.parser.CELParser;

public class TimeSpanVisitor extends CELBaseVisitor<Long> {

    @Override
    public Long visitTime_span(CELParser.Time_spanContext ctx) {
        if (ctx.children.size() == 0) {
            throw new ValueError("TimeWindow must state a value", ctx);
        }
        long totalSeconds = 0;

        if (ctx.seconds() != null) {
            totalSeconds += Long.parseLong(ctx.seconds().number().getText());
        }

        if (ctx.minutes() != null) {
            totalSeconds += Long.parseLong(ctx.minutes().number().getText()) * 60;
        }

        if (ctx.hours() != null) {
            totalSeconds += Long.parseLong(ctx.hours().number().getText()) * 3600;
        }

        return totalSeconds;
    }
}
