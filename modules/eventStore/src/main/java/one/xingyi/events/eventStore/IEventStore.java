package one.xingyi.events.eventStore;

import one.xingyi.event.audit.AndAudit;
import one.xingyi.events.events.IEvent;
import one.xingyi.events.utils.functions.PartialAnd;
import one.xingyi.events.utils.helpers.*;
import one.xingyi.events.utils.tuples.Tuple3;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface IEventStore {
    /**
     * Note that this is name -> namespace -> events... because that way all the things about the name are close together
     */

    static CompletableFuture<Map<String, Map<String, List<AndAudit<IEvent>>>>> getAll(IEventStore eventStore, String nameSpaces, String names) {
        final Function<String, List<String>> splitter = StringHelper.split(",");
        var nsList = splitter.apply(nameSpaces);
        var nameList = splitter.apply(names);
        return IEventStore.getAll(eventStore, nsList, nameList);

    }

    static CompletableFuture<Map<String, Map<String, List<AndAudit<IEvent>>>>> getAll(IEventStore eventStore, List<String> nameSpaces, List<String> names) {
        Map<String, Map<String, List<AndAudit<IEvent>>>> map = new HashMap<>(names.size() * nameSpaces.size());
        return AsyncHelper.toFutureOfList(
                        ListHelper.flatMap(names, name1 -> ListHelper.map(nameSpaces, ns1 ->
                                eventStore.getEvents(ns1, name1).thenApply(events1 -> new Tuple3<>(ns1, name1, events1)))))
                .thenAccept(tuples ->
                        tuples.forEach(tuple -> MapHelper.put2(map, tuple.two(), tuple.one(), NullHelper.orElse(tuple.three(), List.of()))))
                .thenApply(willBeNull ->
                        map);
    }

    @SafeVarargs
    static IEventStore compose(IEventStore defaultEventStore, PartialAnd<String, IEventStore>... partials) {
        return new CompositeEventStoreByNamespace(defaultEventStore, Arrays.asList(partials));
    }

    CompletableFuture<List<AndAudit<IEvent>>> getEvents(String nameSpace, String name);

    CompletableFuture<Void> appendEvent(String nameSpace, String name, AndAudit<IEvent> auditAndEvent);
}
