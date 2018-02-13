package cepl.parser;

import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;


class Tokenizer {

    private ArrayList<Token> tokens;
    private String fileName;

    private FileReader file;
    private BufferedReader fileStream;

    public Tokenizer(String fileName) throws ParserException, IOException {
        tokens = new ArrayList<Token>();
        
        this.fileName = fileName;
        file = new FileReader(fileName);
        fileStream = new BufferedReader(file);
        try {
            tokenize();
        }
        finally {
            fileStream.close();
            file.close();
        }
    }

    private void tokenize() throws ParserException, IOException{
        String line;
        String s;
        int line_n = 0;
        int col_n = 1;

        while ((line = fileStream.readLine()) != null){
            line_n++;
            s = line;
            col_n = 1;
            while (!s.equals("")){
                boolean match = false;
                if (s.charAt(0) == '\'' || s.charAt(0) == '"'){
                    char delim = s.charAt(0);
                    for (int idx = 1; idx < s.length(); idx++){
                        if (s.charAt(idx) == delim && s.charAt(idx-1) != '\\'){
                            match = true;
                            tokens.add(new Token(fileName, TokenType.STRING, "\"" + s.substring(1, idx) +  "\"", line, line_n, col_n));
                            col_n += idx+1;
                            s = s.substring(idx+1, s.length());
                            break;
                        }
                    }
                    if (!match) {
                        throw new ParserException(
                            "File \"" + fileName + "\", line " + line_n + "\n" +
                            "\t" + line + "\n" +
                            String.format("\t %" + ( col_n + s.length() ) + "s", "^\n") +
                            "SyntaxError: EOL while scanning string literal"
                        );
                    }
                }
                else {
                    for (TokenType t_type: TokenType.values()) {
                        Matcher m = t_type.getMatcher(s);
                        if (m.find()){
                            match = true;
                            
                            String tok = m.group();
                            col_n += tok.length();
                            s = m.replaceFirst("");
    
                            // consume white space
                            if (t_type == TokenType.WS) {
                                break;
                            }
    
                            tokens.add(new Token(fileName, t_type, tok, line, line_n, col_n - tok.length()));
    
                            break;
                        }
                    }
                    if (!match) {
                        throw new ParserException(
                            "File \"" + fileName + "\", line " + line_n + "\n" +
                            "\t" + line + "\n" +
                            String.format("\t %" + col_n + "s", "^\n") +
                            "SyntaxError: invalid syntax"
                        );
                    }
                }
            }
        }
        tokens.add(new Token(fileName, TokenType.EOF, "EOF", null, 0, 0));
        
    }

    public ArrayList<Token> getTokens(){
        return tokens;
    }

}

class ParserException extends RuntimeException {
    public ParserException(String msg){
        super(msg);
    }
}