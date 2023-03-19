package one.xingyi.eventProcessor.value;

import one.xingyi.eventFixture.EventProcessorFixture;
import one.xingyi.eventProcessor.IEventProcessor;
import one.xingyi.events.IEvent;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static one.xingyi.eventFixture.EventProcessorFixture.idToValueForTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SetIdEventValueProcessorTest {

    @Test
    public void testSetIdEventValueProcessor() throws ExecutionException, InterruptedException {
        IEventProcessor<IEvent, Object> processor = new SetIdEventValueProcessor<>(idToValueForTest);
        assertEquals(Map.of("a", 3, "b", 4), processor.apply("anything", EventProcessorFixture.idEvent3).get());
    }
}