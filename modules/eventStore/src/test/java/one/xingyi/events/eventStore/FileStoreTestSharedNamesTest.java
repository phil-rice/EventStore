package one.xingyi.events.eventStore;

import one.xingyi.events.eventStore.jsonfile.JsonFileEventStore;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileStoreTestSharedNamesTest extends AbstractJsonFileEventStoreTest {
    static String dir = makeTempDir("SharedName");

    protected FileStoreTestSharedNamesTest() throws IOException {
        super(dir, new JsonFileEventStore(executor, JsonFileEventStore.nameAndNameSpaceToUniqueFileName(dir, File.separator, 2, 2, 2), JsonFileEventStore.defaultIso));
    }

    @Test
    public void testNameAndNameSpaceToSharedFilename() {
        assertEquals("path/of/root/dir/ns/8/2a/353.dat", JsonFileEventStore.nameAndNameSpaceToSharedFileName("path/of/root/dir", "/", 1, 2, 3).apply("ns", "name"));

    }

}
