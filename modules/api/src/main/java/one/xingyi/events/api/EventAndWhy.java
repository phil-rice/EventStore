package one.xingyi.events.api;

import one.xingyi.events.Audit;
import one.xingyi.events.EventAndAudit;
import one.xingyi.events.IEvent;
import one.xingyi.events.utils.ITime;
import org.springframework.util.MultiValueMap;

public record EventAndWhy(IEvent event, String why) {
    public EventAndAudit andAudit(String who, long time) {
        return new EventAndAudit(event, new Audit(who, time, why));
    }
}
