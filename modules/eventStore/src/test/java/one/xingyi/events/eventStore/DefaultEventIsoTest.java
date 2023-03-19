package one.xingyi.events.eventStore;

import one.xingyi.audit.AndAudit;
import one.xingyi.events.IEvent;
import one.xingyi.events.optics.iso.IIso;
import org.junit.jupiter.api.Test;

import static one.xingyi.events.eventFixture.EventProcessorFixture.*;
import static one.xingyi.events.utils.helpers.StringHelper.to1Quote;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DefaultEventIsoTest {

    IIso<String, AndAudit<IEvent>> iso = FileEventStore.defaultIso;

    Object roundTrip(AndAudit<IEvent> o) {
        return iso.to(iso.from(o));
    }

    @Test
    public void testRoundTrips() {
        assertEquals(evA0, roundTrip(evA0));
        assertEquals(evA1, roundTrip(evA1));
        assertEquals(evA2, roundTrip(evA2));
        assertEquals(evA3, roundTrip(evA3));
        assertEquals(evA4, roundTrip(evA4));
    }

    @Test
    public void testSerialisation() {
        assertEquals("{'who':'who0','when':0,'what':'when0'}\t{'type':'zero'}", to1Quote(iso.from(evA0)));
        assertEquals("{'who':'who1','when':1,'what':'when1'}\t{'type':'value','value':{'a':1,'b':2}}", to1Quote(iso.from(evA1)));
        assertEquals("{'who':'who2','when':2,'what':'when2'}\t{'type':'value','value':{'a':2,'b':3}}", to1Quote(iso.from(evA2)));
        assertEquals("{'who':'who3','when':3,'what':'when3'}\t{'type':'id','id':'id3','parser':'json'}", to1Quote(iso.from(evA3)));
        assertEquals("{'who':'who4','when':4,'what':'when4'}\t{'type':'lens','lens':'a','value':44}", to1Quote(iso.from(evA4)));
    }
}
