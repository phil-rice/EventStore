package one.xingyi.eventProcessor.value;

import one.xingyi.events.LensEvent;
import one.xingyi.events.utils.BiFunctionWithException;
import one.xingyi.optics.lens.ILensTC;

import java.util.concurrent.CompletableFuture;

public class LensEventValueProcessor<T> extends AbstractEventValueProcessor<LensEvent, T> {

    final private ILensTC<T> lensTC;
    final private BiFunctionWithException<String, String, T, Exception> parser;

    public LensEventValueProcessor(ILensTC<T> lensTC, BiFunctionWithException<String, String, T, Exception> parser) {
        super(LensEvent.class);
        this.lensTC = lensTC;
        this.parser = parser;
    }

    @Override
    protected CompletableFuture<T> applyEvent(LensEvent event, T value) {
        var lens = event.lefnsDefn().asLens(lensTC);
        try {
            return CompletableFuture.completedFuture(lens.set(value, parser.apply(event.parser(), event.value())));
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
