package one.xingyi.eventProcessor;

import one.xingyi.eventProcessor.audit.EventAuditProcessor;
import one.xingyi.eventProcessor.value.LensEventValueProcessor;
import one.xingyi.eventProcessor.value.SetIdEventValueProcessor;
import one.xingyi.eventProcessor.value.SetValueEventValueProcessor;
import one.xingyi.eventProcessor.value.ZeroEventValueProcessor;
import one.xingyi.events.Audit;
import one.xingyi.events.IEvent;
import one.xingyi.events.utils.AsyncHelper;
import one.xingyi.optics.iso.IIso;
import one.xingyi.optics.iso.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IEventProcessor<T> {
    boolean canProcess(IEvent event);

    CompletableFuture<T> apply(T value, IEvent event);

    static <T> CompletableFuture<T> evaluate(IEventProcessor<T> eventProcessor, List<IEvent> events, T zero) {
        return AsyncHelper.foldLeft(events, zero, eventProcessor::apply);
    }


    static <T> IEventProcessor<T> defaultEventProcessor(IEventTc<T> tc) {
        return combine(
                new SetIdEventValueProcessor<>(tc.id2Value()),
                new SetValueEventValueProcessor<>(tc.parser()),
                new ZeroEventValueProcessor<>(tc.zero()),
                new LensEventValueProcessor<>(tc.lensTC(), tc.parser())
        );
    }

    @SafeVarargs
    static <T> IEventProcessor<T> combine(IEventProcessor<T>... eventProcessors) {
        return new CombineEventProcessor<>(Arrays.asList(eventProcessors));
    }

    static <One, Two, Res> IEventProcessor<Res> merge(IEventProcessor<One> one, IEventProcessor<Two> two, IIso<Tuple2<One, Two>, Res> iso) {
        return new MergeEventProcessor<>(one, two, iso);
    }

    static IEventProcessor<List<Audit>> auditEventProcessor() {
        return new EventAuditProcessor();
    }


}
