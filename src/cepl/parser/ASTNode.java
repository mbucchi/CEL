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

class ASTNode {

    LinkedList<ASTNode> children;
    NodeType type;
    Token value;
    ASTNode parent;

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

    public void addChild(ASTNode child){
        children.add(child);
        child.parent = this;
    }

    public void addChildFirst(ASTNode child){
        children.addFirst(child);
        child.parent = this;
    }

    private void setVars(){
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

    private void checkWellformness() throws WellformedException {
        switch (type){
            case FILTER:
                HashSet<String> filterVars = new HashSet<String>(children.getLast().var);
                // look upwards for variable bounding
                if (findVarDef(filterVars) == null) {
                    String var = filterVars.toArray(new String[0])[0];
                    getVarToken(var).throwWellformedError(); 
                }
            case OR:
            case SEQ:
            case KLEENE:
                // check children for wellformness in all four cases
                for (ASTNode child: children){
                    child.checkWellformness();
                }
            default:
        }
    }

    private Token getVarToken(String var){
        if (type == NodeType.FILTER){
            return children.getLast().getVarToken(var);
        }
        else if (type == NodeType.VARIABLE && value.sequence.equals(var)){
            return value;
        }
        else {
            Token t;
            for (ASTNode child: children){
                t = child.getVarToken(var);
                if (t != null){
                    return t;
                }
            }
        }
        return null;
    }

    private ASTNode findVarDef(HashSet<String> varsToFind){
        varsToFind.removeAll(bound);
        if (varsToFind.size() == 0) return this;
        else if (parent != null) return parent.findVarDef(varsToFind);
        else return null;
    }


    private void checkSafeness(){
        switch (type){
            case SEQ:
            case OR:
                ASTNode left = children.getFirst();
                ASTNode right = children.getLast();
                left.checkSafeness();
                HashSet<String> _vardefp = new HashSet<String>(left.vDefPlus);
                _vardefp.retainAll(right.vDefPlus);
                if (_vardefp.size() > 0){
                    for (String var : _vardefp){
                        Token var1 = left.getVarToken(var);
                        Token var2 = right.getVarToken(var);
                        new SafeFormulaWarning(var1, var2);
                    }
                }
                right.checkSafeness();                
                break;
            case FILTER:
            case KLEENE:
                children.getFirst().checkSafeness();
            default:
        }
    }

    public void lpNormalize(){
        setVars();
        checkWellformness();
        checkSafeness();
        SafeFormulaWarning.postWarnings();
        while (pushPredictesUp()) setVars(); 
        setVars();
        pushPredicatesDown(); 
        consolidateFilters();
        removeNegations();
        setVars();
    }

    private boolean pushPredictesUp(){
        switch (type){
            case OR:
            case SEQ:
                return children.getFirst().pushPredictesUp() || children.getLast().pushPredictesUp();
            case KLEENE:
                return children.getFirst().pushPredictesUp();
            case FILTER:
                boolean child = children.getFirst().pushPredictesUp();
                ASTNode predicate = children.getLast();
                ASTNode scope = findVarDef(predicate.var);
                if (scope == this){
                    return child;
                }

                // making new scope
                ASTNode newScope = new ASTNode(NodeType.OR);
                ASTNode firstUncle = findFirstOrUncle(scope);
                newScope.parent = scope.parent;
                scope.parent = newScope;

                // making new trees

                
                // TRUE tree
                ASTNode trueNode = new ASTNode(NodeType.FILTER);
                replace(this, children.getFirst());

                trueNode.addChild(scope.clone());
                trueNode.addChild(predicate);
                newScope.addChild(trueNode);

                // FALSE tree
                if (firstUncle == null){
                    // there is no need for an OR node to be added
                    replace(scope, newScope.children.getFirst());
                    scope.parent = newScope.parent;
                }
                else {
                    ASTNode falseNode = new ASTNode(NodeType.FILTER);
                    ASTNode orParent = firstUncle.parent;
                    replace(orParent, firstUncle);
                    falseNode.addChild(scope.clone());

                    ASTNode notNode = new ASTNode(NodeType.PRED_NOT);
                    notNode.addChild(predicate);

                    falseNode.addChild(notNode);

                    newScope.addChild(falseNode);
                    replace(scope, newScope);
                    scope.parent = newScope.parent;
                }
                return true;
            default:
        }
        return false;
    }

    private ASTNode findFirstOrUncle(ASTNode limit){
        ASTNode ret = this;
        while (ret.parent != null && ret.parent != limit && ret.parent.type != NodeType.OR){
            ret = ret.parent;
        }
        if (ret.parent == null || ret.parent == limit) return null;

        // the latest ancestor son of a OR node has been found

        int idx = ret.parent.children.indexOf(ret);

        // return brother of that ancestor
        if (idx == 0) return ret.parent.children.get(1);
        else return ret.parent.children.get(0);
    }


    private void pushPredicatesDown(){
        switch (type){
            case KLEENE:
                children.getFirst().pushPredicatesDown();
                break;
            case SEQ:
            case OR:
                children.getFirst().pushPredicatesDown();
                children.getLast().pushPredicatesDown();
                break;
            case FILTER:
                children.getFirst().pushPredicatesDown();
                ASTNode child = children.getFirst();
                ASTNode predicate = children.getLast();
                
                if (child.type != NodeType.ASSIGN && child.type != NodeType.FILTER){
                    for (ASTNode c: children){
                        c.setPredicate(predicate);
                    }
                    replace(this, child);
                }
        }
    }

    private void setPredicate(ASTNode predicate){
        if (type == NodeType.ASSIGN && predicate.var.contains(bound.toArray(new String[0])[0])){
            ASTNode newNode = new ASTNode(NodeType.FILTER);
            copyVars(newNode, this);
            if (parent != null){
                int idx = parent.children.indexOf(this);
                parent.children.set(idx, newNode);
                newNode.parent = parent;
            }
            newNode.addChild(this);
            newNode.addChild(predicate.clone());
        }
        for (ASTNode child: children){
            child.setPredicate(predicate);
        }
    }

    private void consolidateFilters(){
        for (ASTNode child: children){
            child.consolidateFilters();
        }

        if (type == NodeType.FILTER){
            if (children.getFirst().type == NodeType.FILTER){
                ASTNode child = children.removeFirst();
                ASTNode andNode = new ASTNode(NodeType.PRED_AND);
                andNode.addChild(children.removeLast());
                andNode.addChild(child.children.removeLast());
                addChild(child.children.removeFirst());
                addChild(andNode);
            }
        }
    }

    private void removeNegations(){
        if (type == NodeType.PRED_NOT){
            replace(this, pushDownNeg());
        }
        for (ASTNode child: children){
            child.removeNegations();
        }
    }

    private ASTNode pushDownNeg(){
        ASTNode curr = this;
        while ( curr.type == NodeType.PRED_NOT ){
            curr = curr.children.getFirst();
            if (curr.type == NodeType.PRED_NOT){
                curr = curr.children.getFirst();     
            }
            else if (curr.type == NodeType.PRED_AND){
                curr.type = NodeType.PRED_OR;
                for (ASTNode c: curr.children){
                    c.pushDownNeg();
                }
            }
            else if (curr.type == NodeType.PRED_OR){
                curr.type = NodeType.PRED_AND;
                for (ASTNode c: curr.children){
                    c.pushDownNeg();
                }
            }
            else { // PREDICATE
                curr.children.get(1).switchPredicateOperator();
            }
        }
        return curr;
    }

    private void switchPredicateOperator(){
        if (type != NodeType.PRED_OP) return;
        if (value.sequence.equals("=")) value = new Token(null, TokenType.PRED_OP, "!=", null, 0, 0);
        else if (value.sequence.equals("!=")) value = new Token(null, TokenType.PRED_OP, "=", null, 0, 0);
        else if (value.sequence.equals(">")) value = new Token(null, TokenType.PRED_OP, "<=", null, 0, 0);
        else if (value.sequence.equals(">=")) value = new Token(null, TokenType.PRED_OP, "<", null, 0, 0);
        else if (value.sequence.equals("<=")) value = new Token(null, TokenType.PRED_OP, ">", null, 0, 0);
        else if (value.sequence.equals("<")) value = new Token(null, TokenType.PRED_OP, ">=", null, 0, 0);
    }

    public ASTNode clone(){
        ASTNode newNode = new ASTNode(type, value);
        newNode.bound = new HashSet<String>(bound);
        newNode.var = new HashSet<String>(var);
        newNode.vDef = new HashSet<String>(vDef);
        newNode.vDefPlus = new HashSet<String>(vDefPlus);
        for (ASTNode child: children){
            newNode.addChild(child.clone());
        }
        return newNode;
    }

    private static void copyVars(ASTNode a, ASTNode b){
        a.bound = b.bound;
        a.var = b.var;
        a.vDef = b.vDef;
        a.vDefPlus = b.vDefPlus;
    }

    private static void replace(ASTNode a, ASTNode b){
        copyVars(a, b);
        a.type = b.type;
        a.value = b.value;
        a.children.clear();

        for (ASTNode child: b.children){
            a.addChild(child);
        }
    }
}



class WellformedException extends RuntimeException {
    public WellformedException(String msg){
        super(msg);
    }
}

class SafeFormulaWarning {

    public static boolean throwWarning = true;
    private Token var1;
    private Token var2;

    private static int warnings = 0;

    public SafeFormulaWarning(Token var1, Token var2){
        this.var1 = var1;
        this.var2 = var2;

        if (throwWarning){
            throwWarning();
        }
    }

    public void throwWarning(){
        if (warnings == 0){
            System.out.println("WARNINGS: ");
        }
        warnings++;
        if (var1.getLineNo() == var2.getLineNo()){
            System.out.print(
                "File \"" + var1.getFileName() + "\", line " + var1.getLineNo() + "\n" +
                "\t" + var1.getLine() + "\n" +
                String.format("\t%" + var1.getColNo() + "s", "^ ") +
                String.format("%" + (var2.getColNo() - var1.getColNo()) + "s", "^\n")
            );
        }
        else {
            System.out.print(
                "File \"" + var1.getFileName() + "\", lines " + var1.getLineNo() + " and " + var2.getLineNo() + "\n" +
                "\t" + var1.getLine() + "\n" +
                String.format("\t%" + var1.getColNo() + "s", "^\n") +
                "\t" + var2.getLine() + "\n" +
                String.format("\t%" + var2.getColNo() + "s", "^\n")
            );
        }
        System.out.println("UnsafeFormulaWarning: variable '" + var1.sequence + "' defined more than once within the same scope.\n");
    }

    public static void postWarnings(){
        if (throwWarning && warnings > 0){
            System.out.println("Hide unsafe formula warnings by using flag --ignoreUnsafe");
        }
    }

}