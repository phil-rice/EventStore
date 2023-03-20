package one.xingyi.pipeline;

import one.xingyi.events.eventProcessor.IEventProcessor;
import one.xingyi.events.optics.lens.ILens;

import java.util.function.Function;

public record EventPipelineFn<I, E, T, O>(ILens<I, E> inputLens, IEventProcessor<E, T> eventProcessor,
                                          Function<T, O> postProcessor) implements Function<I, O> {
    @Override
    public O apply(I i) {
        return null;
    }
}
