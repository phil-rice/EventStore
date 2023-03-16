package one.xingyi.eventProcessor.value;

import one.xingyi.eventProcessor.IEventProcessor;
import one.xingyi.eventProcessor.EventProcessorFixture;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static one.xingyi.eventProcessor.EventProcessorFixture.idToValue;
import static org.junit.jupiter.api.Assertions.*;

class SetIdEventValueProcessorTest {

    @Test
    public void testSetIdEventValueProcessor() throws ExecutionException, InterruptedException {
        IEventProcessor<Object> processor = new SetIdEventValueProcessor<>(idToValue);
        assertEquals(Map.of("a", 3, "b", 4), processor.apply("anything", EventProcessorFixture.idEvent3).get());
    }
}