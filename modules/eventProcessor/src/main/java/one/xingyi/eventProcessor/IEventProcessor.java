package one.xingyi.eventProcessor;

import one.xingyi.audit.AndAudit;
import one.xingyi.audit.Audit;
import one.xingyi.eventProcessor.audit.EventAuditProcessor;
import one.xingyi.eventProcessor.value.LensEventValueProcessor;
import one.xingyi.eventProcessor.value.SetIdEventValueProcessor;
import one.xingyi.eventProcessor.value.SetValueEventValueProcessor;
import one.xingyi.eventProcessor.value.ZeroEventValueProcessor;
import one.xingyi.events.IEvent;
import one.xingyi.events.utils.helpers.AsyncHelper;
import one.xingyi.events.utils.helpers.ListHelper;
import one.xingyi.events.utils.tuples.Tuple2;
import one.xingyi.optics.iso.IIso;

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

    static <T, E> CompletableFuture<T> evaluate(IEventProcessor<E, T> eventProcessor,Predicate<E> isSource, List<E> events, T zero) {
        return AsyncHelper.foldLeft(eventsFromLastSource(events,isSource), zero, eventProcessor::apply);
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

    static IEventProcessor<AndAudit<IEvent>, List<Audit>> auditEventProcessor() {
        return new EventAuditProcessor();
    }


}
