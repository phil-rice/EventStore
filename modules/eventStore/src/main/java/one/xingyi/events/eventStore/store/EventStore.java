package one.xingyi.events.eventStore.store;

import one.xingyi.event.audit.AndAudit;
import one.xingyi.events.eventStore.IAppendToEventStore;
import one.xingyi.events.eventStore.IEventStore;
import one.xingyi.events.eventStore.IGetFromEventStore;
import one.xingyi.events.events.IEvent;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public final class EventStore implements IEventStore {
    final IGetFromEventStore get;
    final IAppendToEventStore append;

    public EventStore(IGetFromEventStore get, IAppendToEventStore append) {
        this.get = get;
        this.append = append;
    }

    @Override
    public CompletableFuture<Void> appendEvent(String nameSpace, String name, AndAudit<IEvent> auditAndEvent) {
        return append.appendEvent(nameSpace, name, auditAndEvent);

    }

    @Override
    public CompletableFuture<List<AndAudit<IEvent>>> getEvents(String nameSpace, String name) {
        return get.getEvents(nameSpace, name);
    }


    @Override
    public String toString() {
        return "EventStore[" + "get=" + get + ", " + "append=" + append + ']';
    }

}
