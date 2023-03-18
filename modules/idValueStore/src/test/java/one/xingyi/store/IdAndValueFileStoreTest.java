package one.xingyi.store;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import static one.xingyi.events.utils.WrappedException.wrapValue;

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
            return IdAndValueFileStore.store(executor, dir.getAbsolutePath(), File.separator, 2, 2);
        });
    }
}
