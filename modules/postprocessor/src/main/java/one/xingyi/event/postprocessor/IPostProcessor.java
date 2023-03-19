package one.xingyi.event.postprocessor;


import one.xingyi.events.utils.functions.PartialAnd;
import one.xingyi.events.utils.jsontransform.IJsonTransform;
import one.xingyi.store.idvaluestore.IIdAndValueStore;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface IPostProcessor {
    CompletableFuture<byte[]> postProcess(String postProcess, Object resultFromEvents);

    static IPostProcessor composite(IPostProcessor defaultPostProcessor, List<PartialAnd<String, IPostProcessor>> postProcessors) {
        return new CompositePostProcessor(defaultPostProcessor, postProcessors);
    }

    static <C> IPostProcessor defaultPostProcessor(Function<String, CompletableFuture<String>> id2JsonString, IJsonTransform<C> jsonTransform) {
        return composite(new JsonPostProcessor(), List.of(
                PartialAnd.match("json", new JsonPostProcessor()),
                PartialAnd.startsWith("transform:", new JsonTxPostProcessor<>(id2JsonString, jsonTransform))));
    }
}
