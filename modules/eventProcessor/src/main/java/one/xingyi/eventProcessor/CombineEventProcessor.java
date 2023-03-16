package one.xingyi.eventProcessor;

import one.xingyi.events.IEvent;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class CombineEventProcessor<T> implements IEventProcessor<T> {
    private final List<IEventProcessor<T>> eventProcessors;

    public CombineEventProcessor(List<IEventProcessor<T>> eventProcessors) {
        this.eventProcessors = eventProcessors;
    }

    @Override
    public boolean canProcess(IEvent event) {
        return eventProcessors.stream().anyMatch(eventProcessor -> eventProcessor.canProcess(event));
    }

    @Override
    public CompletableFuture<T> apply(T value, IEvent event) {
        for (IEventProcessor eventProcessor : eventProcessors)
            if (eventProcessor.canProcess(event))
                return eventProcessor.apply(value, event);
        throw new RuntimeException("No event processor found for event " + event);
    }

    public List<IEventProcessor<T>> eventProcessors() {
        return eventProcessors;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        var that = (CombineEventProcessor) obj;
        return Objects.equals(this.eventProcessors, that.eventProcessors);
    }

    @Override
    public int hashCode() {
        return Objects.hash(eventProcessors);
    }

    @Override
    public String toString() {
        return "CombineEventProcessor[" +
                "eventProcessors=" + eventProcessors + ']';
    }

}
