package one.xingyi.eventStore;

public class MapEventStoreTest extends AbstractEventStoreTest {

    public MapEventStoreTest() {
        super(new MapEventStore());
    }
}
