package one.xingyi.eventStore;

import one.xingyi.events.utils.AsyncHelper;
import one.xingyi.events.utils.ListHelper;
import one.xingyi.events.utils.StringHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static one.xingyi.eventFixture.EventProcessorFixture.evA01234;
import static one.xingyi.events.utils.StringHelper.to1Quote;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileEventStoreTest extends AbstractEventStoreTest {

    public final static String dir = makeTempDir();


    public static ExecutorService executor = Executors.newSingleThreadExecutor();

    @AfterAll
    public static void shutDownExecutor() {
        executor.shutdown();
    }

    protected FileEventStoreTest() {
        super(new FileEventStore(executor, FileEventStore.nameAndNameSpaceToFileName(dir, File.separator, 2, 2, 2), FileEventStore.defaultIso));
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

    List<String> savedContent(String nameSpace, String name) throws IOException {
        FileEventStore fileEventStore = (FileEventStore) eventStore;
        var file = fileEventStore.nameAndNameSpaceToFileName.apply(nameSpace, name);
        return ListHelper.map(Files.readAllLines(Paths.get(file)), StringHelper::to1Quote);
    }

    @Test
    public void testSerialisationOfEvents() throws IOException {
        String name = "name3";
        AsyncHelper.forEach(evA01234, e -> eventStore.appendEvent(nameSpace, name, e)).join();
        var lines = savedContent(nameSpace, name);
        assertEquals(to1Quote(FileEventStore.defaultIso.from(evA01234.get(0))), lines.get(0));
        assertEquals(to1Quote(FileEventStore.defaultIso.from(evA01234.get(1))), lines.get(1));
        assertEquals(to1Quote(FileEventStore.defaultIso.from(evA01234.get(2))), lines.get(2));
        assertEquals(to1Quote(FileEventStore.defaultIso.from(evA01234.get(3))), lines.get(3));
        assertEquals(to1Quote(FileEventStore.defaultIso.from(evA01234.get(4))), lines.get(4));
        assertEquals(5, lines.size());

    }


}