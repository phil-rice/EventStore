package one.xingyi.eventProcessor.audit;

import one.xingyi.eventFixture.EventProcessorFixture;
import one.xingyi.eventProcessor.IEventProcessor;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EventAuditProcessorTest {

    @Test
    public void testEventAuditProcessorOnlySinceLastSource() throws ExecutionException, InterruptedException {
        var actual = IEventProcessor.evaluate(IEventProcessor.auditEventProcessor(), e -> e.payload().isSource(), EventProcessorFixture.evA01234, List.of());
        assertEquals(EventProcessorFixture.audit01234.subList(3,5), actual.get());
    }

    @Test
    public void testEventAuditProcessor() throws ExecutionException, InterruptedException {
        var actual = IEventProcessor.evaluate(IEventProcessor.auditEventProcessor(), e -> false, EventProcessorFixture.evA01234, List.of());
        assertEquals(EventProcessorFixture.audit01234, actual.get());
    }

}