package one.xingyi.eventStore;

import one.xingyi.events.Audit;
import one.xingyi.events.EventAndAudit;
import one.xingyi.events.IEvent;
import one.xingyi.events.utils.MapHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public interface IEventStore {
    /**
     * Note that this is name -> namespace -> events... because that way all the things about the name are close together
     */
    static Map<String, Map<String, List<EventAndAudit>>> getAll(IEventStore eventStore, List<String> nameSpaces, List<String> names) {
        Map<String, Map<String, List<EventAndAudit>>> map = new HashMap<>(names.size() * nameSpaces.size());
        nameSpaces.forEach(ns -> names.forEach(name -> MapHelper.put2(map, ns, name, eventStore.getEvents(ns, name).join())));
        return map;
    }

    CompletableFuture<List<EventAndAudit>> getEvents(String nameSpace, String name);

    CompletableFuture<Void> appendEvent(String nameSpace, String name, EventAndAudit eventAndAudit);
}
