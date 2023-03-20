package one.xingyi.events.optics.traversal;

import one.xingyi.events.utils.helpers.MapHelper;
import one.xingyi.events.utils.interfaces.FunctionWithException;

import java.util.Map;
import java.util.stream.Stream;

class MapTraversal<K, V> implements ITraversal<Map<K, V>, V> {

    @Override
    public Stream<V> stream(Map<K, V> m) {
        return m.values().stream();
    }

    @Override
    public <E extends Exception> FunctionWithException<Map<K, V>, Map<K, V>, E> replace(FunctionWithException<V, V, E> fn) {
        return m -> MapHelper.mapE(m, fn);
    }
}
