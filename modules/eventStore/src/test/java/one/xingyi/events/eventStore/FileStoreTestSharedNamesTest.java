package one.xingyi.events.eventStore;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileStoreTestSharedNamesTest extends AbstractFileEventStoreTest {
    static String dir = makeTempDir("SharedName");

    protected FileStoreTestSharedNamesTest() throws IOException {
        super(dir, new FileEventStore(executor, FileEventStore.nameAndNameSpaceToUniqueFileName(dir, File.separator, 2, 2, 2), FileEventStore.defaultIso));
    }

    @Test
    public void testNameAndNameSpaceToSharedFilename() {
        assertEquals("path/of/root/dir/ns/8/2a/353.dat", FileEventStore.nameAndNameSpaceToSharedFileName("path/of/root/dir", "/", 1, 2, 3).apply("ns", "name"));

    }

}
