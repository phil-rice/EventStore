package one.xingyi.events.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;


public interface ListHelper {
    static <Acc, V> Acc foldLeft(List<V> list, Acc acc, BiFunction<Acc, V, Acc> f) {
        Acc result = acc;
        for (V v : list) result = f.apply(result, v);
        return result;
    }

    static <T> List<T> add(List<T> list, T t) {
        var result = new ArrayList<T>(list.size() + 1);
        result.addAll(list);
        result.add(t);
        return result;
    }
}
