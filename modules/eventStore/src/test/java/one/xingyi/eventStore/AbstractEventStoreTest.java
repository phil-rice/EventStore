package one.xingyi.eventStore;

import org.junit.jupiter.api.Test;

import static one.xingyi.eventFixture.EventProcessorFixture.evA01234;
import static org.junit.jupiter.api.Assertions.assertEquals;

public abstract class AbstractEventStoreTest {

    public final IEventStore eventStore;


    protected AbstractEventStoreTest(IEventStore eventStore) {
        this.eventStore = eventStore;
    }

    String nameSpace = "nameSpace";

    @Test
    public void testRoundTrip() {
        String name = "name1";
        evA01234.forEach(e -> eventStore.appendEvent(nameSpace, name, e).join());
        assertEquals(evA01234, eventStore.getEvents(nameSpace, name).join());
        assertEquals(evA01234, eventStore.getEvents(nameSpace, name).join());
    }

    @Test
    public void testRoundTripDifferentName() {
        String name = "name2";
        evA01234.forEach(e -> eventStore.appendEvent(nameSpace, name, e).join());
        assertEquals(evA01234, eventStore.getEvents(nameSpace, name).join());
        assertEquals(evA01234, eventStore.getEvents(nameSpace, name).join());
    }
}