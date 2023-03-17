package one.xingyi.eventProcessor;

import one.xingyi.eventProcessor.audit.EventAuditProcessor;
import one.xingyi.eventProcessor.value.LensEventValueProcessor;
import one.xingyi.eventProcessor.value.SetIdEventValueProcessor;
import one.xingyi.eventProcessor.value.SetValueEventValueProcessor;
import one.xingyi.eventProcessor.value.ZeroEventValueProcessor;
import one.xingyi.events.Audit;
import one.xingyi.events.EventAndAudit;
import one.xingyi.events.IEvent;
import one.xingyi.events.utils.AsyncHelper;
import one.xingyi.optics.iso.IIso;
import one.xingyi.optics.iso.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface IEventProcessor<E, T> {
    boolean canProcess(E event);

    CompletableFuture<T> apply(T value, E event);

    static <T, E> CompletableFuture<T> evaluate(IEventProcessor<E, T> eventProcessor, List<E> events, T zero) {
        return AsyncHelper.foldLeft(events, zero, eventProcessor::apply);
    }


    static <T> IEventProcessor<IEvent, T> defaultEventProcessor(IEventTc<T> tc) {
        return combine(
                new SetIdEventValueProcessor<>(tc.id2Value()),
                new SetValueEventValueProcessor<>(tc.parser()),
                new ZeroEventValueProcessor<>(tc.zero()),
                new LensEventValueProcessor<>(tc.lensTC(), tc.parser())
        );
    }

    static <Parent, Child, T> IEventProcessor<Parent, T> parentEventProcessor(IEventProcessor<Child, T> childProcessor, Function<Parent, Child> childFn) {
        return new ParentEventProcessor<>(childProcessor, childFn);
    }

    @SafeVarargs
    static <E, T> IEventProcessor<E, T> combine(IEventProcessor<E, T>... eventProcessors) {
        return new CombineEventProcessor<>(Arrays.asList(eventProcessors));
    }


    static <E, One, Two, Res> IEventProcessor<E, Res> merge(IEventProcessor<E, One> one, IEventProcessor<E, Two> two, IIso<Tuple2<One, Two>, Res> iso) {
        return new MergeEventProcessor<>(one, two, iso);
    }

    static IEventProcessor<EventAndAudit, List<Audit>> auditEventProcessor() {
        return new EventAuditProcessor();
    }


}
