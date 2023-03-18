package one.xingyi.events.api.controllers;

import one.xingyi.audit.AndAudit;
import one.xingyi.eventProcessor.IEventProcessor;
import one.xingyi.eventProcessor.IEventTc;
import one.xingyi.eventStore.IEventStore;
import one.xingyi.events.EventAndWhy;
import one.xingyi.events.IEvent;
import one.xingyi.events.utils.JsonHelper;
import one.xingyi.events.utils.MapHelper;
import one.xingyi.events.utils.StringHelper;
import one.xingyi.store.IIdAndValueStore;
import one.xingyi.store.ValueAndMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

import static one.xingyi.eventFixture.EventProcessorFixture.idToValue;
import static one.xingyi.events.utils.WrappedException.wrapFn;

@RestController
public class ProcessedController {
    private final IIdAndValueStore idAndValueStore;
    private final IEventStore eventStore;


    public ProcessedController(@Autowired IIdAndValueStore idAndValueStore, @Autowired IEventStore eventStore) {
        this.idAndValueStore = idAndValueStore;
        this.eventStore = eventStore;
    }

    final private Function<String, List<String>> splitter = StringHelper.split(",");

    CompletableFuture<Object> id2Value(String id) {
        return idAndValueStore.get(id).thenApply(wrapFn(vm -> {
            if (vm == null) return null;
            var metadata = vm.metadata();
            if (!metadata.contentType().equals("application/json"))
                throw new RuntimeException("Cannot handle content type " + metadata.contentType());
            String json = new String(vm.value(), StandardCharsets.UTF_8);
            return JsonHelper.mapper.readValue(json, Map.class);
        }));
    }

    @GetMapping(value = "/processed/{namespaces}/{names}", produces = "application/json")
    public CompletableFuture<String> getProcessedEvents(@PathVariable String namespaces, @PathVariable String names) {
        var nsList = splitter.apply(namespaces);
        var nameList = splitter.apply(names);
        IEventTc<Object> tc = IEventTc.jsonEventIc(this::id2Value);
        IEventProcessor<AndAudit<IEvent>, Object> eventProcessor = IEventProcessor.parentEventProcessor(IEventProcessor.defaultEventProcessor(tc), AndAudit::payload);
        CompletableFuture<Map<String, Map<String, List<AndAudit<IEvent>>>>> result = IEventStore.getAll(eventStore, nsList, nameList);
        return result.thenCompose(map -> MapHelper.<String, String, List<AndAudit<IEvent>>, Object>map2K(map,
                        es -> IEventProcessor.<Object, AndAudit<IEvent>>evaluate(eventProcessor, e -> e.payload().isSource(), es, null))
                .thenApply(res -> JsonHelper.printJson(res)));

    }
}
