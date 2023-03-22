package one.xingyi.events.utils.collectors;

import one.xingyi.events.utils.helpers.MapHelper;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

public record StringMapMapCollector<T, V>(Function<T, String> key1Fn,
                                          Function<T, String> key2Fn,
                                          Function<T, V> valueFn) implements Collector<T, Map<String, Map<String, V>>, Map<String, Map<String, V>>> {


    @Override
    public Supplier<Map<String, Map<String, V>>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<String, Map<String, V>>, T> accumulator() {
        return (acc, t) -> MapHelper.put2(acc, key1Fn.apply(t), key2Fn.apply(t), valueFn.apply(t));
    }

    @Override
    public BinaryOperator<Map<String, Map<String, V>>> combiner() {
        return (acc1, acc2) -> {
            acc1.putAll(acc2);
            return acc1;
        };
    }

    @Override
    public Function<Map<String, Map<String, V>>, Map<String, Map<String, V>>> finisher() {
        return Collections::unmodifiableMap;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED);
    }
}
