package one.xingyi.eventStore;

import one.xingyi.events.EventAndAudit;
import one.xingyi.events.IEventParserPrinter;
import org.junit.jupiter.api.Test;

import static one.xingyi.eventFixture.EventProcessorFixture.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

class IEventParserPrinterTest {
    EventAndAudit roundTrip(EventAndAudit event) {
        return IEventParserPrinter.iso.to(IEventParserPrinter.iso.from(event));
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