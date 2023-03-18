package one.xingyi.store;

import one.xingyi.events.utils.*;
import one.xingyi.optics.iso.IIso;
import one.xingyi.signedData.SignedData;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;

public class IdAndValueFileStore implements IIdAndValueStore {
    private Executor executor;
    private IIso<String, Metadata> metadataIIso;
    public final Function<String, String> idToFileBaseName;
    private String metadataExtension;

    private final IHash hash;

    public static String defaultMetadataExtension = "metadata";

    public static IdAndValueFileStore store(Executor executor, String secret, String dir, String separator, int... pattern) {
        return new IdAndValueFileStore(executor, IHash.sha256, SignedData.iso(secret, Metadata.class), StringHelper.asFileNoExtension(dir, separator, pattern), defaultMetadataExtension);
    }

    public IdAndValueFileStore(Executor executor, IHash hash, IIso<String, Metadata> metadataIIso, Function<String, String> idToFileBaseName, String metadataExtension) {
        this.executor = executor;
        this.hash = hash;
        this.metadataIIso = metadataIIso;
        this.idToFileBaseName = idToFileBaseName;
        this.metadataExtension = metadataExtension;
    }

    @Override
    public CompletableFuture<ValueAndMetadata> get(String id) {
        return AsyncHelper.wrapSupplier(executor, () -> {
            String root = idToFileBaseName.apply(id);
            try {
                var metadataString = Files.readString(Path.of(root + "." + metadataExtension));
                var metadata = metadataIIso.to(metadataString);
                var bytes = Files.readAllBytes(Path.of(root + "." + metadata.extension()));
                var actualHash = hash.hash(bytes);
                if (actualHash.equals(id))
                    return new ValueAndMetadata(bytes, metadata);
                throw new HashMismatchException(id, actualHash);

            } catch (NoSuchFileException e) {
                throw new NotFoundException();
            }
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
                var metadata = metadataIIso.to(metadataString);
                return new PutResult(id, Optional.empty(), Optional.of(metadata));
            } catch (IOException e) {
                File file = metaDataPath.getParent().toFile();
                var create = file.mkdirs();
                String metaDataString = metadataIIso.from(valueAndMetadata.metadata());
                Files.writeString(metaDataPath, metaDataString, StandardOpenOption.CREATE_NEW);
                Files.write(Path.of(root + "." + valueAndMetadata.metadata().extension()), valueAndMetadata.value());
                return new PutResult(id, Optional.of(valueAndMetadata.metadata()), Optional.empty());
            }
        });
    }
}
