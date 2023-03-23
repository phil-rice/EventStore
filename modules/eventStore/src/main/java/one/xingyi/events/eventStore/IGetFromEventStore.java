package one.xingyi.events.eventStore;

import one.xingyi.event.audit.AndAudit;
import one.xingyi.events.events.IEvent;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface IGetFromEventStore {
    CompletableFuture<List<AndAudit<IEvent>>> getEvents(String nameSpace, String name);

}
