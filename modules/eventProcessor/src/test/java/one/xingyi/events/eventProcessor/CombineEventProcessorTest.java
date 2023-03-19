package one.xingyi.events.eventProcessor;

import one.xingyi.events.IEvent;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static one.xingyi.events.eventFixture.EventProcessorFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class CombineEventProcessorTest {

    @Test
    public void testCombineEventProcessor() throws ExecutionException, InterruptedException {
        IEventProcessor<IEvent, Object> processor = IEventProcessor.defaultEventProcessor(IEventTc.jsonEventIc(idToValueForTest));

        assertEquals(Map.of("a", 44, "b", 4), IEventProcessor.evaluate(processor,
                IEvent::isSource,
                List.of(
                        valueEvent1,
                        valueEvent2,
                        idEvent3,
                        lensEvent4
                ), null).get());
    }

    @Test
    public void testCombineEventProcessor2() throws ExecutionException, InterruptedException {
        IEventProcessor<IEvent, Object> processor = IEventProcessor.defaultEventProcessor(IEventTc.jsonEventIc(idToValueForTest));

        assertEquals(Map.of("a", 44, "b", 2), IEventProcessor.evaluate(processor, IEvent::isSource, List.of(
                valueEvent1,
                lensEvent4
        ), null).get());
    }
}