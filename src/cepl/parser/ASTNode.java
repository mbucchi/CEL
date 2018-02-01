package cepl.parser;

import java.util.HashSet;
import java.util.LinkedList;

enum NodeType {
    MAX,
    STRICT,
    ANY,
    NXT,
    LAST,

    FILTER,
    ASSIGN,
    OR,
    KLEENE,
    SEQ,
    
    PREDICATE,
    PRED_OR,
    PRED_AND,
    PRED_NOT,
    PRED_OP,

    VALUE,
    VARIABLE,
    RELATION,
    VAR_PROP,
    PROPERTY,

    EMPTY,
}

public class ASTNode {

    LinkedList<ASTNode> children;
    NodeType type;
    Token value;
    private HashSet<String> var;
    private HashSet<String> vDef;
    private HashSet<String> vDefPlus;
    private HashSet<String> bound;

    public ASTNode(NodeType type, Token value){
        children = new LinkedList<ASTNode>();
        this.type = type;
        this.value = value;
        prepareSets();
    }
    
    public ASTNode(){
        this(NodeType.EMPTY, null);
    }

    public ASTNode(NodeType type){
        this(type, null);
    }

    public boolean isEmpty(){
        return type == NodeType.EMPTY;
    }

    public void print(){
        print("");
    }

    public void print(String sep){
        String t = type.name();
        StringBuilder varStr = new StringBuilder();
        for (String v: bound){
            varStr.append(v + " ");
        }
        String v = value != null ? value.sequence : varStr.toString();
        System.out.println(sep + t + ": " + v);
        for (ASTNode child: children){
            child.print(sep + "  ");
        }
    }

    private void prepareSets(){
        var = new HashSet<String>();
        vDef = new HashSet<String>();
        vDefPlus = new HashSet<String>();
        bound = new HashSet<String>();
    }

    public void setVars(){
        ASTNode left, right;
        switch (type){
            case VARIABLE:
                var.add(value.sequence);
                break;
            case ASSIGN:
                // get variables from assignation
                right = children.getLast();
                right.setVars();
                var.addAll(right.var);
                vDef.addAll(var);
                vDefPlus.addAll(var);
                bound.addAll(var);
                break;
            case FILTER:
                left = children.getFirst();
                right = children.getLast();
                left.setVars();
                right.setVars();
                
                var.addAll(left.var);
                var.addAll(right.var);

                vDef.addAll(left.vDef);
                vDefPlus.addAll(left.vDefPlus);
                bound.addAll(left.bound);
                break;
            case OR:
                left = children.getFirst();
                right = children.getLast();
                left.setVars();
                right.setVars();
                
                var.addAll(left.var);
                var.addAll(right.var);

                vDef.addAll(left.vDef);
                vDef.addAll(right.vDef);
                
                vDefPlus.addAll(left.vDefPlus);
                vDefPlus.addAll(right.vDefPlus);
                
                bound.addAll(left.bound);
                bound.retainAll(right.bound);
                break;
            case SEQ:
                left = children.getFirst();
                right = children.getLast();
                left.setVars();
                right.setVars();
                
                var.addAll(left.var);
                var.addAll(right.var);

                vDef.addAll(left.vDef);
                vDef.addAll(right.vDef);
                
                vDefPlus.addAll(left.vDefPlus);
                vDefPlus.addAll(right.vDefPlus);
                
                bound.addAll(left.bound);
                bound.addAll(right.bound);
                break;
            case KLEENE:
                left = children.getFirst();
                left.setVars();
                var.addAll(left.var);
                vDef.addAll(left.vDef);
            case PREDICATE:
            case PRED_OR:
            case PRED_AND:
            case PRED_NOT:
            case PRED_OP:
            case VAR_PROP:
                for (ASTNode child: children){
                    child.setVars();
                    var.addAll(child.var);
                }
            default:
        }
        
    }
}