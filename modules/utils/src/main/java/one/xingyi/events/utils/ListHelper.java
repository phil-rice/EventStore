package one.xingyi.events.utils;

import java.util.List;
import java.util.function.BiFunction;


public interface ListHelper {
    static <Acc, V> Acc foldLeft(List<V> list, Acc acc, BiFunction<Acc, V, Acc> f) {
        Acc result = acc;
        for (V v : list) result = f.apply(result, v);
        return result;
    }
}
