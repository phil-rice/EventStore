package one.xingyi.store;

import one.xingyi.events.utils.AsyncHelper;
import one.xingyi.events.utils.JsonHelper;
import one.xingyi.events.utils.StringHelper;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class IdAndValueFileStore implements IIdAndValueStore {
    private Executor executor;
    public final Function<String, String> idToFileBaseName;
    private String metadataExtension;

    public static String defaultMetadataExtension = "metadata";

    public static IdAndValueFileStore store(Executor executor, String dir, String separator, int... pattern) {
        return new IdAndValueFileStore(executor, StringHelper.asFileNoExtension(dir, separator, pattern), defaultMetadataExtension);
    }

    public IdAndValueFileStore(Executor executor, Function<String, String> idToFileBaseName, String metadataExtension) {
        this.executor = executor;
        this.idToFileBaseName = idToFileBaseName;
        this.metadataExtension = metadataExtension;
    }

    @Override
    public CompletableFuture<ValueAndMetadata> get(String id) {
        return AsyncHelper.wrapSupplier(executor, () -> {
            String root = idToFileBaseName.apply(id);
            var metadataString = Files.readString(Path.of(root + "." + metadataExtension));
            var metadata = JsonHelper.mapper.readValue(metadataString, Metadata.class);
            var bytes = Files.readAllBytes(Path.of(root + "." + metadata.extension()));
            return new ValueAndMetadata(metadata, bytes);
        });
    }

    @Override
    public CompletableFuture<Void> put(String id, ValueAndMetadata valueAndMetadata) {
        return AsyncHelper.<Exception>wrapRunnable(executor, () -> {
            String root = idToFileBaseName.apply(id);
            Files.writeString(Path.of(root + "." + metadataExtension), JsonHelper.mapper.writeValueAsString(valueAndMetadata.metadata()));
            Files.write(Path.of(root + "." + valueAndMetadata.metadata().extension()), valueAndMetadata.value());
        });
    }
}
