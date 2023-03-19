package one.xingyi.events.eventProcessor;

import one.xingyi.events.utils.interfaces.FunctionWithException;
import one.xingyi.events.utils.helpers.JsonHelper;
import one.xingyi.events.optics.lens.ILensTC;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

import static one.xingyi.events.utils.exceptions.WrappedException.wrapValue;

public record IEventTc<T>(
        ILensTC<T> lensTC,
        BiFunction<String, byte[], T> byteArrayParser,
        Function<Object, T> objectParser,
        T zero,
        Function<String, CompletableFuture<byte[]>> id2Value,
        BiFunction<T, T, T> merge
) {
    public static BiFunction<String, byte[], Object> jsonByteArrayParser = (parser, bytes) -> wrapValue(() -> {
        return JsonHelper.parseJson(new String(bytes, StandardCharsets.UTF_8));
    });

    public static IEventTc<Object> jsonEventTC(Function<String, CompletableFuture<byte[]>> id2Value) {
        return new IEventTc<Object>(
                ILensTC.jsonLensTc,
                jsonByteArrayParser,
                j -> j,
                null,
                id2Value,
                JsonHelper::deepCombine);
    }
}
