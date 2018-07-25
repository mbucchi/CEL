package cel.predicate;


import cel.event.EventSchema;
import cel.filter.EventFilter;
import cel.stream.StreamSchema;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class FilterPredicate extends Predicate {

    private final Collection<EventFilter> filterCollection;

    public FilterPredicate(EventSchema eventSchema){
        super(eventSchema);
        filterCollection = List.of();
    }

    public FilterPredicate(StreamSchema streamSchema, EventSchema eventSchema){
        super(eventSchema, streamSchema);
        filterCollection = List.of();
    }


    public FilterPredicate(StreamSchema streamSchema, EventSchema eventSchema, Collection<EventFilter> filters){
        super(eventSchema, streamSchema);
        filterCollection = List.copyOf(filters);
    }

    private FilterPredicate(FilterPredicate toCopy) {
        super(toCopy);
        filterCollection = List.copyOf(toCopy.filterCollection);
    }

    private FilterPredicate(FilterPredicate toCopy, Collection<EventFilter> newFilters) {
        super(toCopy);
        List<EventFilter> temp = new ArrayList<>(toCopy.filterCollection);
        temp.addAll(newFilters);
        filterCollection = List.copyOf(temp);
    }

    public FilterPredicate addFilter(EventFilter eventFilter) {
        return new FilterPredicate(this, List.of(eventFilter));
    }

    public FilterPredicate addFilters(Collection<EventFilter> eventFilters) {
        return new FilterPredicate(this, eventFilters);
    }

    @Override
    public Predicate copy() {
        return new FilterPredicate(this);
    }

    @Override
    protected boolean implies(Predicate other) {
        if (this.equals(other)) return true;
        // TODO
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof FilterPredicate)) return false;
        FilterPredicate other = (FilterPredicate)obj;

        if (!other.filterCollection.stream().allMatch(
                f1 -> filterCollection.stream().anyMatch(f1::equals))) return false;

        if (!filterCollection.stream().allMatch(
                f1 -> other.filterCollection.stream().anyMatch(f1::equals))) return false;
        // TODO
        return true;
    }

    @Override
    public Predicate flatten() {
        // transform into an AND predicate if more than one filter is inside
        if (filterCollection.size() > 1){
            return new AndPredicate(filterCollection.stream()
                    .map(eventFilter -> new FilterPredicate(getStreamSchema(), getEventSchema(), List.of(eventFilter)))
                    .collect(Collectors.toList()));
        }
        // just return self;
        return this.copy();
    }

    @Override
    public Predicate negate() {
        if (filterCollection.size() == 1){
            // if only on filter is present, just negate the filter and return
            EventFilter filter = List.copyOf(filterCollection).get(0).negate();
//            if (filter instanceof AndEventFilter){
//                return new FilterPredicate(
//                        getStreamSchema(),
//                        getEventSchema(),
//                        ((AndEventFilter)filter).getFilterCollection());
//            }
//            else {
                return new FilterPredicate(getStreamSchema(), getEventSchema(), List.of(filter));
//            }
        }
        // else, just flatten and negate
        return flatten().negate();
    }

    @Override
    public boolean isSatisfiable() {
        return true;
    }

    @Override
    public boolean inconsistentWith(Predicate other) {
        if (!(other instanceof FilterPredicate)) return other.inconsistentWith(this);

        // contains the negation of one of the others
        return ((FilterPredicate) other).filterCollection
                .stream()
                .map(EventFilter::negate)
                .anyMatch(negated -> filterCollection.stream().anyMatch(negated::equals)
        );
    }

    @Override
    boolean consistentWith(Collection<Predicate> predicates) {
        if (!isSatisfiable()) return false;
        return predicates.stream().noneMatch(this::inconsistentWith);
    }

    @Override
    String innerToString() {
        StringBuilder stringBuilder = new StringBuilder("[");
        boolean hasFilters = false;
        for (EventFilter eventFilter : filterCollection) {
            if (hasFilters) stringBuilder.append(", ");
            stringBuilder.append(eventFilter.toString());
            hasFilters = true;
        }
        if (!hasFilters) stringBuilder.append("TRUE");
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
