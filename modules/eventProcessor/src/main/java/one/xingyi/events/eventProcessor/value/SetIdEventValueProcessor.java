package one.xingyi.events.eventProcessor.value;

import one.xingyi.events.events.SetIdEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class SetIdEventValueProcessor<T> extends AbstractEventValueProcessor<SetIdEvent, T> {

   final private Function<String, CompletableFuture<T>> idToValue;

    public SetIdEventValueProcessor(Function<String, CompletableFuture<T>> idToValue) {
        super(SetIdEvent.class);
        this.idToValue = idToValue;
    }

    @Override
    protected CompletableFuture<T> applyEvent(SetIdEvent event, T value) {
        return idToValue.apply(event.id());
    }
}
