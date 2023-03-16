package one.xingyi.eventProcessor.value;

import one.xingyi.eventProcessor.IEventTc;
import one.xingyi.events.SetValueEvent;
import one.xingyi.events.utils.BiFunctionWithException;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiFunction;
import java.util.function.Function;

public class SetValueEventValueProcessor<T> extends AbstractEventValueProcessor<SetValueEvent, T> {

    private BiFunctionWithException<String, String, T, Exception> parser;

    public SetValueEventValueProcessor(BiFunctionWithException<String, String, T, Exception> parser) {
        super(SetValueEvent.class);
        this.parser = parser;
    }

    @Override
    protected CompletableFuture<T> applyEvent(SetValueEvent event, T value) {
        try {
            return CompletableFuture.completedFuture(parser.apply(event.parser(), event.value()));
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
