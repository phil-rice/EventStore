package one.xingyi.events.eventStore;

import one.xingyi.events.eventStore.jsonfile.JsonFileEventStore;
import one.xingyi.events.fileStore.INameSpaceAndNameToFileName;
import one.xingyi.events.fileStore.NameSpaceAndNameConfig;

import java.io.File;
import java.io.IOException;

public class FileStoreTestUniqueNames extends AbstractJsonFileEventStoreTest {

    static String dir = makeTempDir("UniqueName");
    protected FileStoreTestUniqueNames() throws IOException {
        super(dir,new JsonFileEventStore(executor, INameSpaceAndNameToFileName.uniqueFile(new NameSpaceAndNameConfig(dir, File.separator,"dat", new int[]{ 2, 2, 2})), JsonFileEventStore.defaultIso));
    }


}
