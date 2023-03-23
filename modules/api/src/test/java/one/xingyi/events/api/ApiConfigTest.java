package one.xingyi.events.api;

import one.xingyi.events.eventStore.jsonfile.JsonFileEventStore;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;

class ApiConfigTest {

    @Test
    public void testStore() {
        ApiConfig config = new ApiConfig();
        JsonFileEventStore store = (JsonFileEventStore) config.store("theFileStoreDir");
        assertEquals(JsonFileEventStore.class, store.getClass());
        assertEquals("theFileStoreDir/ns/82/a3/53/name.dat", store.nameAndNameSpaceToFileName.apply("ns", "name").replaceAll("\\\\", "/"));
        assertEquals("anonymous", config.who().who(new HttpHeaders()));
    }
}