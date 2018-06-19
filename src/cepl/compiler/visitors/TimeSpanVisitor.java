package cepl.compiler.visitors;

import cepl.compiler.errors.ValueError;
import cepl.parser.CEPLBaseVisitor;
import cepl.parser.CEPLParser;

public class TimeSpanVisitor extends CEPLBaseVisitor<Long> {

    @Override
    public Long visitTime_span(CEPLParser.Time_spanContext ctx) {
        if (ctx.children.size() == 0) {
            throw new ValueError("TimeWindow must state a value");
        }
        long totalSeconds = 0;

        if (ctx.seconds() != null){
            totalSeconds += Long.parseLong(ctx.seconds().number().getText());
        }

        if (ctx.minutes() != null){
            totalSeconds += Long.parseLong(ctx.minutes().number().getText()) * 60;
        }

        if (ctx.hours() != null){
            totalSeconds += Long.parseLong(ctx.hours().number().getText()) * 3600;
        }

        return totalSeconds;
    }
}
