import cel.query.Query;
import cel.runtime.source.CEASourceGenerator;

public class CEACodeGetter {

    public static String getCEACode(Query q) {
        CEASourceGenerator ceaTest = new CEASourceGenerator(q.getPatternCEA());
        return ceaTest.makeSourceCode();
    }
}
