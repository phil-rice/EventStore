package one.xingyi.store;

import org.junit.jupiter.api.AfterAll;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static one.xingyi.events.utils.exceptions.WrappedException.wrapValue;

public class IdAndValueFileStoreTest extends AbstractIdAndValueStoreTest {
    public static ExecutorService executor = Executors.newSingleThreadExecutor();

    @AfterAll
    public static void shutDownExecutor() {
        executor.shutdown();
    }


    protected IdAndValueFileStoreTest() {
        super(() -> {
            var dir = wrapValue(() -> File.createTempFile("test", "idAndValueFileStore"));
            dir.delete();
            return IdAndValueFileStore.store(executor, "someSecret", dir.getAbsolutePath(), File.separator, 2, 2);
        });
    }
}
