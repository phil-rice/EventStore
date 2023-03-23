package one.xingyi.events.eventStore;

import one.xingyi.events.eventStore.jsonfile.JsonFileEventStore;
import one.xingyi.events.utils.helpers.AsyncHelper;
import one.xingyi.events.utils.helpers.ListHelper;
import one.xingyi.events.utils.helpers.StringHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static one.xingyi.events.eventFixture.EventProcessorFixture.evA01234;
import static one.xingyi.events.eventFixture.EventProcessorFixture.evVIA01234;
import static one.xingyi.events.utils.exceptions.WrappedException.wrapValue;
import static one.xingyi.events.utils.helpers.StringHelper.to1Quote;
import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class AbstractJsonFileEventStoreTest extends AbstractEventStoreTest<JsonFileEventStore> {

    public final String dir;


    static ExecutorService executor = Executors.newSingleThreadExecutor();


    static String makeTempDir(String key) {
        String dir = wrapValue(() -> File.createTempFile("fileStoreTest", key).getAbsolutePath());
        FileSystemUtils.deleteRecursively(new File(dir));
        return dir;
    }

    @BeforeEach
    public void clear(){
        FileSystemUtils.deleteRecursively(new File(dir));
    }

    protected AbstractJsonFileEventStoreTest(String dir, JsonFileEventStore store) throws IOException {
        super(store);
        this.dir = dir;
    }


    List<String> savedContent(String nameSpace, String name) throws IOException {

        var file = eventStore.nameAndNameSpaceToFileName.apply(nameSpace, name);
        return ListHelper.map(Files.readAllLines(Paths.get(file)), StringHelper::to1Quote);
    }

    @Test
    public void testSerialisationOfEvents() throws IOException {

        String name = "name";
        AsyncHelper.forEach(evA01234, e -> eventStore.appendEvent(nameSpace, name, e)).join();
        var lines = savedContent(nameSpace, name);
        assertEquals(to1Quote(JsonFileEventStore.defaultIso.from(evVIA01234.get(0))), lines.get(0));
        assertEquals(to1Quote(JsonFileEventStore.defaultIso.from(evVIA01234.get(1))), lines.get(1));
        assertEquals(to1Quote(JsonFileEventStore.defaultIso.from(evVIA01234.get(2))), lines.get(2));
        assertEquals(to1Quote(JsonFileEventStore.defaultIso.from(evVIA01234.get(3))), lines.get(3));
        assertEquals(to1Quote(JsonFileEventStore.defaultIso.from(evVIA01234.get(4))), lines.get(4));
        assertEquals(5, lines.size());
    }

}