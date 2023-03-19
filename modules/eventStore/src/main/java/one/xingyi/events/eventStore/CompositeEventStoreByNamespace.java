package one.xingyi.events.eventStore;

import one.xingyi.event.audit.AndAudit;
import one.xingyi.events.events.IEvent;
import one.xingyi.events.utils.functions.PartialAnd;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public class CompositeEventStoreByNamespace implements IEventStore {
    final private IEventStore defaultEventStore;
    private final List<PartialAnd<String, IEventStore>> partials;

    public CompositeEventStoreByNamespace(IEventStore defaultEventStore, List<PartialAnd<String, IEventStore>> partials) {
        this.defaultEventStore = defaultEventStore;
        this.partials = partials;
    }

    @Override
    public CompletableFuture<List<AndAudit<IEvent>>> getEvents(String nameSpace, String name) {
        return getEventStore(nameSpace).getEvents(nameSpace, name);
    }

    @Override
    public CompletableFuture<Void> appendEvent(String nameSpace, String name, AndAudit<IEvent> auditAndEvent) {
        return getEventStore(nameSpace).appendEvent(nameSpace, name, auditAndEvent);
    }

    private IEventStore getEventStore(String nameSpace) {
        return PartialAnd.chainWithDefault(nameSpace, defaultEventStore, partials);
    }

}
