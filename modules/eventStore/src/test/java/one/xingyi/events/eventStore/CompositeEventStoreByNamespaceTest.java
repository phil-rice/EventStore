package one.xingyi.events.eventStore;

import one.xingyi.events.utils.functions.PartialAnd;
import org.junit.jupiter.api.Test;

import java.util.List;

import static one.xingyi.events.eventFixture.EventProcessorFixture.*;
import static org.junit.jupiter.api.Assertions.*;

class CompositeEventStoreByNamespaceTest {

    @Test
    public void testCompose() {
        MapEventStore mem1 = new MapEventStore();
        MapEventStore mem2 = new MapEventStore();
        MapEventStore mem3 = new MapEventStore();

        IEventStore composite = IEventStore.compose(mem1, PartialAnd.match("ns2", mem2), PartialAnd.match("ns3", mem3));
        composite.appendEvent("ns1", "name", evA0).join();
        assertEquals(List.of(evA0), mem1.getEvents("ns1", "name").join());

        composite.appendEvent("ns2", "name", evA1).join();
        assertEquals(List.of(evA1), mem2.getEvents("ns2", "name").join());

        composite.appendEvent("ns3", "name", evA3).join();
        assertEquals(List.of(evA3), mem3.getEvents("ns3", "name").join());

        assertEquals(1, mem1.getCopyOfMap().size());
        assertEquals(1, mem2.getCopyOfMap().size());
        assertEquals(1, mem3.getCopyOfMap().size());
    }

}