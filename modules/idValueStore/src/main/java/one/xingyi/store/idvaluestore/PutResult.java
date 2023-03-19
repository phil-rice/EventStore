package one.xingyi.store.idvaluestore;

import java.util.Optional;

/**
 * Only one of the metadata or existing metadata will be present
 */
public record PutResult(String id, Optional<Metadata> metadata, Optional<Metadata> existingMetadata) {
}
