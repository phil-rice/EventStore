package one.xingyi.events.eventProcessor.value;

import one.xingyi.events.eventFixture.EventProcessorFixture;
import one.xingyi.events.eventProcessor.IEventProcessor;
import one.xingyi.events.eventProcessor.IEventTc;
import one.xingyi.events.events.IEvent;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static one.xingyi.events.eventFixture.EventProcessorFixture.idToValueForTest;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SetIdEventValueProcessorTest {

    @Test
    public void testSetIdEventValueProcessor() throws ExecutionException, InterruptedException {
        IEventProcessor<IEvent, Object> processor = new SetIdEventValueProcessor<>(idToValueForTest, IEventTc.jsonByteArrayParser);
        assertEquals(Map.of("a", 3, "b", 4), processor.apply("anything", EventProcessorFixture.idEvent3).get());
    }
}