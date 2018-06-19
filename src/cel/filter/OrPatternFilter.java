package cel.filter;

import cel.cea.CEA;
import cel.cea.OrCEA;

public class OrPatternFilter implements PatternFilter {
    private PatternFilter left;
    private PatternFilter right;

    public OrPatternFilter(PatternFilter left, PatternFilter right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public CEA applyToCEA(CEA cea) {
        // TODO: optimize this. In most cases there is no need to duplicate the whole automata
        CEA leftCea = left.applyToCEA(cea.copy());
        CEA rightCea = right.applyToCEA(cea.copy());
        return new OrCEA(leftCea, rightCea);
    }
}
