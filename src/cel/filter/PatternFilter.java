package cel.filter;

import cel.cea.CEA;

public interface PatternFilter {
    public CEA applyToCEA(CEA cea);
}
