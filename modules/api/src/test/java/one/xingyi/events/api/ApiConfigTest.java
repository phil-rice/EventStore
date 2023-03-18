package one.xingyi.events.api;

import one.xingyi.eventStore.FileEventStore;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

import static org.junit.jupiter.api.Assertions.*;

class ApiConfigTest {

    @Test
    public void testStore() {
        ApiConfig config = new ApiConfig();
        assertEquals(FileEventStore.class, config.store().getClass());
        FileEventStore store = (FileEventStore) config.store();
        assertEquals("fileStore/ns/82/a3/53/name.dat", store.nameAndNameSpaceToFileName.apply("ns", "name").replaceAll("\\\\", "/"));
        assertEquals("anonymous", config.who().who(new HttpHeaders()));
    }
}