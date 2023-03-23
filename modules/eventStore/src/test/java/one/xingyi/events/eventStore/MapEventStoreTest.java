package one.xingyi.events.eventStore;

import one.xingyi.events.eventStore.store.MapEventStore;

public class MapEventStoreTest extends AbstractEventStoreTest {

    public MapEventStoreTest() {
        super(new MapEventStore());
    }
}
