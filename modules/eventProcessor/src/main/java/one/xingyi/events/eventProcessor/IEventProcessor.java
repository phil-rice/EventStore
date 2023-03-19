package one.xingyi.events.eventProcessor;

import one.xingyi.event.audit.AndAudit;
import one.xingyi.event.audit.Audit;
import one.xingyi.events.eventProcessor.audit.EventAuditProcessor;
import one.xingyi.events.eventProcessor.value.LensEventValueProcessor;
import one.xingyi.events.eventProcessor.value.SetIdEventValueProcessor;
import one.xingyi.events.eventProcessor.value.SetValueEventValueProcessor;
import one.xingyi.events.eventProcessor.value.ZeroEventValueProcessor;
import one.xingyi.events.events.IEvent;
import one.xingyi.events.optics.iso.IIso;
import one.xingyi.events.utils.helpers.AsyncHelper;
import one.xingyi.events.utils.helpers.ListHelper;
import one.xingyi.events.utils.tuples.Tuple2;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

public interface IEventProcessor<E, T> {
    boolean canProcess(E event);

    CompletableFuture<T> apply(T value, E event);

    static <E> List<E> eventsFromLastSource(List<E> list, Predicate<E> isSource) {
        int index = ListHelper.lastIndexOf(list, isSource);
        return ListHelper.takeFrom(list, index);
    }

    static <T, E> CompletableFuture<T> evaluate(IEventProcessor<E, T> eventProcessor, Predicate<E> isSource, List<E> events, T zero) {
        return AsyncHelper.foldLeft(eventsFromLastSource(events, isSource), zero, eventProcessor::apply);
    }


    static <T> IEventProcessor<IEvent, T> defaultEventProcessor(IEventTc<T> tc) {
        return combine(
                new SetIdEventValueProcessor<>(tc.id2Value(), tc.byteArrayParser()),
                new SetValueEventValueProcessor<>(tc.objectParser()),
                new ZeroEventValueProcessor<>(tc.zero()),
                new LensEventValueProcessor<>(tc.lensTC(), tc.objectParser())
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

    static IEventProcessor<AndAudit<IEvent>, List<Audit>> auditEventProcessor() {
        return new EventAuditProcessor();
    }


}
