package one.xingyi.events.eventStore;

import one.xingyi.event.audit.AndAudit;
import one.xingyi.events.events.IEvent;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface IGetFromEventStore2 {

    CompletableFuture<Map<String, List<AndAudit<IEvent>>>> getEvents(String nameSpaces, List<String> name);
}
