package one.xingyi.events.eventProcessor;

import one.xingyi.event.audit.AndAudit;
import one.xingyi.event.audit.Audit;
import one.xingyi.events.events.IEvent;
import one.xingyi.events.optics.iso.IIso;
import one.xingyi.events.utils.tuples.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static one.xingyi.events.eventFixture.EventProcessorFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MergeEventProcessorTest {

    @Test
    public void testMergeEventProcessor() throws ExecutionException, InterruptedException {
        IEventProcessor<AndAudit<IEvent>, Tuple2<Object, List<Audit>>> processor = IEventProcessor.merge(
                IEventProcessor.parentEventProcessor(IEventProcessor.defaultEventProcessor(IEventTc.jsonEventIc(idToValueForTest)), AndAudit<IEvent>::payload),
                IEventProcessor.auditEventProcessor(),
                IIso.identity()
        );
        assertEquals(new Tuple2<>(Map.of("a", 44, "b", 4), audit01234),
                IEventProcessor.evaluate(processor, e ->false, evA01234, new Tuple2<>(null, List.of())).get());
        assertEquals(new Tuple2<>(Map.of("a", 44, "b", 4), audit01234.subList(3,5)),
                IEventProcessor.evaluate(processor, e ->e.payload().isSource(), evA01234, new Tuple2<>(null, List.of())).get());

    }

}