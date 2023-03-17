package one.xingyi.eventStore;

import one.xingyi.events.IEventParserPrinter;
import one.xingyi.events.utils.FilesHelper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.function.BiFunction;

import static org.junit.jupiter.api.Assertions.*;

class FileEventStoreTest extends AbstractEventStoreTest {

    public final static String dir = makeTempDir();

    protected FileEventStoreTest() {
        super(new FileEventStore(FileEventStore.nameAndNameSpaceToFileName(dir), IEventParserPrinter.iso));
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


}