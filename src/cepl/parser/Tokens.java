package cepl.parser;

import java.util.regex.Pattern;
import java.io.EOFException;
import java.util.regex.Matcher;


enum TokenType {
    FILTER      ( "FILTER"  ),
    AS          ( "AS"      ),
    OR          ( "OR"      ),
    AND         ( "AND"     ),
    UNLESS      ( "UNLESS"  ),
    ALL         ( "ALL"     ),

    MAX         ( "MAX"     ),
    STRICT      ( "STRICT"  ),
    ANY         ( "ANY"     ),
    NXT         ( "NXT"     ),
    LAST        ( "LAST"    ),

    NOT         ( "not"         ),
    FILT_OP     ( "and|or"      ),
    PRED_OP     ( "<=|>=|<|>|=" ),

    PLUS        ( "\\+" ),
    MINUS       ( "\\-" ),
    MULT        ( "\\*" ),
    DIV         ( "/"   ),
    Lp          ( "\\(" ),
    Rp          ( "\\)" ),
    COLON       ( ";"   ),
    DOT         ( "\\." ),

    
    RELATION            ( "[A-Z][a-zA-Z0-9_]*"  ),
    WORD                ( "[a-z_][a-zA-Z0-9_]*"  ),

    NUMBER              ( "\\d+(\\.\\d+)?"  ),

    WS                  ( "[ \\t]+" ),
    NEWLINE             ( "(\\r?\\n|\\r)+"  ),

    
    // Non matching tokens
    STRING              ( "[~.]" ),
    EOF                 ( "[~.]" );
    

    private final Pattern regexp;

    TokenType(String regexp){
        this.regexp = Pattern.compile("^(" + regexp + ")");
    }

    Matcher getMatcher(String s){
        return regexp.matcher(s);
    }

}

class Token {
    public final TokenType type;
    public final String sequence;
    private final String line;
    private final int line_n;
    private final int col_n;
    private final String fileName;


    public Token(String fileName, TokenType type, String sequence, String line, int line_n, int col_n){
        this.type = type;
        this.sequence = sequence;
        this.line = line;
        this.line_n = line_n;
        this.col_n = ++col_n;
        this.fileName = fileName;
    }

    public int getLineNo(){
        return line_n;
    }
    
    public int getColNo(){
        return col_n;
    }

    public String getLine(){
        return line;
    }

    public String getFileName(){
        return fileName;
    }

    public void throwParseError(String msg) throws ParserException {
        throw new ParserException(
                "\nFile \"" + fileName + "\", line " + line_n + "\n" +
                "\t" + line + "\n" +
                String.format("\t%" + col_n + "s", "^\n") +
                msg
            );
    }

    public void throwParseError() throws ParserException {
        if (type == TokenType.EOF){
            throw new ParserException(
                "\nFile \"" + fileName + "\"\n" +
                "SyntaxError: unexpected EOF found"
            );
        }
        else{
            throwParseError("SyntaxError: unexpected symbol found");
        }
    }

    public void throwWellformedError() throws WellformedException {
        throw new WellformedException(
            "\nFile \"" + fileName + "\", line " + line_n + "\n" +
            "\t" + line + "\n" +
            String.format("\t%" + col_n + "s", "^\n") +
            "Wellformness Error: Variable '" + sequence + "' is mentioned on a FILTER clause but not bound within the same scope."
        );
    }
}