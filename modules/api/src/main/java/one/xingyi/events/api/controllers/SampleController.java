package one.xingyi.events.api.controllers;

import one.xingyi.audit.IWho;
import one.xingyi.events.eventStore.IEventStore;
import one.xingyi.events.IEvent;
import one.xingyi.events.utils.services.ITime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

import static one.xingyi.events.eventFixture.EventProcessorFixture.events01234;

@RestController
public class SampleController {
    ITime time;
    IWho who;
    IEventStore eventStore;

    public SampleController(@Autowired ITime time, @Autowired IWho who, @Autowired IEventStore eventStore) {
        this.time = time;
        this.who = who;
        this.eventStore = eventStore;
    }

    @GetMapping(value = "/sampleEvent/{i}", produces = "application/json")
    public CompletableFuture<IEvent> sampleEvents(@PathVariable Integer i) {
        IEvent e = events01234.get(i);
        System.out.println("Event " + i + " is " + e);
        if (e == null) throw new RuntimeException("No event for " + i);
        return CompletableFuture.completedFuture(e);
    }


}
