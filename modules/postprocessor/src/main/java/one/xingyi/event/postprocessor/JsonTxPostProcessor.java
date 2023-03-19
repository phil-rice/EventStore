package one.xingyi.event.postprocessor;

import one.xingyi.events.utils.helpers.JsonHelper;
import one.xingyi.events.utils.jsontransform.IJsonTransform;
import one.xingyi.store.idvaluestore.IIdAndValueStore;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

/**
 * Turns the result into a json string
 */
public class JsonTxPostProcessor<C> implements IPostProcessor {
    private Function<String, CompletableFuture<String>> id2JsonString;
    private IJsonTransform<C> jsonTransform;

    JsonTxPostProcessor(Function<String, CompletableFuture<String>> id2JsonString, IJsonTransform<C> jsonTransform) {
        this.id2JsonString = id2JsonString;
        this.jsonTransform = jsonTransform;
    }

    @Override
    public CompletableFuture<byte[]> postProcess(String postProcess, Object resultFromEvents) {
        if (!postProcess.startsWith("transform:"))
            throw new RuntimeException("Cannot handle postProcess " + postProcess);
        var txid = postProcess.substring(10);
        return id2JsonString.apply(txid).thenApply(defnString -> {
            var compiled = jsonTransform.compile(defnString);
            return jsonTransform.transform(compiled, JsonHelper.mapper.valueToTree(resultFromEvents)).toPrettyString().getBytes(StandardCharsets.UTF_8);
        });
    }
}
