package one.xingyi.events.eventStore;

import one.xingyi.events.eventStore.jsonfile.JsonFileEventStore;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileStoreTestOneFileTest extends AbstractJsonFileEventStoreTest {

    static String dir = makeTempDir("UniqueName");

    protected FileStoreTestOneFileTest() throws IOException {
        super(dir, new JsonFileEventStore(executor, (ns, n) -> dir + "/" + ns + "/data.dat", JsonFileEventStore.defaultIso));
    }

    @Test
    public void testNameAndNameSpaceToUniqueFileName() {
        assertEquals("path/of/root/dir/ns/name.dat", JsonFileEventStore.nameAndNameSpaceToUniqueFileName("path/of/root/dir", "/").apply("ns", "name"));
    }

}
