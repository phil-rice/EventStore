package one.xingyi.eventProcessor;

import one.xingyi.audit.AndAudit;
import one.xingyi.audit.Audit;
import one.xingyi.events.IEvent;
import one.xingyi.optics.iso.IIso;
import one.xingyi.optics.iso.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static one.xingyi.eventFixture.EventProcessorFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class MergeEventProcessorTest {

    @Test
    public void testMergeEventProcessor() throws ExecutionException, InterruptedException {
        IEventProcessor<AndAudit<IEvent>, Tuple2<Object, List<Audit>>> processor = IEventProcessor.merge(
                IEventProcessor.parentEventProcessor(IEventProcessor.defaultEventProcessor(IEventTc.jsonEventIc(idToValue)), AndAudit<IEvent>::payload),
                IEventProcessor.auditEventProcessor(),
                IIso.identity()
        );
        assertEquals(new Tuple2<>(Map.of("a", 44, "b", 4), audit01234),
                IEventProcessor.evaluate(processor, evA01234, new Tuple2<>(null, List.of())).get());

    }

}