package one.xingyi.eventStore;

import one.xingyi.events.IEventParserPrinter;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileEventStoreTest extends AbstractEventStoreTest {

    public final static String dir = makeTempDir();

    protected FileEventStoreTest() {
        super(new FileEventStore(FileEventStore.nameAndNameSpaceToFileName(dir, File.separator, 2, 2, 2), IEventParserPrinter.iso));
    }

    private static String makeTempDir() {
        try {
            return File.createTempFile("test", "fileEventStore").getAbsolutePath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeAll
    static void cleanDirectory() throws IOException {
        FileSystemUtils.deleteRecursively(new File(dir));
    }

    @Test
    public void testNameAndNameSpaceToFileName() {
        assertEquals("path/of/root/dir/ns/8/2a/353/name.dat", FileEventStore.nameAndNameSpaceToFileName("path/of/root/dir", "/", 1, 2, 3).apply("ns", "name"));
    }


}