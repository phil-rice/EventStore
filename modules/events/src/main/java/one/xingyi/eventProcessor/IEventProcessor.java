package one.xingyi.eventProcessor;

import one.xingyi.events.IEvent;

import java.util.Arrays;
import java.util.concurrent.CompletableFuture;

public interface IEventProcessor {
    boolean canProcess(IEvent event);

    <T> CompletableFuture<T> apply(IEventTc<T> tc, IEvent event, T value);

    static IEventProcessor defaultEventProcessor = combine(
            new SetIdEventProcessor(),
            new SetValueEventProcessor(),
            new ZeroEventProcessor(),
            new LensEventProcessor()
    );

    static IEventProcessor combine(IEventProcessor... eventProcessors) {
        return new CombineEventProcessor(Arrays.asList(eventProcessors));
    }
}
