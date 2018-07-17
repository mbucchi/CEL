package cel.runtime.utils;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class BitMapTransition {

    private BitSet ANDMask;
    private BitSet ANDResult;
    private List<BitSet> ORMasks;
    private List<BitSet> ORResults;
    private Integer toState;

    public BitMapTransition(Integer toState) {
        this.toState = toState;
    }

    public void addANDMask(BitSet ANDMask) {
        if (this.ANDMask == null) {
            this.ANDMask = ANDMask;
        } else {
            this.ANDMask.or(ANDMask);
        }
    }

    public void addANDResult(BitSet ANDResult) {
        if (this.ANDResult == null) {
           this.ANDResult = ANDResult;
        } else {
            /* these should always be disjoint, else the transition is trivially unsatisfiable */
            this.ANDResult.or(ANDResult);
        }
    }

    public void addORMask(BitSet ORFilter) {
        if (ORMasks == null) {
            ORMasks = new ArrayList<>();
        }
        ORMasks.add(ORFilter);
    }

    public void addORResults(BitSet ORResult) {
        if (ORResults == null) {
            ORResults = new ArrayList<>();
        }
        ORResults.add(ORResult);
    }

    public boolean isSatisfiedBy(BitSet b) {
        if (ANDMask != null) {
            BitSet ANDTemp = (BitSet) b.clone();
            ANDTemp.and(ANDMask);
            if (!ANDTemp.equals(ANDResult)) {
                return false;
            }
        }
        if (ORMasks != null) {
            BitSet ORTemp;
            for (int i = 0; i < ORMasks.size(); i++) {
                ORTemp = (BitSet) b.clone();
                ORTemp.xor(ORResults.get(i));
                ORTemp.and(ORMasks.get(i));
                if (ORTemp.isEmpty()) {
                    return false;
                }

            }
        }
        return true;
    }

    public Integer getToState() {
        return toState;
    }
}
