package one.xingyi.events.api.controllers;

import one.xingyi.audit.AndAudit;
import one.xingyi.events.EventAndWhy;
import one.xingyi.audit.IWho;
import one.xingyi.eventStore.IEventStore;
import one.xingyi.events.IEvent;
import one.xingyi.events.utils.ITime;
import one.xingyi.events.utils.StringHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@RestController
public class EventsApiController {
    static Logger logger = LoggerFactory.getLogger(EventsApiController.class);
    final ITime time;
    final IWho who;

    final IEventStore eventStore;

    EventsApiController(@Autowired ITime time, @Autowired IWho who, @Autowired IEventStore eventStore) {
        this.time = time;
        this.who = who;
        this.eventStore = eventStore;
    }


    final private Function<String, List<String>> splitter = StringHelper.split(",");

    @GetMapping(value = "/events/{namespaces}/{names}", produces = "application/json")
    public CompletableFuture<Map<String, Map<String, List<AndAudit<IEvent>>>>> getEvents(@PathVariable String namespaces, @PathVariable String names) {
        var nsList = splitter.apply(namespaces);
        var nameList = splitter.apply(names);
        CompletableFuture<Map<String, Map<String, List<AndAudit<IEvent>>>>> result = IEventStore.getAll(eventStore, nsList, nameList);
        result.thenAccept(map -> logger.debug("Returning " + map));
        return result;
    }


    @PostMapping(value = "/events/{namespace}/{name}", consumes = "application/json")
    public CompletableFuture<Void> appendEvents(@PathVariable String namespace, @PathVariable String name, @RequestBody EventAndWhy eventAndWhy, @RequestHeader HttpHeaders headers) {
        AndAudit<IEvent> auditAndEvent = eventAndWhy.andAudit(who.who(headers), time.time());
        logger.debug("Appending event " + auditAndEvent);
        return eventStore.appendEvent(namespace, name, auditAndEvent);
    }
}
