package one.xingyi.eventProcessor;

import one.xingyi.events.IEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public record CombineEventProcessor(List<IEventProcessor> eventProcessors) implements IEventProcessor {

    @Override
    public boolean canProcess(IEvent event) {
        return eventProcessors.stream().anyMatch(eventProcessor -> eventProcessor.canProcess(event));
    }

    @Override
    public <T> CompletableFuture<T> apply(IEventTc<T> tc, IEvent event, T value) {
        for (IEventProcessor eventProcessor : eventProcessors)
            if (eventProcessor.canProcess(event))
                return eventProcessor.apply(tc, event, value);
        throw new RuntimeException("No event processor found for event " + event);
    }
}
