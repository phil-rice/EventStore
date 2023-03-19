package one.xingyi.idvaluestore;

import one.xingyi.store.idvaluestore.IdAndValueMemoryStore;

public class IdAndValueMemoryStoreTest extends AbstractIdAndValueStoreTest {
    protected IdAndValueMemoryStoreTest() {
        super(() -> new IdAndValueMemoryStore());
    }
}
