package one.xingyi.eventProcessor.value;

import one.xingyi.events.SetValueEvent;
import one.xingyi.events.utils.interfaces.FunctionWithException;

import java.util.concurrent.CompletableFuture;

public class SetValueEventValueProcessor<T> extends AbstractEventValueProcessor<SetValueEvent, T> {

    /**
     * The object is the json in the event
     */
    private FunctionWithException<Object, T, Exception> parser;

    public SetValueEventValueProcessor(FunctionWithException<Object, T, Exception> parser) {
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
