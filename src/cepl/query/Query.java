package cepl.query;
import cepl.cea.CEA;
import cepl.cea.MaxCEA;
import cepl.cea.MinimizedCEA;
import cepl.stream.StreamSchema;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class Query {
    private SelectionStrategy selectionStrategy;
    private ProjectionList projectionList;
    private Set<StreamSchema> streamSchemas;
    private CEA patternCEA;
    private Collection<Partition> partitions;
    private TimeWindow timeWindow;
    private ConsumptionPolicy consumptionPolicy;

    public Query(SelectionStrategy selectionStrategy,
                 ProjectionList projectionList,
                 Collection<StreamSchema> streamSchemas,
                 CEA patternCEA,
                 Collection<Partition> partitions,
                 TimeWindow timeWindow,
                 ConsumptionPolicy consumptionPolicy){

        this.selectionStrategy = selectionStrategy;
        this.projectionList = projectionList;
        this.streamSchemas = new HashSet<>(streamSchemas);
        this.patternCEA = patternCEA;
        this.partitions = partitions;
        this.timeWindow = timeWindow;
        this.consumptionPolicy = consumptionPolicy;

        runOptimizations();
    }

    private void runOptimizations() {
        patternCEA = new MinimizedCEA(patternCEA);

//  TODO: implement automatons for each selection strategy
//        switch (selectionStrategy){
//            case LAST:
//                selectionStrategy = SelectionStrategy.ALL;
//                patternCEA = new LastCEA(patternCEA);
//                break;
//            case MAX:
//                selectionStrategy = SelectionStrategy.ALL;
//                patternCEA = new MaxCEA(patternCEA);
//                break;
//            case NEXT:
//                selectionStrategy = SelectionStrategy.ALL;
//                patternCEA = new NextCEA(patternCEA);
//                break;
//            case STRICT:
//                selectionStrategy = SelectionStrategy.ALL;
//                patternCEA = new StrictCEA(patternCEA);
//                break;
//        }
    }

}
