package one.xingyi.eventStore;

import one.xingyi.events.EventAndAudit;
import one.xingyi.events.utils.MapHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static one.xingyi.events.utils.MapHelper.addToList2;

public class MapEventStore implements IEventStore {
    private final Map<String, Map<String, List<EventAndAudit>>> map = new HashMap<>();

    @Override
    public CompletableFuture<List<EventAndAudit>> getEvents(String nameSpace, String name) {
        return CompletableFuture.completedFuture(MapHelper.get2(map, nameSpace, name));

    }

    @Override
    public CompletableFuture<Void> appendEvent(String nameSpace, String name, EventAndAudit eventAndAudit) {
        addToList2(map, nameSpace, name, eventAndAudit);
        return CompletableFuture.completedFuture(null);
    }
}
