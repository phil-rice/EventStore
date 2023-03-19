package one.xingyi.events.eventProcessor.value;

import one.xingyi.events.eventFixture.EventProcessorFixture;
import one.xingyi.events.eventProcessor.IEventProcessor;
import one.xingyi.events.events.IEvent;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SetValueEventValueProcessorTest {
    @Test
    public void testSetValueEventValueProcessor() throws ExecutionException, InterruptedException {
        IEventProcessor<IEvent, Object> processor = new SetValueEventValueProcessor<>(e->e);
        assertEquals(Map.of("a", 1, "b", 2), processor.apply("anything", EventProcessorFixture.valueEvent1).get());
        assertEquals(Map.of("a", 2, "b", 3), processor.apply("anything", EventProcessorFixture.valueEvent2).get());
    }
}