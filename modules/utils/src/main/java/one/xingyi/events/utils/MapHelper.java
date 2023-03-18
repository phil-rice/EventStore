package one.xingyi.events.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MapHelper {

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

}
