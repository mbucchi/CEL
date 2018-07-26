package cel.runtime.cea;

import java.util.BitSet;

public interface ExecutableCEA {

    Integer blackTransition(Integer state, BitSet b);
    Integer whiteTransition(Integer state, BitSet b);
    boolean isFinal(Integer state);

}
