package cel.compiler;

import cel.compiler.errors.UnknownStatementError;
import cel.compiler.visitors.EventDeclarationVisitor;
import cel.compiler.visitors.StreamDeclarationVisitor;
import cel.parser.CEPLParser;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class DeclarationCompiler extends BaseCompiler<Void> {


    @Override
    public Void compile(String declaration) throws ParseCancellationException {
        CEPLParser ceplParser = parse(declaration);

        // a declaration can only be valid if it parses on this parser rule
        CEPLParser.Cel_declarationContext ctx = ceplParser.cel_declaration();


        return compileContext(ctx);
    }

    @Override
    Void compileContext(ParserRuleContext ctx) {
        if (!(ctx instanceof CEPLParser.Cel_declarationContext)){
            throw new Error("FATAL ERROR!");
        }

        CEPLParser.Cel_declarationContext declarationContext = (CEPLParser.Cel_declarationContext) ctx;

        // check what kind of declaration it is and compile it
        if (declarationContext.event_declaration() != null){
            compileEventDeclaration(declarationContext.event_declaration());
        }
        else if (declarationContext.stream_declaration() != null){
            compileStreamDeclaration(declarationContext.stream_declaration());
        }
        else {
            // This means someone has modified the grammar to implement another kind of declaration.
            // This method should be modified to include that kind of declaration.
            throw new UnknownStatementError("No knowledge on how to compile given declaration statement", ctx);
        }

        return null;
    }

    private void compileEventDeclaration(CEPLParser.Event_declarationContext tree){
        tree.accept(new EventDeclarationVisitor());
    }

    private void compileStreamDeclaration(CEPLParser.Stream_declarationContext tree){
        tree.accept(new StreamDeclarationVisitor());
    }
}
