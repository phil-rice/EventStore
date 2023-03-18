package one.xingyi.store;

import one.xingyi.events.utils.AsyncHelper;
import one.xingyi.events.utils.JsonHelper;
import one.xingyi.events.utils.StringHelper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class IdAndValueFileStore implements IIdAndValueStore {
    private Executor executor;
    public final Function<String, String> idToFileBaseName;
    private String metadataExtension;

    private final IHash hash;

    public static String defaultMetadataExtension = "metadata";

    public static IdAndValueFileStore store(Executor executor, String dir, String separator, int... pattern) {
        return new IdAndValueFileStore(executor, IHash.sha256, StringHelper.asFileNoExtension(dir, separator, pattern), defaultMetadataExtension);
    }

    public IdAndValueFileStore(Executor executor, IHash hash, Function<String, String> idToFileBaseName, String metadataExtension) {
        this.executor = executor;
        this.hash = hash;
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
            return new ValueAndMetadata(bytes, metadata);
        });
    }

    @Override
    public CompletableFuture<PutResult> put(ValueAndMetadata valueAndMetadata) {
        return AsyncHelper.wrapSupplier(executor, () -> {
            String id = hash.hash(valueAndMetadata.value());
            String root = idToFileBaseName.apply(id);
            Path metaDataPath = Path.of(root + "." + metadataExtension);
            try {
                var metadataString = Files.readString(metaDataPath);
                var metadata = JsonHelper.mapper.readValue(metadataString, Metadata.class);
                return new PutResult(id, Optional.of(metadata));
            } catch (IOException e) {
                File file = metaDataPath.getParent().toFile();
                var create = file.mkdirs();
                String metaDataString = JsonHelper.mapper.writeValueAsString(valueAndMetadata.metadata());
                Files.writeString(metaDataPath, metaDataString, StandardOpenOption.CREATE_NEW);
                Files.write(Path.of(root + "." + valueAndMetadata.metadata().extension()), valueAndMetadata.value());
                return new PutResult(id, Optional.empty());
            }
        });
    }
}
