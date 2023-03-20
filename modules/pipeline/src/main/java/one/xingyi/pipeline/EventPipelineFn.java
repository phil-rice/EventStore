package one.xingyi.pipeline;

import one.xingyi.events.eventProcessor.IEventProcessor;
import one.xingyi.events.optics.traversal.ITraversalK;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * <p>
 * This gets the inputs, applies the event processor to them, and then applies the post processor
 * <p>
 * It is expected that I will be some sort of Map from the name/namespace to the list of events. Either a map or map of maps.
 * T might be anything... It's what is returned by the event processor, and we get a new map or map of maps of this...
 * <p>
 * The post processor takes the map or map of maps
 */
public record EventPipelineFn<E, T, O>(ITraversalK<Map<String, Object>, List<E>> inputs,
                                       IEventProcessor<E, T> eventProcessor,
                                       Predicate<E> isSource,
                                       T zero,
                                       Function<Map<String, Object>, CompletableFuture<O>> postProcessor) implements Function<Map<String, Object>, CompletableFuture<O>> {

    @Override
    public CompletableFuture<O> apply(Map<String, Object> i) {
        return inputs.replaceKCast(IEventProcessor.<T, E>evaluate(eventProcessor, isSource, zero)).apply(i).thenCompose(postProcessor);
    }
}
