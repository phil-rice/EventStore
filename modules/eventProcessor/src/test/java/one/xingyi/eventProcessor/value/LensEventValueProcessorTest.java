package one.xingyi.eventProcessor.value;

import one.xingyi.eventFixture.EventProcessorFixture;
import one.xingyi.events.utils.JsonHelper;
import one.xingyi.optics.lens.ILensTC;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LensEventValueProcessorTest {

    @Test
    public void testLensEventValueProcessor() throws ExecutionException, InterruptedException {
        LensEventValueProcessor<Object> processor = new LensEventValueProcessor<>(ILensTC.jsonLensTc, e -> e);
        assertEquals(Map.of("a", 44), processor.apply(Map.of("a", 1), EventProcessorFixture.lensEvent4).get());
    }
}