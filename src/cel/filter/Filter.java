package cel.filter;

import cel.cea.CEA;

public abstract class Filter {
    public abstract CEA applyToCEA(CEA cea);
}
