package cepl.compiler.errors;

abstract class CompilerError extends Error {
    CompilerError(String msg){
        super(msg);
    }
}
