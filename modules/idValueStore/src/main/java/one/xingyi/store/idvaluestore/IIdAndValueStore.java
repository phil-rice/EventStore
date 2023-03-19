package one.xingyi.store.idvaluestore;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static one.xingyi.events.utils.exceptions.WrappedException.wrapFn;

public interface IIdAndValueStore {
    /**
     * Returns the value and metadata. Throws exception if the id is not the hash of the value
     */
    CompletableFuture<ValueAndMetadata> get(String id);

    /**
     * Returns the id - which is calculated as a hash of the value
     */
    CompletableFuture<PutResult> put(ValueAndMetadata valueAndMetadata);

    static Function<String, CompletableFuture<String>> getJsonString(IIdAndValueStore idAndValueStore) {
        var getBytes = getJsonBytes(idAndValueStore);
        return id -> getBytes.apply(id).thenApply(bytes -> new String(bytes, StandardCharsets.UTF_8));
    }


    static Function<String, CompletableFuture<byte[]>> getJsonBytes(IIdAndValueStore idAndValueStore) {
        return id -> {
            return idAndValueStore.get(id).thenApply(wrapFn(vm -> {
                if (vm == null) return null;
                var metadata = vm.metadata();
                if (!metadata.contentType().equals("application/json"))
                    throw new RuntimeException("Cannot handle content type " + metadata.contentType());
                return vm.value();
            }));
        };
    }
}
