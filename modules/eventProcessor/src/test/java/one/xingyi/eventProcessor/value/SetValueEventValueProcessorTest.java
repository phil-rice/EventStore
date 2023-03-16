package one.xingyi.eventProcessor.value;

import one.xingyi.eventProcessor.IEventProcessor;
import one.xingyi.eventProcessor.EventProcessorFixture;
import one.xingyi.events.utils.JsonHelper;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class SetValueEventValueProcessorTest {
    @Test
    public void testSetValueEventValueProcessor() throws ExecutionException, InterruptedException {
        IEventProcessor<Object> processor = new SetValueEventValueProcessor<>(JsonHelper.toJsonParser);
        assertEquals(Map.of("a", 1, "b", 2), processor.apply("anything", EventProcessorFixture.valueEvent1).get());
        assertEquals(Map.of("a", 2, "b", 3), processor.apply("anything", EventProcessorFixture.valueEvent2).get());
    }
}