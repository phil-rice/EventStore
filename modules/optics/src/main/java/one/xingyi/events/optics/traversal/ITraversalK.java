package one.xingyi.events.optics.traversal;

import one.xingyi.events.optics.lens.ILens;
import one.xingyi.events.utils.helpers.AsyncHelper;
import one.xingyi.events.utils.helpers.ListHelper;
import one.xingyi.events.utils.helpers.MapHelper;
import one.xingyi.events.utils.interfaces.FunctionWithException;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

public interface ITraversalK<M, C> extends ITraversal<M, C> {
    Function<M, CompletableFuture<M>> replaceK(Function<C, CompletableFuture<C>> fn);

    default <C1> Function<M, CompletableFuture<M>> replaceKCast(Function<C, CompletableFuture<C1>> fn) {
        return replaceK((Function) fn);
    }

    default <G> ITraversalK<M, G> andThenK(ITraversalK<C, G> other) {
        return new ComposeTraversalK<>(this, other);
    }

    default <G> ITraversalK<M, G> focusonK(ILens<? super C, ? extends G> lens) {
        return new LensTraversalK(this, (ILens) lens);
    }

    static <K, V> ITraversalK<Map<K, V>, V> mapT() {
        return new MapTraversalK<>();
    }

    static <T> ITraversal<List<T>, T> listT() {
        return new ListTraversalK<>();
    }

    static <T> ITraversal<T, T> singleton() {
        return new SingletonTraversal<>();
    }

}

class ComposeTraversalK<M, C, G> extends ComposeTraversal<M, C, G> implements ITraversalK<M, G> {
    private ITraversalK<M, C> firstK;
    private ITraversalK<C, G> secondK;

    public ComposeTraversalK(ITraversalK<M, C> firstK, ITraversalK<C, G> secondK) {
        super(firstK, secondK);
        this.firstK = firstK;
        this.secondK = secondK;
    }

    @Override
    public Stream<G> stream(M m) {
        return first.stream(m).flatMap(second::stream);
    }

    @Override
    public Function<M, CompletableFuture<M>> replaceK(Function<G, CompletableFuture<G>> fn) {
        return m -> firstK.replaceK(secondK.replaceK(fn)).apply(m);
    }
}

