package one.xingyi.events.utils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;

public interface AsyncHelper {

    static <Acc,T> CompletableFuture<Acc> foldLeft(List<T> values, Acc zero, BiFunction<Acc, T, CompletableFuture<Acc>> f) {
        CompletableFuture<Acc> result = CompletableFuture.completedFuture(zero);
        for (T value : values) result = result.thenCompose(acc -> f.apply(acc, value));
        return result;
    }
}
