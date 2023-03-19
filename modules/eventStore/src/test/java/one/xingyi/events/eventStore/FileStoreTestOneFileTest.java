package one.xingyi.events.eventStore;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileStoreTestOneFileTest extends AbstractFileEventStoreTest {

    static String dir = makeTempDir("UniqueName");

    protected FileStoreTestOneFileTest() throws IOException {
        super(dir, new FileEventStore(executor, (ns, n) -> dir + "/" + ns + "/data.dat", FileEventStore.defaultIso));
    }

    @Test
    public void testNameAndNameSpaceToUniqueFileName() {
        assertEquals("path/of/root/dir/ns/name.dat", FileEventStore.nameAndNameSpaceToUniqueFileName("path/of/root/dir", "/").apply("ns", "name"));
    }

}
