package one.xingyi.eventProcessor;

import one.xingyi.events.utils.BiFunctionWithException;
import one.xingyi.events.utils.JsonHelper;
import one.xingyi.optics.lens.ILensTC;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

public record IEventTc<T>(
        ILensTC<T> lensTC,
        BiFunctionWithException<String, String, T, Exception> parser,
        T zero,
        Function<String, CompletableFuture<T>> id2Value,
        BiFunction<T, T, T> merge
) {
    static IEventTc<Object> jsonEventIc(Function<String, CompletableFuture<Object>> id2Value) {
        return new IEventTc<Object>(
                ILensTC.jsonLensTc,
                JsonHelper.toJsonParser,
                null,
                id2Value,
                JsonHelper::deepCombine);
    }
}
