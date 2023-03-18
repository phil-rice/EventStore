package one.xingyi.events.api.controllers;

import one.xingyi.audit.Who;
import one.xingyi.eventStore.MapEventStore;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import java.util.concurrent.atomic.AtomicLong;

@TestConfiguration
public class IntegrationTestContext {
    @Bean @Primary
    EventsController makeEventsApiController() {
        var count = new AtomicLong();
        return new EventsController(() -> 1000 * count.getAndIncrement(), new Who(), new MapEventStore());
    }

}
