package one.xingyi.events;

import one.xingyi.eventFixture.EventProcessorFixture;
import one.xingyi.eventStore.FileEventStore;
import one.xingyi.eventStore.IEventStore;
import one.xingyi.events.api.EventAndWhy;
import one.xingyi.events.api.IWho;
import one.xingyi.events.utils.ITime;
import one.xingyi.events.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static one.xingyi.eventFixture.EventProcessorFixture.events01234;

@RestController
public class EventsApiController {

    @Autowired
    ITime time;
    @Autowired
    IWho who;
    final IEventStore eventStore = FileEventStore.store("store");
    final private Function<String, List<String>> splitter = StringHelper.split(",");

    @GetMapping(value = "/events/{namespaces}/{names}", produces = "application/json")
    public Map<String, Map<String, List<EventAndAudit>>> getEvents(@PathVariable String namespaces, @PathVariable String names) {
        var nsList = splitter.apply(namespaces);
        var nameList = splitter.apply(names);
        return IEventStore.getAll(eventStore, nsList, nameList);
    }

    @GetMapping(value = "/sampleEvent/{i}", produces = "application/json")
    public EventAndWhy sampleEvents(@PathVariable Integer i) {
        IEvent e = events01234.get(i);
        System.out.println("Event " + i + " is " + e);
        if (e == null) throw new RuntimeException("No event for " + i);
        return new EventAndWhy(e, "sample");
    }

    @PostMapping(value = "/sampleEvent/{namespace}/{name}", produces = "application/json")
    public CompletableFuture<Void> sampleEvents(@PathVariable String namespace, @PathVariable String name, @RequestHeader HttpHeaders headers) {
        events01234.forEach(e -> eventStore.appendEvent(namespace, name, new EventAndWhy(e, "sample").andAudit(who.who(headers), time.time())).join());
        return CompletableFuture.completedFuture(null);
    }

    @PostMapping(value = "/events/{namespace}/{name}", consumes = "application/json")
    public CompletableFuture<Void> appendEvents(@PathVariable String namespace, @PathVariable String name, @RequestBody EventAndWhy eventAndWhy, @RequestHeader HttpHeaders headers) {
        return eventStore.appendEvent(namespace, name, eventAndWhy.andAudit(who.who(headers), time.time()));
    }
}
