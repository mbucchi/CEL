package cel.runtime.cea;

import java.util.BitSet;
import java.util.Set;

public interface ExecutableCEA {

    Set<Integer> blackTransition(Integer state, BitSet b);
    Set<Integer> whiteTransition(Integer state, BitSet b);
    boolean isFinal(Integer state);
    Integer getNStates();

}
