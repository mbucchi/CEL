package cepl.compiler;

import cepl.compiler.errors.UnknownStatementError;
import cepl.compiler.visitors.EventDeclarationVisitor;
import cepl.compiler.visitors.StreamDeclarationVisitor;
import cepl.parser.CEPLParser;
import org.antlr.v4.runtime.misc.ParseCancellationException;

public class DeclarationCompiler extends BaseCompiler {

    public void compile(String declaration) throws ParseCancellationException {
        CEPLParser ceplParser = parse(declaration);

        // a declaration can only be valid if it parses on this parser rule
        CEPLParser.Cel_declarationContext tree = ceplParser.cel_declaration();

        // check what kind of declaration it is and compile it
        if (tree.event_declaration() != null){
            compileEventDeclaration(tree.event_declaration());
        }
        else if (tree.stream_declaration() != null){
            compileStreamDeclaration(tree.stream_declaration());
        }
        else {
            // This means someone has modified the grammar to implement another kind of declaration.
            // This method should be modified to include that kind of declaration.
            throw new UnknownStatementError("No knowledge on how to compile given declaration statement.");
        }
    }

    private void compileEventDeclaration(CEPLParser.Event_declarationContext tree){
        tree.accept(new EventDeclarationVisitor());
    }

    private void compileStreamDeclaration(CEPLParser.Stream_declarationContext tree){
        tree.accept(new StreamDeclarationVisitor());
    }
}
