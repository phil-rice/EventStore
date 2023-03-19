package one.xingyi.events.api;

import one.xingyi.event.audit.IWho;
import one.xingyi.event.audit.Who;
import one.xingyi.event.jslt.Jslt;
import one.xingyi.event.postprocessor.IPostProcessor;
import one.xingyi.events.eventStore.FileEventStore;
import one.xingyi.events.eventStore.IEventStore;
import one.xingyi.events.utils.services.IMimeHelper;
import one.xingyi.events.utils.services.ITime;
import one.xingyi.events.utils.services.Time;
import one.xingyi.store.idvaluestore.IIdAndValueStore;
import one.xingyi.store.idvaluestore.IdAndValueFileStore;
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
        return FileEventStore.storeUniqueFiles(executor, directory, File.separator, 2, 2, 2);
    }

    @Bean
    IIdAndValueStore idAndValueStore(@Value("${idAndValue.metadata.secret}") String secret, @Value("${idAndValue.directory}") String directory) {
        return IdAndValueFileStore.store(executor, secret, directory, File.separator, 2, 2, 2);
    }

    @Bean
    IPostProcessor processor(@Autowired IIdAndValueStore store) {
        if (store == null) throw new NullPointerException("store is null");
        return IPostProcessor.defaultPostProcessor(IIdAndValueStore.getJsonString(store), new Jslt());
    }

    @Bean
    IWho who() {
        return new Who();
    }


    @Bean
    ITime time() {
        return new Time();
    }
}
