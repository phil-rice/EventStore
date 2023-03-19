package one.xingyi.events.eventProcessor.value;

import one.xingyi.events.eventProcessor.IEventProcessor;
import one.xingyi.events.events.IEvent;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractEventValueProcessor<E extends IEvent, T> implements IEventProcessor<IEvent, T> {
    private final Class<E> eClass;

    protected AbstractEventValueProcessor(Class<E> eClass) {
        this.eClass = eClass;
    }

    @Override
    public boolean canProcess(IEvent event) {
        return eClass.isInstance(event);
    }

    abstract protected CompletableFuture<T> applyEvent(E event, T value);

    @Override
    public CompletableFuture<T> apply(T value, IEvent event) {
        if (canProcess(event))
            return applyEvent(eClass.cast(event), value);
        throw new RuntimeException("Soft error: Event processor " + this.getClass().getName() + " cannot process event " + event.getClass().getName());
    }
}
