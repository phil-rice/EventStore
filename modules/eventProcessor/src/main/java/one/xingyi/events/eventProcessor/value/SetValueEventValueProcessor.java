package one.xingyi.events.eventProcessor.value;

import one.xingyi.events.events.SetValueEvent;
import one.xingyi.events.utils.interfaces.FunctionWithException;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class SetValueEventValueProcessor<T> extends AbstractEventValueProcessor<SetValueEvent, T> {

    /**
     * The object is the json in the event
     */
    private Function<Object, T> parser;

    public SetValueEventValueProcessor(Function<Object, T> parser) {
        super(SetValueEvent.class);
        this.parser = parser;
    }

    @Override
    protected CompletableFuture<T> applyEvent(SetValueEvent event, T value) {
        try {
            return CompletableFuture.completedFuture(parser.apply(event.value()));
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
