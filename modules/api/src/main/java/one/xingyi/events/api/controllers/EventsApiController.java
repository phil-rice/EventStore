package one.xingyi.events.api.controllers;

import one.xingyi.eventStore.FileEventStore;
import one.xingyi.eventStore.IEventStore;
import one.xingyi.events.EventAndAudit;
import one.xingyi.events.api.domain.EventAndWhy;
import one.xingyi.events.api.domain.IWho;
import one.xingyi.events.utils.ITime;
import one.xingyi.events.utils.StringHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@RestController
public class EventsApiController {

    @Autowired
    ITime time;
    @Autowired
    IWho who;
    @Autowired
    IEventStore eventStore;

    final private Function<String, List<String>> splitter = StringHelper.split(",");

    @GetMapping(value = "/events/{namespaces}/{names}", produces = "application/json")
    public Map<String, Map<String, List<EventAndAudit>>> getEvents(@PathVariable String namespaces, @PathVariable String names) {
        var nsList = splitter.apply(namespaces);
        var nameList = splitter.apply(names);
        return IEventStore.getAll(eventStore, nsList, nameList);
    }


    @PostMapping(value = "/events/{namespace}/{name}", consumes = "application/json")
    public CompletableFuture<Void> appendEvents(@PathVariable String namespace, @PathVariable String name, @RequestBody EventAndWhy eventAndWhy, @RequestHeader HttpHeaders headers) {
        return eventStore.appendEvent(namespace, name, eventAndWhy.andAudit(who.who(headers), time.time()));
    }
}
