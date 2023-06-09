package one.xingyi.events.api;

import one.xingyi.event.audit.AndAudit;
import one.xingyi.event.audit.Audit;
import one.xingyi.events.events.IEvent;

public record EventAndWhy(IEvent event, String why) {
    public AndAudit<IEvent> andAudit(String who, long time) {
        return new AndAudit<>(event, new Audit(who, time, why));
    }
}
