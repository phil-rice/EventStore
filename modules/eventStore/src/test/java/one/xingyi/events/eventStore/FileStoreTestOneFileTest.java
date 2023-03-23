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


}
