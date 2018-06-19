package cepl.compiler;

import cepl.parser.CEPLLexer;
import cepl.parser.CEPLParser;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

class BaseCompiler {

    CEPLParser parse(String cmd){
        CharStream charStream = CharStreams.fromString(cmd);
        CEPLLexer ceplLexer = new CEPLLexer(charStream);
        CommonTokenStream commonTokenStream = new CommonTokenStream(ceplLexer);
        CEPLParser ceplParser = new CEPLParser(commonTokenStream);

        return ceplParser;
    }
}
