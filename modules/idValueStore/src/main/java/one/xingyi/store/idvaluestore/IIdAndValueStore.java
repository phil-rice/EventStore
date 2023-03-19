package one.xingyi.store.idvaluestore;

import java.util.concurrent.CompletableFuture;

public interface IIdAndValueStore {
    /**
     * Returns the value and metadata. Throws exception if the id is not the hash of the value
     */
    CompletableFuture<ValueAndMetadata> get(String id);

    /**
     * Returns the id - which is calculated as a hash of the value
     */
    CompletableFuture<PutResult> put(ValueAndMetadata valueAndMetadata);
}
