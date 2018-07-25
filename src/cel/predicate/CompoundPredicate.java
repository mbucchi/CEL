package cel.predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

abstract class CompoundPredicate extends Predicate {

    final Collection<Predicate> predicates;

    CompoundPredicate(CompoundPredicate toCopy){
        super(toCopy);
        predicates = toCopy.predicates.stream().map(Predicate::copy).collect(Collectors.toList());
    }

    CompoundPredicate(List<Predicate> predicates){
        super(predicates.get(0));
        Predicate[] predicateArray = predicates.toArray(new Predicate[0]);
        List<Predicate> useful = new ArrayList<>();

        for (int i=0; i<predicateArray.length; i++){
            boolean usefulPredicate = true;
            for (int j=i+1; j<predicateArray.length; j++){
                if (predicateArray[i].equals(predicateArray[j])) {
                    usefulPredicate = false;
                    break;
                }
            }
            if (usefulPredicate) useful.add(predicateArray[i].copy());
        }

        this.predicates = List.copyOf(useful);

        if (!overSameStreamAndEvent(this.predicates)) throw new Error("THIS CANT HAPPEN");
    }

}
