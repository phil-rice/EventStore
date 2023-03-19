package one.xingyi.audit;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public record AndVersionIdAndAudit<T>(String version, String id, T payload, Audit audit) {

    public AndAudit<T> toAndAudit() {
        return new AndAudit<>(payload, audit);
    }
    public AndVersionIdAndAudit {
        if (version == null) throw new RuntimeException("Version is null");
        if (id == null) throw new RuntimeException("Id is null");
        if (payload == null) throw new RuntimeException("Payload is null");
        if (audit == null) throw new RuntimeException("Audit is null");
        if (version.contains("\t")) throw new RuntimeException("Version contains a tab: " + version);
        if (id.contains("\t")) throw new RuntimeException("Id contains a tab: " + id);
        if (audit.what().contains("\t")) throw new RuntimeException("Audit what contains a tab: " + audit.what());
        if (audit.who().contains("\t")) throw new RuntimeException("Audit who contains a tab: " + audit.who());
    }

    public <T1> AndVersionIdAndAudit<T1> map(Function<T, T1> fn) {
        return new AndVersionIdAndAudit<>(version, id, fn.apply(payload), audit);
    }

    public <T1> CompletableFuture<AndVersionIdAndAudit<T1>> mapK(Function<T, CompletableFuture<T1>> fn) {
        return fn.apply(payload).thenApply(v -> new AndVersionIdAndAudit<>(version, id, v, audit));
    }
}
