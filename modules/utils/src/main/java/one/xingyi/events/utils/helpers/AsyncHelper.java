package one.xingyi.events.utils.helpers;

import one.xingyi.events.utils.interfaces.FunctionWithException;
import one.xingyi.events.utils.interfaces.RunnableWithException;
import one.xingyi.events.utils.interfaces.SupplierWithException;
import one.xingyi.events.utils.exceptions.WrappedException;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.StreamSupport;

public interface AsyncHelper {

    static CompletableFuture<Void> runAsync(Executor executor, RunnableWithException<Exception> runnable) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                runnable.run();
                return null;
            } catch (Exception e) {
                throw WrappedException.wrap(e);
            }
        }, executor);
    }

    static <T> CompletableFuture<List<T>> toFutureOfList(List<CompletableFuture<T>> futures) {
        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream().map(CompletableFuture::join).toList());
    }

    static <T> CompletableFuture<Void> forEach(Iterable<T> iterable, Function<T, CompletableFuture<Void>> fn) {
        var results = StreamSupport.stream(iterable.spliterator(), false).map(fn).toList();
        return CompletableFuture.allOf(results.toArray(new CompletableFuture[0]));
    }

    static <T, T1> CompletableFuture<List<T1>> mapK(Iterable<T> iterable, Function<T, CompletableFuture<T1>> fn) {
        var results = StreamSupport.stream(iterable.spliterator(), false).map(fn).toList();
        return CompletableFuture.allOf(results.toArray(new CompletableFuture[0])).thenApply(v -> results.stream().map(CompletableFuture::join).toList());
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
