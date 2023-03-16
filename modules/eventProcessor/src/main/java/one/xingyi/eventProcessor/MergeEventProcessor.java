package one.xingyi.eventProcessor;

import one.xingyi.events.IEvent;
import one.xingyi.optics.iso.IIso;
import one.xingyi.optics.iso.Tuple2;

import java.util.concurrent.CompletableFuture;

public class MergeEventProcessor<One, Two, Res> implements IEventProcessor<Res> {

    private final IEventProcessor<One> one;
    private final IEventProcessor<Two> two;
    private IIso<Tuple2<One, Two>, Res> iso;

    public MergeEventProcessor(IEventProcessor<One> one, IEventProcessor<Two> two, IIso<Tuple2<One, Two>, Res> iso) {
        this.one = one;
        this.two = two;
        this.iso = iso;
    }

    @Override
    public boolean canProcess(IEvent event) {
        return one.canProcess(event) && two.canProcess(event);
    }

    @Override
    public CompletableFuture<Res> apply(Res value, IEvent event) {
        var tuple = iso.from(value);

        return one.apply(tuple.one(), event).thenCombine(two.apply(tuple.two(), event), (r1, r2) -> iso.to(new Tuple2<>(r1, r2)));
    }
}
