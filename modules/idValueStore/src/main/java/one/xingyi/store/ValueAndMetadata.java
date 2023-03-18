package one.xingyi.store;

import one.xingyi.audit.Audit;

public record ValueAndMetadata(Metadata metadata,byte[] value) {
}
