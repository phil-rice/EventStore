package one.xingyi.event.postprocessor;

import one.xingyi.events.utils.helpers.JsonHelper;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;

/**
 * Turns the result into a json string
 */
public class JsonPostProcessor implements IPostProcessor {
    @Override
    public CompletableFuture<byte[]> postProcess(String postProcessorIgnored, Object resultFromEvents) {
        String result = JsonHelper.printJson(resultFromEvents);
        return CompletableFuture.completedFuture(result.getBytes(StandardCharsets.UTF_8));
    }
}
