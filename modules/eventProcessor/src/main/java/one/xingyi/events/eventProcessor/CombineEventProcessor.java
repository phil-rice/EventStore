package one.xingyi.events.eventProcessor;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class CombineEventProcessor<E,T> implements IEventProcessor<E, T> {
    private final List<IEventProcessor<E, T>> eventProcessors;

    public CombineEventProcessor(List<IEventProcessor<E, T>> eventProcessors) {
        this.eventProcessors = eventProcessors;
    }

    @Override
    public boolean canProcess(E event) {
        return eventProcessors.stream().anyMatch(eventProcessor -> eventProcessor.canProcess(event));
    }

    @Override
    public CompletableFuture<T> apply(T value, E event) {
        for (IEventProcessor eventProcessor : eventProcessors)
            if (eventProcessor.canProcess(event))
                return eventProcessor.apply(value, event);
        throw new RuntimeException("No event processor found for event " + event);
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
