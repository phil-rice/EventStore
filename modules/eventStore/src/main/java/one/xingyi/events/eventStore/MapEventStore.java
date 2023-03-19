package one.xingyi.events.eventStore;

import one.xingyi.audit.AndAudit;
import one.xingyi.events.IEvent;
import one.xingyi.events.utils.helpers.MapHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static one.xingyi.events.utils.helpers.MapHelper.addToList2;

public class MapEventStore implements IEventStore {
    private final Map<String, Map<String, List<AndAudit<IEvent>>>> map = new HashMap<>();

    public final Map<String, Map<String, List<AndAudit<IEvent>>>> getCopyOfMap() {
        return new HashMap<>(map);
    }

    @Override
    public CompletableFuture<List<AndAudit<IEvent>>> getEvents(String nameSpace, String name) {
        return CompletableFuture.completedFuture(MapHelper.get2(map, nameSpace, name));

    }

    @Override
    public CompletableFuture<Void> appendEvent(String nameSpace, String name, AndAudit<IEvent> eventAndAudit) {
        addToList2(map, nameSpace, name, eventAndAudit);
        return CompletableFuture.completedFuture(null);
    }
}
