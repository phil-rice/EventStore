package one.xingyi.events.utils.helpers;

import one.xingyi.events.utils.interfaces.RunnableWithException;
import one.xingyi.events.utils.interfaces.SupplierWithException;
import one.xingyi.events.utils.exceptions.WrappedException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;

public interface AsyncHelper {

    static <T> CompletableFuture<List<T>> toFutureOfList(List<CompletableFuture<T>> futures) {
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).toList());
    }

    static <T> CompletableFuture<Void> forEach(List<T> list, Function<T, CompletableFuture<Void>> fn) {
        var results = list.stream().map(fn).toList();
        return CompletableFuture.allOf(results.toArray(new CompletableFuture[0]));
    }


    static <E extends Exception> CompletableFuture<Void> wrapRunnable(Executor executor, RunnableWithException<E> runnable) {
        return CompletableFuture.runAsync(() -> {
            try {
                runnable.run();
            } catch (Exception e) {
                throw WrappedException.wrap(e);
            }
        }, executor);
    }

    static <T, E extends Exception> CompletableFuture<T> wrapSupplier(Executor executor, SupplierWithException<T, E> supplier) {
        return CompletableFuture.<T>supplyAsync(WrappedException.wrapSupplier(supplier), executor);
    }

    static <Acc, T> CompletableFuture<Acc> foldLeft(List<T> values, Acc zero, BiFunction<Acc, T, CompletableFuture<Acc>> f) {
        CompletableFuture<Acc> result = CompletableFuture.completedFuture(zero);
        for (T value : values) result = result.thenCompose(acc -> f.apply(acc, value));
        return result;
    }
}
