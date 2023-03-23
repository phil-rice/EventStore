package one.xingyi.events.eventStore;

import one.xingyi.events.eventStore.jsonfile.JsonFileEventStore;
import one.xingyi.events.fileStore.INameSpaceAndNameToFileName;
import one.xingyi.events.fileStore.NameSpaceAndNameConfig;

import java.io.File;
import java.io.IOException;

public class FileStoreTestSharedNamesTest extends AbstractJsonFileEventStoreTest {
    static String dir = makeTempDir("SharedName");

    protected FileStoreTestSharedNamesTest() throws IOException {
        super(dir, new JsonFileEventStore(executor, INameSpaceAndNameToFileName.sharedFile(new NameSpaceAndNameConfig(dir, File.separator, "dat", new int[]{2, 2, 2})), JsonFileEventStore.defaultIso));
    }


}
