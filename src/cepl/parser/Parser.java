package cepl.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

class Parser {

    private String fileName;
    private ArrayList<Token> tokens;
    private int c_tok;
    private Token lookahead;
    private Set<String> relations;
    private Map<String, String> varRelations;

    public Parser(String fileName, Set<String> definedRelations) throws IOException, ParserException {
        relations = definedRelations;
        this.fileName = fileName;
        Tokenizer tokenizer = new Tokenizer(fileName);
        tokens = tokenizer.getTokens();
        varRelations = new HashMap<String, String>();
    }

    public ASTNode parse() throws ParserException {
        c_tok = 0;
        lookahead = tokens.get(0);
        ASTNode node = parseSemantic();
        if (lookahead.type != TokenType.EOF){
            lookahead.throwParseError();
        }
        return node;
    }

    public Map<String, String> getVarRelations(){
        return varRelations;
    }

    private ASTNode parseSemantic() throws ParserException {
        ASTNode node = new ASTNode();
        switch (lookahead.type){
            case Lp:
                nextToken();
                node = parseSemantic();
                if (lookahead.type != TokenType.Rp){
                    lookahead.throwParseError();
                }
                nextToken();
                break;
            case NXT: 
                node.type = NodeType.NXT;
                nextToken();
                node.addChild(parseParCEPL());
                break;
            case LAST:
                node.type = NodeType.LAST;
                nextToken();
                node.addChild(parseParCEPL());                    
                break;
            case STRICT:
                node.type = NodeType.STRICT;
                nextToken();
                node.addChild(parseParCEPL());                    
                break;
            case MAX:
                node.type = NodeType.MAX;
                nextToken();
                node.addChild(parseParCEPL());                    
                break;
            case ANY:
                node.type = NodeType.ANY;
                nextToken();
                node.addChild(parseParCEPL());     
                break;               
            default:
                node.type = NodeType.ANY;
                node.addChild(parseCEPL());
                break;
        }
        
        if (node.isEmpty()) {
            lookahead.throwParseError();
        }
        return node;
    }

    private ASTNode parseParCEPL() throws ParserException {
        ASTNode node;
        if (lookahead.type != TokenType.Lp){
            lookahead.throwParseError();                
        }
        nextToken();
        node = parseCEPL();
        if (lookahead.type != TokenType.Rp){
            lookahead.throwParseError();
        }
        nextToken();
        return node;
    }

    private ASTNode parseCEPL() throws ParserException {
        ASTNode node, a, b;
        node = new ASTNode();
        if (lookahead.type == TokenType.Lp){
            nextToken();
            node = parseCEPL();
            if (lookahead.type != TokenType.Rp){
                lookahead.throwParseError();
            }
            nextToken();
        }
        else if (lookahead.type == TokenType.RELATION ){
            node = parseAssignation();
        }
        
        if (node.isEmpty()) {
            lookahead.throwParseError();
        }

        if (lookahead.type == TokenType.PLUS){
            nextToken();
            a = new ASTNode(NodeType.KLEENE);
            a.addChild(node);
            node = a;
        }

        node = parseFilterOp(node);
        node = parseCEPLOP(node);

        return node;
    }

    private ASTNode parseAssignation() throws ParserException {
        ASTNode node = new ASTNode(NodeType.ASSIGN);       
        if (!relations.contains(lookahead.sequence)){
            lookahead.throwParseError("UnknownRelationError: Relation was not declared before compilation of query.");
        }
        node.addChild(new ASTNode(NodeType.RELATION, lookahead));
        String relation = lookahead.sequence;
        nextToken();
        if (lookahead.type != TokenType.AS){
            lookahead.throwParseError();
        }
        nextToken();
        if (lookahead.type != TokenType.WORD){
            lookahead.throwParseError();
        }
        node.addChild(new ASTNode(NodeType.VARIABLE, lookahead));   
        String variable = lookahead.sequence;           
        nextToken();

        varRelations.put(variable, relation);

        return node;
    }

    private ASTNode parseCEPLOP(ASTNode prev) throws ParserException {
        ASTNode node = new ASTNode();

        if (lookahead.type == TokenType.COLON){
            node.type = NodeType.SEQ;
            nextToken();
            node.addChild(prev);
            node.addChild(parseCEPL());
        }
        else if (lookahead.type == TokenType.OR){
            node.type = NodeType.OR;
            nextToken();
            node.addChild(prev);            
            node.addChild(parseCEPL());
        }
        else {
            node = prev;
        }
        return node;
    }

    private ASTNode parseFilterOp(ASTNode prev) throws ParserException {
        ASTNode node, ret;
        node = new ASTNode();
        if (lookahead.type == TokenType.FILTER){
            node.type = NodeType.FILTER;
            nextToken();
            node.addChild(prev);
            node.addChild(parseFilterFormula());
        }
        else {
            node = prev;
        }
        return node;
    }
    
    private ASTNode parseFilterFormula() throws ParserException {
        ASTNode node;
        if (lookahead.type == TokenType.NOT){
            node = new ASTNode(NodeType.PRED_NOT);
            nextToken();
            node.addChild(parseFilterFormula());
        }
        else if (lookahead.type == TokenType.Lp){
            nextToken();
            node = parseFilterFormula();
            if (lookahead.type != TokenType.Rp){
                lookahead.throwParseError();
            }
            nextToken();
            node = parseFilterContinuation(node);
        }
        else if (lookahead.type == TokenType.WORD){
            node = new ASTNode(NodeType.PREDICATE);
            node.addChild(parseVariable());
            if (lookahead.type != TokenType.PRED_OP){
                lookahead.throwParseError();
            }
            node.addChild(new ASTNode(NodeType.PRED_OP, lookahead));

            Token op = lookahead;
            nextToken();
            if (lookahead.type == TokenType.STRING && !op.sequence.equals("=")){
                op.throwParseError("SyntaxError: Can only use equality and inequality operator over strings");
            }
            
            node.addChild(parseExpression());
            node = parseFilterContinuation(node);
        }
        else {
            node = new ASTNode(NodeType.PREDICATE);  
            TokenType vartype = lookahead.type;          
            node.addChild(parseExpression());
            if (lookahead.type != TokenType.PRED_OP){
                lookahead.throwParseError();
            }
            
            if (vartype == TokenType.STRING && !lookahead.sequence.equals("=")){
                lookahead.throwParseError("SyntaxError: Can only use equality and inequality operator over strings");
            }

            node.addChildFirst(new ASTNode(NodeType.PRED_OP, lookahead));  

            nextToken();

            node.addChildFirst(parseVariable());
            node = parseFilterContinuation(node);
        }
        return node;
    }

    private ASTNode parseFilterContinuation(ASTNode prev) throws ParserException {
        ASTNode node = new ASTNode();
        if (lookahead.type == TokenType.FILT_OP){
            node.type = lookahead.sequence.equals("or") ? NodeType.PRED_OR : NodeType.PRED_AND;
            nextToken();
            node.addChild(prev);
            node.addChild(parseFilterFormula());
        }
        else {
            node = prev;
        }
        return node;
    }

    private ASTNode parseVariable() throws ParserException {
        ASTNode node = new ASTNode(NodeType.VAR_PROP);
        if (lookahead.type != TokenType.WORD){
            lookahead.throwParseError();   
        }
        node.addChild(new ASTNode(NodeType.VARIABLE, lookahead));
        nextToken();
        if (lookahead.type != TokenType.DOT){
            lookahead.throwParseError();               
        }
        nextToken();
        if (lookahead.type != TokenType.WORD){
            lookahead.throwParseError();               
        }
        node.addChild(new ASTNode(NodeType.PROPERTY, lookahead));                    
        nextToken();
        return node;
    }

    private ASTNode parseExpression() throws ParserException {
        ASTNode node = new ASTNode(NodeType.VALUE);

        if (lookahead.type == TokenType.STRING){
            node.value = lookahead;
            nextToken();
            return node;
        }

        double a = parseSignedTerm();
        TokenType op = lookahead.type;
        double b = parseSumOp();
        if (op == TokenType.PLUS){
            a += b;
        }
        else if (op == TokenType.MINUS){
            a -= b;
        }
        node.value = new Token(fileName, null, "" + a, "", 0, 0);
        return node;
    }

    private double parseSumOp() throws ParserException {
        if (lookahead.type == TokenType.PLUS || lookahead.type == TokenType.MINUS){
            nextToken();
            double a = parseSignedTerm();
            TokenType op = lookahead.type;
            double b = parseSumOp();
            if (op == TokenType.PLUS){
                return a + b;
            }
            else if (op == TokenType.MINUS){
                return a - b;
            }
            return a;
        }
        return 0;
    }

    private double parseSignedTerm() throws ParserException {
        if (lookahead.type == TokenType.MINUS){
            nextToken();
            return - parseTerm();
        }
        else{
            return parseTerm();
        }
    }

    private double parseTerm() throws ParserException {
        double a = parseFactor();
        TokenType op = lookahead.type;
        double b = parseTermOp();
        if (op == TokenType.MULT){
            return a * b;
        }
        else if (op == TokenType.DIV){
            return a / b;
        }
        return a;
    }

    private double parseTermOp() throws ParserException {
        if (lookahead.type == TokenType.MULT || lookahead.type == TokenType.DIV){
            nextToken();
            double a = parseSignedFactor();
            TokenType op = lookahead.type;
            double b = parseTermOp();
            if (op == TokenType.MULT){
                return a * b;
            }
            else if (op == TokenType.DIV){
                return a / b;
            }
            return a;
        }
        return 0;
    }

    private double parseSignedFactor() throws ParserException {
        if (lookahead.type == TokenType.MINUS){
            nextToken();
            return -parseFactor();
        }
        else{
            return parseFactor();
        }
    }

    private double parseFactor() throws ParserException {
        if (lookahead.type == TokenType.Lp){
            nextToken();
            double a = Double.parseDouble(parseExpression().value.sequence);

            if (lookahead.type != TokenType.Rp){
                lookahead.throwParseError();
            }
            nextToken();
            return a;
        }
        else{
            return parseValue();
        }
    }

    private double parseValue() throws ParserException {
        if (lookahead.type != TokenType.NUMBER){
            lookahead.throwParseError();
        }
        double a = Double.parseDouble(lookahead.sequence);
        nextToken();
        return a;
    }


    private void nextToken(){
        lookahead = tokens.get(++c_tok);
    }
}
