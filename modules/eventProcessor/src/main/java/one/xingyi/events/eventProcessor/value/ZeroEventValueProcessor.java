package one.xingyi.events.eventProcessor.value;

import one.xingyi.events.ZeroEvent;

import java.util.concurrent.CompletableFuture;

public class ZeroEventValueProcessor<T> extends AbstractEventValueProcessor<ZeroEvent, T> {

   final private T zero;

    public ZeroEventValueProcessor(T zero) {
        super(ZeroEvent.class);
        this.zero = zero;
    }

    @Override
    protected CompletableFuture<T> applyEvent(ZeroEvent event, T value) {
        return CompletableFuture.completedFuture(zero);
    }
}
