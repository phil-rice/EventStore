package one.xingyi.audit;

import java.util.function.Function;

public record AndAudit<T>(T payload, Audit audit) {
    public AndAudit {
        if (payload == null) throw new RuntimeException("Payload is null");
        if (audit == null) throw new RuntimeException("Audit is null");
        if (audit.what().contains("\t")) throw new RuntimeException("Audit what contains a tab: " + audit.what());
        if (audit.who().contains("\t")) throw new RuntimeException("Audit who contains a tab: " + audit.who());
    }

    public <T1> AndAudit<T1> map(Function<T, T1> fn) {
        return new AndAudit<>(fn.apply(payload), audit);
    }

}
