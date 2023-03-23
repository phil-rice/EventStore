package one.xingyi.events.eventStore;

import one.xingyi.events.eventStore.jsonfile.JsonFileEventStore;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileStoreTestUniqueNames extends AbstractJsonFileEventStoreTest {

    static String dir = makeTempDir("UniqueName");
    protected FileStoreTestUniqueNames() throws IOException {
        super(dir,new JsonFileEventStore(executor, JsonFileEventStore.nameAndNameSpaceToUniqueFileName(dir, File.separator, 2, 2, 2), JsonFileEventStore.defaultIso));
    }

    @Test
    public void testNameAndNameSpaceToUniqueFileName() {
        assertEquals("path/of/root/dir/ns/8/2a/353/name.dat", JsonFileEventStore.nameAndNameSpaceToUniqueFileName("path/of/root/dir", "/", 1, 2, 3).apply("ns", "name"));
    }

}
