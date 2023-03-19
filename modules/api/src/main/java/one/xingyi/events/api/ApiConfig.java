package one.xingyi.events.api;

import one.xingyi.audit.IWho;
import one.xingyi.audit.Who;
import one.xingyi.eventStore.FileEventStore;
import one.xingyi.eventStore.IEventStore;
import one.xingyi.events.utils.services.IMimeHelper;
import one.xingyi.store.IIdAndValueStore;
import one.xingyi.store.IdAndValueFileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.util.concurrent.Executor;

@Configuration
public class ApiConfig {

    @Bean
    IMimeHelper mimeHelper() {
        return IMimeHelper.defaultMimeHelper;
    }

    @Autowired
    Executor executor;

    @Bean
    IEventStore store(@Value("${fileStore.directory}") String directory) {
        return FileEventStore.store(executor, directory, File.separator, 2, 2, 2);
    }

    @Bean
    IIdAndValueStore idAndValueStore(@Value("${idAndValue.metadata.secret}") String secret, @Value("${idAndValue.directory}") String directory) {
        return IdAndValueFileStore.store(executor, secret, directory, File.separator, 2, 2, 2);
    }

    @Bean
    IWho who() {
        return new Who();
    }
}
