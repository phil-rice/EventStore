package one.xingyi.events.api;

import one.xingyi.eventStore.FileEventStore;
import one.xingyi.eventStore.IEventStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Configuration
public class ApiConfig {
    @Bean
    IEventStore store() {
        return FileEventStore.store("store", File.separator, 2, 2, 2);
    }
}
