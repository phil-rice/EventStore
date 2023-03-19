package one.xingyi.eventProcessor;

import one.xingyi.optics.iso.IIso;
import one.xingyi.events.utils.tuples.Tuple2;

import java.util.concurrent.CompletableFuture;

public class MergeEventProcessor<One, Two, Res, E> implements IEventProcessor<E, Res> {

    private final IEventProcessor<E, One> one;
    private final IEventProcessor<E, Two> two;
    private IIso<Tuple2<One, Two>, Res> iso;

    public MergeEventProcessor(IEventProcessor<E, One> one, IEventProcessor<E, Two> two, IIso<Tuple2<One, Two>, Res> iso) {
        this.one = one;
        this.two = two;
        this.iso = iso;
    }

    @Override
    public boolean canProcess(E event) {
        return one.canProcess(event) && two.canProcess(event);
    }

    @Override
    public CompletableFuture<Res> apply(Res value, E event) {
        var tuple = iso.from(value);

        return one.apply(tuple.one(), event).thenCombine(two.apply(tuple.two(), event), (r1, r2) -> iso.to(new Tuple2<>(r1, r2)));
    }
}
