package one.xingyi.eventStore;

import one.xingyi.events.utils.AsyncHelper;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

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
        AsyncHelper.forEach(evA01234, e -> eventStore.appendEvent(nameSpace, name, e)).join();
        assertEquals(evA01234, eventStore.getEvents(nameSpace, name).join());
        assertEquals(evA01234, eventStore.getEvents(nameSpace, name).join());
    }

    @Test
    public void testRoundTripDifferentName() {
        String name = "name2";
        AsyncHelper.forEach(evA01234, e -> eventStore.appendEvent(nameSpace, name, e)).join();
        assertEquals(evA01234, eventStore.getEvents(nameSpace, name).join());
        assertEquals(evA01234, eventStore.getEvents(nameSpace, name).join());
    }

    @Test
    public void testGetAll() {
        String ns1 = "ns1";
        String ns2 = "ns2";
        String name1 = "name1";
        String name2 = "name2";
        AsyncHelper.forEach(evA01234, e -> eventStore.appendEvent(ns1, name1, e)).join();
        AsyncHelper.forEach(evA01234, e -> eventStore.appendEvent(ns1, name2, e)).join();
        AsyncHelper.forEach(evA01234, e -> eventStore.appendEvent(ns2, name1, e)).join();
        AsyncHelper.forEach(evA01234, e -> eventStore.appendEvent(ns2, name2, e)).join();
        assertEquals(Map.of(
                        name1, Map.of(ns1, evA01234, ns2, evA01234),
                        name2, Map.of(ns1, evA01234, ns2, evA01234)),
                IEventStore.getAll(eventStore, List.of(ns1, ns2), List.of(name1, name2)).join());


    }
}