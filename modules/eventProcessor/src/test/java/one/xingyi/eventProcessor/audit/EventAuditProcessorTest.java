package one.xingyi.eventProcessor.audit;

import one.xingyi.eventProcessor.EventProcessorFixture;
import one.xingyi.eventProcessor.IEventProcessor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class EventAuditProcessorTest {

    @Test
    public void testEventAuditProcessor() throws ExecutionException, InterruptedException {
        var actual = IEventProcessor.evaluate(IEventProcessor.auditEventProcessor(), List.of(
                EventProcessorFixture.zeroEvent,
                EventProcessorFixture.valueEvent1,
                EventProcessorFixture.valueEvent2,
                EventProcessorFixture.idEvent3,
                EventProcessorFixture.lensEvent4
        ), List.of());
        assertEquals(EventProcessorFixture.audit01234, actual.get());
    }

}