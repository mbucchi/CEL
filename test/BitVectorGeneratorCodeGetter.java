import cel.query.Query;
import cel.runtime.source.BitVectorSourceGenerator;
import cel.runtime.source.CEASourceGenerator;

public class BitVectorGeneratorCodeGetter {

    public static String getBitVectorGeneratorCode(Query q) {

        CEASourceGenerator ceaTest = new CEASourceGenerator(q.getPatternCEA());
        BitVectorSourceGenerator test = new BitVectorSourceGenerator(ceaTest.getBitVectorOrder());
        return test.makeSourceCode();
    }

}
