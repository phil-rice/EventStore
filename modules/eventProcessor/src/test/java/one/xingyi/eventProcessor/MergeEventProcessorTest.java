package one.xingyi.eventProcessor;

import one.xingyi.events.Audit;
import one.xingyi.optics.iso.IIso;
import one.xingyi.optics.iso.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static one.xingyi.eventProcessor.EventProcessorFixture.*;
import static org.junit.jupiter.api.Assertions.*;

class MergeEventProcessorTest {

    @Test
    public void testMergeEventProcessor() throws ExecutionException, InterruptedException {
        IEventProcessor<Tuple2<Object, List<Audit>>> processor = IEventProcessor.merge(
                IEventProcessor.defaultEventProcessor(IEventTc.jsonEventIc(idToValue)),
                IEventProcessor.auditEventProcessor(),
                IIso.identity()
        );
        assertEquals(new Tuple2<>(Map.of("a", 44, "b", 4), audit01234),
                IEventProcessor.evaluate(processor, List.of(
                        zeroEvent,
                        valueEvent1,
                        valueEvent2,
                        idEvent3,
                        lensEvent4
                ), new Tuple2<>(null, List.of())).get());

    }

}