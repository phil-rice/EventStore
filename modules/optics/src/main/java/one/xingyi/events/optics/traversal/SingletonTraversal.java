package one.xingyi.events.optics.traversal;

import one.xingyi.events.utils.interfaces.FunctionWithException;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

public class SingletonTraversal<T> implements ITraversalK<T, T> {
    @Override
    public Stream<T> stream(T t) {
        return Stream.of(t);
    }

    @Override
    public <E extends Exception> FunctionWithException<T, T, E> replace(FunctionWithException<T, T, E> fn) {
        return fn;
    }

    @Override
    public Function<T, CompletableFuture<T>> replaceK(Function<T, CompletableFuture<T>> fn) {
        return fn;
    }
}
