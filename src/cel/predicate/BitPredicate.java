package cel.predicate;

import java.util.BitSet;

public class BitPredicate {

    private BitSet mask;
    private BitSet match;

    private static final BitPredicate truePredicate = new BitPredicate();
    private static final BitPredicate falsePredicate = new BitPredicate();

    private BitPredicate(){ }

    BitPredicate(BitSet mask, BitSet match){
        this.mask = mask;
        this.match = match;
    }

    public BitPredicate cojoin(BitPredicate other){
        if (isTruePredicate()) return other.copy();
        if (isFalsePredicate()) return copy();

        // Testing if these predicates are satisfiable together
        BitSet temp = (BitSet) match.clone();
        temp.and(other.mask);

        BitSet temp2 = (BitSet) other.match.clone();
        temp2.and(mask);

        temp.xor(temp2);

        if (!temp.isEmpty()){
            // these filters are incompatible
            return getFalsePredicate();
        }

        BitPredicate newBitPredicate = copy();
        newBitPredicate.mask.or(other.mask);
        newBitPredicate.match.or(other.match);

        if (newBitPredicate.match.isEmpty()) return falsePredicate;

        return newBitPredicate;
    }

    public BitPredicate copy() {
        if (isFalsePredicate() || isTruePredicate()) return this;
        return new BitPredicate((BitSet)mask.clone(), (BitSet)match.clone());
    }

    public boolean isFalsePredicate() {
        return this == falsePredicate;
    }

    public boolean isTruePredicate() {
        return this == truePredicate;
    }

    public static BitPredicate getFalsePredicate(){
        return falsePredicate;
    }

    public static BitPredicate getTruePredicate(){
        return truePredicate;
    }

    @Override
    public int hashCode() {
        if (isFalsePredicate() || isTruePredicate()) {
            return super.hashCode();
        }
        return mask.hashCode() * 29 + match.hashCode() * 13;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof BitPredicate)) return false;
        BitPredicate other = (BitPredicate)obj;
        return (mask.equals(other.mask) && match.equals(other.match));
    }

    @Override
    public String toString() {
        int nBits = BitPredicateFactory.getInstance().getnBits();

        if (isTruePredicate()) return String.format("[%-" + (nBits)  + "s]", String.format("%" + (4 + (nBits - 4) / 2) + "s", "TRUE"));
        if (isFalsePredicate()) return String.format("[%-" + (nBits)  + "s]", String.format("%" + (5 + (nBits - 5) / 2) + "s", "FALSE"));

        StringBuilder stringBuilder = new StringBuilder("[");

        for (int bit=0; bit < nBits; bit++){
            if (mask.get(bit)) stringBuilder.append(match.get(bit) ? "●" : "○");
            else stringBuilder.append("*");
        }

        return stringBuilder.append("]").toString();
    }
}
