package one.xingyi.store;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class IdAndValueMemoryStore implements IIdAndValueStore {
    private Map<String, ValueAndMetadata> map = new HashMap<>();

    @Override
    public CompletableFuture<ValueAndMetadata> get(String id) {
        return CompletableFuture.completedFuture(map.get(id));
    }

    @Override
    public CompletableFuture<Void> put(String id, ValueAndMetadata valueAndMetadata) {
        map.put(id, valueAndMetadata);
        return CompletableFuture.completedFuture(null);
    }
}
