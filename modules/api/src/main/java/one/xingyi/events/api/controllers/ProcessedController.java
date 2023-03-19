package one.xingyi.events.api.controllers;

import one.xingyi.audit.AndAudit;
import one.xingyi.audit.Audit;
import one.xingyi.events.eventProcessor.IEventProcessor;
import one.xingyi.events.eventProcessor.IEventTc;
import one.xingyi.events.eventStore.IEventStore;
import one.xingyi.events.events.IEvent;
import one.xingyi.events.utils.exceptions.WrappedException;
import one.xingyi.events.utils.helpers.JsonHelper;
import one.xingyi.events.utils.helpers.ListHelper;
import one.xingyi.events.utils.helpers.MapHelper;
import one.xingyi.events.utils.helpers.StringHelper;
import one.xingyi.store.idvaluestore.IIdAndValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.Predicate;

import static one.xingyi.events.utils.exceptions.WrappedException.wrapFn;

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

    IEventTc<Object> tc = IEventTc.jsonEventIc(this::id2Value);
    IEventProcessor<AndAudit<IEvent>, Object> valueEventProcessor = IEventProcessor.parentEventProcessor(IEventProcessor.defaultEventProcessor(tc), AndAudit::payload);
    IEventProcessor<AndAudit<IEvent>, List<Audit>> auditEventProcessor = IEventProcessor.auditEventProcessor();

    private IEventProcessor<AndAudit<IEvent>, Object> getEventProcessor(String processor) {
        if (processor == null) return (IEventProcessor) valueEventProcessor;
        if ("audit".equals(processor)) return (IEventProcessor) auditEventProcessor;
        if ("value".equals(processor)) return (IEventProcessor) valueEventProcessor;

        throw new UnknownProcessorException(processor);
    }

    @GetMapping(value = "/processed/{namespaces}/{names}/details", produces = "application/json")
    public CompletableFuture<String> getProcessedEventDetails(@PathVariable String namespaces, @PathVariable String names, @RequestParam(required = false) String processor, @RequestParam(required = false) Boolean trim) {
        var nsList = splitter.apply(namespaces);
        var nameList = splitter.apply(names);
        return IEventStore.getAll(eventStore, nsList, nameList)
                .thenCompose(map -> MapHelper.map2K(map,
                                es -> IEventProcessor.evaluate(getEventProcessor(processor), trimPredicate(trim), es, null))
                        .thenApply(j -> {
                            return JsonHelper.printJson(j);
                        })).exceptionally(e -> {
                    e.printStackTrace();
                    throw WrappedException.wrap(e);
                });

    }

    private static Predicate<AndAudit<IEvent>> trimPredicate(Boolean trim) {
        return trim == null || trim ? (e -> e.payload().isSource()) : (e -> false);
    }


    @GetMapping(value = "/processed/{namespaces}/{names}", produces = "application/json")
    public CompletableFuture<String> getProcessedEvents(@PathVariable String namespaces, @PathVariable String names, @RequestParam(required = false) String processor, @RequestParam(required = false) Boolean trim) {
        var nsList = splitter.apply(namespaces);
        var nameList = splitter.apply(names);
        return IEventStore.getAll(eventStore, nsList, nameList)
                .thenCompose(map -> MapHelper.<String, String, List<AndAudit<IEvent>>, Object>map2K(map,
                                es -> IEventProcessor.<Object, AndAudit<IEvent>>evaluate(getEventProcessor(processor), trimPredicate(trim), es, null))
                        .thenApply(es -> {
                            return JsonHelper.printJson(MapHelper.map(es, ns2json -> ListHelper.foldLeft(ns2json.values(), null, JsonHelper::deepCombine)));
                        }));
    }
}
