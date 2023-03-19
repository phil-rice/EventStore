package one.xingyi.eventProcessor;

import one.xingyi.events.utils.interfaces.FunctionWithException;
import one.xingyi.events.utils.helpers.JsonHelper;
import one.xingyi.optics.lens.ILensTC;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

public record IEventTc<T>(
        ILensTC<T> lensTC,
        FunctionWithException<Object, T, Exception> parser,
        T zero,
        Function<String, CompletableFuture<T>> id2Value,
        BiFunction<T, T, T> merge
) {
    public static IEventTc<Object> jsonEventIc(Function<String, CompletableFuture<Object>> id2Value) {
        return new IEventTc<Object>(
                ILensTC.jsonLensTc,
                j -> j,
                null,
                id2Value,
                JsonHelper::deepCombine);
    }
}
