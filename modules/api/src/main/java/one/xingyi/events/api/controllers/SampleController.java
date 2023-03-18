package one.xingyi.events.api.controllers;

import one.xingyi.eventStore.IEventStore;
import one.xingyi.events.IEvent;
import one.xingyi.events.api.domain.EventAndWhy;
import one.xingyi.events.api.domain.IWho;
import one.xingyi.events.utils.ITime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static one.xingyi.eventFixture.EventProcessorFixture.events01234;

@RestController
public class SampleController {
    @Autowired
    ITime time;
    @Autowired
    IWho who;

    @Autowired
    IEventStore eventStore;

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

}
