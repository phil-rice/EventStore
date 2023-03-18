package one.xingyi.store;

import java.util.Optional;

public record PutResult (String id, Optional<Metadata> existingMetadata){
}
