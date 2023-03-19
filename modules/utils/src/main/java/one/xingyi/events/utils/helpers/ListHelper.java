package one.xingyi.events.utils.helpers;

import one.xingyi.events.utils.interfaces.FunctionWithException;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Predicate;


public interface ListHelper {
    static <Acc, V> Acc foldLeft(Iterable<V> list, Acc acc, BiFunction<Acc, V, Acc> f) {
        Acc result = acc;
        for (V v : list) result = f.apply(result, v);
        return result;
    }

    static <T> int lastIndexOf(List<T> list, Predicate<T> predicate) {
        for (int i = list.size() - 1; i >= 0; i--) if (predicate.test(list.get(i))) return i;
        return -1;
    }

    static <T> List<T> takeFrom(List<T> list, int index) {
        if (index < 0) return list;
        return list.subList(index, list.size());
    }

    static <T> List<T> add(List<T> list, T t) {
        var result = new ArrayList<T>(list.size() + 1);
        result.addAll(list);
        result.add(t);
        return result;
    }

    static <T, T1, E extends Exception> ArrayList<T1> map(List<T> list, FunctionWithException<T, T1, E> lineToT) throws E {
        var result = new ArrayList<T1>(list.size());
        for (T t : list) result.add(lineToT.apply(t));
        return result;
    }

    static <T, T1, E extends Exception> ArrayList<T1> flatMap(List<T> list, FunctionWithException<T, List<T1>, E> lineToT) throws E {
        var result = new ArrayList<T1>(list.size());
        for (T t : list) result.addAll(lineToT.apply(t));
        return result;
    }
}
