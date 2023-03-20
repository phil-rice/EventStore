package one.xingyi.events.optics.traversal;

import one.xingyi.events.utils.helpers.AsyncHelper;
import one.xingyi.events.utils.helpers.ListHelper;
import one.xingyi.events.utils.interfaces.FunctionWithException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

class ListTraversalK<T> implements ITraversalK<List<T>, T> {

    @Override
    public Stream<T> stream(List<T> m) {
        return m.stream();
    }

    @Override
    public <E extends Exception> FunctionWithException<List<T>, List<T>, E> replace(FunctionWithException<T, T, E> fn) {
        return list -> ListHelper.map(list, fn);
    }

    @Override
    public Function<List<T>, CompletableFuture<List<T>>> replaceK(Function<T, CompletableFuture<T>> fn) {
        return list -> AsyncHelper.mapK(list, fn);
    }
}
