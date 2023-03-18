package one.xingyi.events.api;

import one.xingyi.audit.IWho;
import one.xingyi.audit.Who;
import one.xingyi.eventStore.FileEventStore;
import one.xingyi.eventStore.IEventStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.concurrent.Executor;

@Configuration
public class ApiConfig {

    @Autowired
    Executor executor;

    @Bean
    IEventStore store() {
        return FileEventStore.store(executor, "store", File.separator, 2, 2, 2);
    }

    @Bean
    IWho who() {
        return new Who();
    }
}
