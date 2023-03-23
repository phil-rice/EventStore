package one.xingyi.events.eventStore;

import one.xingyi.event.audit.AndAudit;
import one.xingyi.events.events.IEvent;

import java.util.concurrent.CompletableFuture;

public interface IAppendToEventStore {
    CompletableFuture<Void> appendEvent(String nameSpace, String name, AndAudit<IEvent> auditAndEvent);
}
