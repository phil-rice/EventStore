package one.xingyi.events.eventStore;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileStoreTestUniqueNames extends AbstractFileEventStoreTest {

    static String dir = makeTempDir("UniqueName");
    protected FileStoreTestUniqueNames() throws IOException {
        super(dir,new FileEventStore(executor, FileEventStore.nameAndNameSpaceToUniqueFileName(dir, File.separator, 2, 2, 2), FileEventStore.defaultIso));
    }

    @Test
    public void testNameAndNameSpaceToUniqueFileName() {
        assertEquals("path/of/root/dir/ns/8/2a/353/name.dat", FileEventStore.nameAndNameSpaceToUniqueFileName("path/of/root/dir", "/", 1, 2, 3).apply("ns", "name"));
    }

}
