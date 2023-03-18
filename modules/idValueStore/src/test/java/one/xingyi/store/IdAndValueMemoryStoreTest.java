package one.xingyi.store;

import java.util.function.Supplier;

public class IdAndValueMemoryStoreTest extends AbstractIdAndValueStoreTest {
    protected IdAndValueMemoryStoreTest() {
        super(() -> new IdAndValueMemoryStore());
    }
}
