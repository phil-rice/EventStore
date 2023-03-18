package one.xingyi.store;

import one.xingyi.audit.Audit;

public record Metadata(String extension, String mimeType, Audit audit) {
}
