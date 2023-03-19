package one.xingyi.events.eventStore;

public class MapEventStoreTest extends AbstractEventStoreTest {

    public MapEventStoreTest() {
        super(new MapEventStore());
    }
}
