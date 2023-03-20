package one.xingyi.events.optics.traversal;

import one.xingyi.events.utils.helpers.AsyncHelper;
import one.xingyi.events.utils.helpers.MapHelper;
import one.xingyi.events.utils.interfaces.FunctionWithException;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

class MapTraversalK<K, V> implements ITraversalK<Map<K, V>, V> {
    @Override
    public Stream<V> stream(Map<K, V> m) {
        return m.values().stream();
    }

    @Override
    public <E extends Exception> FunctionWithException<Map<K, V>, Map<K, V>, E> replace(FunctionWithException<V, V, E> fn) {
        return m -> MapHelper.mapE(m, fn);
    }

    @Override
    public Function<Map<K, V>, CompletableFuture<Map<K, V>>> replaceK(Function<V, CompletableFuture<V>> fn) {
        return kvMap -> AsyncHelper.mapK(kvMap.entrySet(), ev -> {
            var k = ev.getKey();
            var v = ev.getValue();
            return fn.apply(v).thenApply(v1 -> Map.entry(k, v1));
        }).thenApply(MapHelper::fromEntries);
    }

}
