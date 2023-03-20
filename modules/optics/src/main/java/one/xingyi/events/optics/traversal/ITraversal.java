package one.xingyi.events.optics.traversal;

import one.xingyi.events.optics.lens.ILens;
import one.xingyi.events.utils.helpers.ListHelper;
import one.xingyi.events.utils.helpers.MapHelper;
import one.xingyi.events.utils.interfaces.FunctionWithException;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public interface ITraversal<M, C> {
    default List<C> getAll(M m) {
        return stream(m).toList();
    }

    Stream<C> stream(M m);

    default <G> ITraversal<M, G> andThen(ITraversal<C, G> other) {
        return new ComposeTraversal<>(this, other);
    }

    default <G> ITraversal<M, G> focuson(ILens<? super C, ? extends G> lens) {
        return new LensTraversal(this, (ILens) lens);
    }

    <E extends Exception> FunctionWithException<M, M, E> replace(FunctionWithException<C, C, E> fn);


    static <K, V> ITraversal<Map<K, V>, V> mapT() {
        return new MapTraversal<>();
    }

    static <T> ITraversal<List<T>, T> listT() {
        return new ListTraversal<>();
    }

    static <T> ITraversal<T, T> singleton() {
        return new SingletonTraversal<>();
    }
}

