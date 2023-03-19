package one.xingyi.events.utils.helpers;

import one.xingyi.events.utils.tuples.Tuple3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface MapHelper {

    static <K1, K2, V1, V2> CompletableFuture<Map<K1, Map<K2, V2>>> map2K(Map<K1, Map<K2, V1>> map, Function<V1, CompletableFuture<V2>> fn) {
        Map<K1, Map<K2, V2>> result = new HashMap<>();
        var x = AsyncHelper.toFutureOfList(map.entrySet().stream().flatMap(kv1 ->
                kv1.getValue().entrySet().stream().map(kv2 ->
                        fn.apply(kv2.getValue())
                                .thenApply(v2 -> new Tuple3<>(kv1.getKey(), kv2.getKey(), v2)))).toList());
        return x.thenAccept(list -> list.forEach(tuple -> put2(result, tuple.one(), tuple.two(), tuple.three()))).thenApply(willBeNull -> result);
    }

    static <K1, K2, V1, V2> Map<K1, Map<K2, V2>> map2(Map<K1, Map<K2, V1>> map, Function<V1, V2> fn) {
        Map<K1, Map<K2, V2>> result = new HashMap<>();
        map.forEach((k1, m1) -> m1.forEach((k2, v1) -> put2(result, k1, k2, fn.apply(v1))));
        return result;
    }

    static <K1, K2, V> List<V> values2(Map<K1, Map<K2, V>> map) {
        List<V> result = new ArrayList<>();
        map.forEach((k1, m1) -> m1.forEach((k2, v1) -> result.add(v1)));
        return result;
    }


    static <K1, K2, V> List<V> get2(Map<K1, Map<K2, List<V>>> map, K1 k1, K2 k2) {
        var m2 = map.get(k1);
        return m2 == null ? null : m2.get(k2);
    }


    static <K, V> void addToList(Map<K, List<V>> map, K key, V value) {
        map.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
    }


    static <K1, K2, V> void put2(Map<K1, Map<K2, V>> map, K1 k1, K2 k2, V value) {
        map.computeIfAbsent(k1, k -> new HashMap<>());
        map.get(k1).put(k2, value);
    }

    static <K1, K2, V> void addToList2(Map<K1, Map<K2, List<V>>> map, K1 k1, K2 k2, V value) {
        map.computeIfAbsent(k1, k -> new HashMap<>());
        addToList(map.get(k1), k2, value);
    }

    static <K, V, V1> Map<K, V1> map(Map<K, V> map, Function<V, V1> fn) {
        Map<K, V1> result = new HashMap<>();
        map.forEach((k, v) -> result.put(k, fn.apply(v)));
        return result;
    }

}
