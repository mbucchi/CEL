package cepl.filter;

import cepl.cea.CEA;

public interface PatternFilter {
    public CEA applyToCEA(CEA cea);
}
