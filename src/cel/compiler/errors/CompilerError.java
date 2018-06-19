package cel.compiler.errors;

import cel.compiler.BaseCompiler;
import org.antlr.v4.runtime.ParserRuleContext;

public abstract class CompilerError extends Error {
    private String positionInfo;

    CompilerError(String msg, int lineNumber, int columnNumber) {
        super(msg);
        setPositionInfo(lineNumber, columnNumber);
    }

    CompilerError(String msg, ParserRuleContext context) {
        super(msg);
        if (context == null) {
            positionInfo = "";
        }
        else {
            int lineNumber = context.start.getLine();
            int columnNumber = context.start.getCharPositionInLine();
            setPositionInfo(lineNumber, columnNumber);
        }
    }

    private void setPositionInfo(int lineNumber, int columnNumber) {
        StringBuilder stringBuilder = new StringBuilder("line ")
                .append(lineNumber)
                .append(",\n  ")
                .append(BaseCompiler.getLatestInput(lineNumber))
                .append("\n  ");

        for (int c=0; c<columnNumber; c++){
            stringBuilder.append(' ');
        }
        stringBuilder.append("^\n");
        positionInfo = stringBuilder.toString();
    }

    @Override
    public String toString() {
        return positionInfo + this.getClass().getSimpleName() + ": " + getMessage();
    }
}
