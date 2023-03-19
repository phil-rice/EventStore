package one.xingyi.events.eventStore;

import one.xingyi.event.audit.AndVersionIdAndAudit;
import one.xingyi.events.events.IEvent;
import one.xingyi.events.optics.iso.IIso;
import org.junit.jupiter.api.Test;

import static one.xingyi.events.eventFixture.EventProcessorFixture.*;
import static one.xingyi.events.utils.helpers.StringHelper.to1Quote;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultEventIsoTest {

    IIso<String, AndVersionIdAndAudit<IEvent>> iso = FileEventStore.defaultIso;

    Object roundTrip(AndVersionIdAndAudit<IEvent> o) {
        return iso.to(iso.from(o));
    }

    @Test
    public void testRoundTrips() {
        assertEquals(evVIA0, roundTrip(evVIA0));
        assertEquals(evVIA1, roundTrip(evVIA1));
        assertEquals(evVIA2, roundTrip(evVIA2));
        assertEquals(evVIA3, roundTrip(evVIA3));
        assertEquals(evVIA4, roundTrip(evVIA4));
    }

    @Test
    public void testSerialisation() {
        assertEquals("0\tname\t{'who':'who0','when':0,'what':'when0'}\t{'type':'zero'}", to1Quote(iso.from(evVIA0)));
        assertEquals("0\tname\t{'who':'who1','when':1,'what':'when1'}\t{'type':'value','value':{'a':1,'b':2}}", to1Quote(iso.from(evVIA1)));
        assertEquals("0\tname\t{'who':'who2','when':2,'what':'when2'}\t{'type':'value','value':{'a':2,'b':3}}", to1Quote(iso.from(evVIA2)));
        assertEquals("0\tname\t{'who':'who3','when':3,'what':'when3'}\t{'type':'id','id':'id3','parser':'json'}", to1Quote(iso.from(evVIA3)));
        assertEquals("0\tname\t{'who':'who4','when':4,'what':'when4'}\t{'type':'lens','lens':'a','value':44}", to1Quote(iso.from(evVIA4)));
    }
}
