package one.xingyi.events.utils.collectors;

import one.xingyi.events.utils.tuples.Tuple2;
import one.xingyi.events.utils.tuples.Tuple3;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public record StringMapCollector<T, V>(Function<T, String> keyFn,
                                       Function<T, V> valueFn) implements Collector<T, Map<String, V>, Map<String, V>> {


    @Override
    public Supplier<Map<String, V>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<String, V>, T> accumulator() {
        return (acc, t) -> acc.put(keyFn.apply(t), valueFn.apply(t));
    }

    @Override
    public BinaryOperator<Map<String, V>> combiner() {
        return (acc1, acc2) -> {
            acc1.putAll(acc2);
            return acc1;
        };
    }

    @Override
    public Function<Map<String, V>, Map<String, V>> finisher() {
        return Collections::unmodifiableMap;
    }


    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED);
    }
}
