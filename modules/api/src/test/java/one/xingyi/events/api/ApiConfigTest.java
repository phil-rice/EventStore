package one.xingyi.events.api;

import one.xingyi.eventStore.FileEventStore;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;

class ApiConfigTest {

    @Test
    public void testStore() {
        ApiConfig config = new ApiConfig();
        FileEventStore store = (FileEventStore) config.store("theFileStoreDir");
        assertEquals(FileEventStore.class, store.getClass());
        assertEquals("theFileStoreDir/ns/82/a3/53/name.dat", store.nameAndNameSpaceToFileName.apply("ns", "name").replaceAll("\\\\", "/"));
        assertEquals("anonymous", config.who().who(new HttpHeaders()));
    }
}