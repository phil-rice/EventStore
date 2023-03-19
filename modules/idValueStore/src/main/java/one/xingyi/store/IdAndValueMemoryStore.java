package one.xingyi.store;

import one.xingyi.events.utils.exceptions.NotFoundException;
import one.xingyi.events.utils.services.IHash;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class IdAndValueMemoryStore implements IIdAndValueStore {
    private Map<String, ValueAndMetadata> map = new HashMap<>();

    private IHash hash = IHash.sha256;

    @Override
    public CompletableFuture<ValueAndMetadata> get(String id) {
        ValueAndMetadata value = map.get(id);
        return value == null
                ? CompletableFuture.failedFuture(new NotFoundException())
                : CompletableFuture.completedFuture(value);
    }

    @Override
    public CompletableFuture<PutResult> put(ValueAndMetadata valueAndMetadata) {
        String id = hash.hash(valueAndMetadata.value());
        if (map.containsKey(id))
            return CompletableFuture.completedFuture(new PutResult(id, Optional.empty(), Optional.of(map.get(id).metadata())));
        map.put(id, valueAndMetadata);
        return CompletableFuture.completedFuture(new PutResult(id, Optional.of(valueAndMetadata.metadata()), Optional.empty()));
    }
}
