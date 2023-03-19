package one.xingyi.events.eventProcessor.value;

import one.xingyi.events.events.SetIdEvent;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SetIdEventValueProcessor<T> extends AbstractEventValueProcessor<SetIdEvent, T> {

    final private Function<String, CompletableFuture<byte[]>> idToValue;
    private BiFunction<String, byte[], T> parser;

    public SetIdEventValueProcessor(Function<String, CompletableFuture<byte[]>> idToValue, BiFunction<String, byte[], T> parser) {
        super(SetIdEvent.class);
        this.idToValue = idToValue;
        this.parser = parser;
    }

    @Override
    protected CompletableFuture<T> applyEvent(SetIdEvent event, T value) {
        return idToValue.apply(event.id()).thenApply(bytes -> parser.apply(event.parser(), bytes));
    }
}
