package one.xingyi.store;

import java.util.concurrent.CompletableFuture;

public interface IIdAndValueStore {
    CompletableFuture<ValueAndMetadata> get(String id);

    CompletableFuture<Void> put(String id, ValueAndMetadata valueAndMetadata);
}
