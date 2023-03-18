package one.xingyi.eventAndAudit;

import one.xingyi.audit.AndAudit;
import one.xingyi.audit.AuditIso;
import one.xingyi.events.IEvent;
import one.xingyi.optics.iso.IIso;
import org.junit.jupiter.api.Test;

import static one.xingyi.eventFixture.EventProcessorFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AuditIsoTest {

    IIso<String, AndAudit<IEvent>> iso = AuditIso.iso(IIso.jsonIso(IEvent.class));

    AndAudit<IEvent> roundTrip(AndAudit<IEvent> event) {
        return iso.to(iso.from(event));
    }

    @Test
    public void testRoundTrip() {
        assertEquals(evA0, roundTrip(evA0));
        assertEquals(evA1, roundTrip(evA1));
        assertEquals(evA2, roundTrip(evA2));
        assertEquals(evA3, roundTrip(evA3));
        assertEquals(evA4, roundTrip(evA4));
    }

}