package one.xingyi.eventProcessor;

import one.xingyi.events.IEvent;

import java.util.concurrent.CompletableFuture;

public abstract class AbstractEventProcessor<E extends IEvent> implements IEventProcessor {

    private final Class<E> eClass;

    protected AbstractEventProcessor(Class<E> eClass) {
        this.eClass = eClass;
    }

    @Override
    public boolean canProcess(IEvent event) {
        return eClass.isInstance(event);
    }

    abstract protected <T> T applyEvent(IEventTc<T> tc, E event, T value);

    @Override
    public <T> CompletableFuture<T> apply(IEventTc<T> tc, IEvent event, T value) {
        if (canProcess(event))
            return apply(tc, eClass.cast(event), value);
        throw new RuntimeException("Soft error: Event processor " + this.getClass().getName() + " cannot process event " + event.getClass().getName());
    }
}
