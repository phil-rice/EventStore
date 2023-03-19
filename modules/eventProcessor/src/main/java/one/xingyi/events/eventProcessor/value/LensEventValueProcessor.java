package one.xingyi.events.eventProcessor.value;

import one.xingyi.events.events.LensEvent;
import one.xingyi.events.utils.interfaces.FunctionWithException;
import one.xingyi.events.optics.lens.ILensTC;
import one.xingyi.events.optics.lens.LensDefn;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class LensEventValueProcessor<T> extends AbstractEventValueProcessor<LensEvent, T> {

    final private ILensTC<T> lensTC;
    final private Function<Object, T> parser;

    public LensEventValueProcessor(ILensTC<T> lensTC, Function<Object, T> parser) {
        super(LensEvent.class);
        this.lensTC = lensTC;
        this.parser = parser;
    }

    @Override
    protected CompletableFuture<T> applyEvent(LensEvent event, T value) {
        var lens = new LensDefn(event.lens()).asLens(lensTC);
        try {
            return CompletableFuture.completedFuture(lens.set(value, parser.apply(event.value())));
        } catch (Exception e) {
            return CompletableFuture.failedFuture(e);
        }
    }
}
