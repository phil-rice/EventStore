package one.xingyi.events.api.controllers;

import com.schibsted.spt.data.jslt.Expression;
import one.xingyi.event.audit.AndAudit;
import one.xingyi.event.audit.Audit;
import one.xingyi.events.eventProcessor.IEventProcessor;
import one.xingyi.events.eventProcessor.IEventTc;
import one.xingyi.events.eventStore.IEventStore;
import one.xingyi.events.events.IEvent;
import one.xingyi.events.utils.exceptions.WrappedException;
import one.xingyi.events.utils.helpers.JsonHelper;
import one.xingyi.events.utils.helpers.ListHelper;
import one.xingyi.events.utils.helpers.MapHelper;
import one.xingyi.events.utils.helpers.StringHelper;
import one.xingyi.events.utils.jsontransform.IJsonTransform;
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


    public ProcessedController(@Autowired IIdAndValueStore idAndValueStore, @Autowired IEventStore eventStore, @Autowired IJsonTransform<Expression> jslt) {
        this.idAndValueStore = idAndValueStore;
        this.eventStore = eventStore;
        this.jslt = jslt;
    }

    final private Function<String, List<String>> splitter = StringHelper.split(",");

    final private IJsonTransform<Expression> jslt;

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

    Function<Object, CompletableFuture<String>> postProcess(String postProcess) {
        if ("value".equals(postProcess) || postProcess == null)
            return s -> CompletableFuture.completedFuture(JsonHelper.printJson(s));
        if (postProcess.startsWith("transform:")) {
            return s -> {
                var txid = postProcess.substring("transform:".length());
                return idAndValueStore.get(txid).thenApply(defn -> {
                    if (!defn.metadata().contentType().equals("application/json"))
                        throw new RuntimeException("Cannot handle defn content type " + defn.metadata().contentType() + " for id " + txid);
                    var compiled = jslt.compile(new String(defn.value(), StandardCharsets.UTF_8));
                    return jslt.transform(compiled, JsonHelper.mapper.valueToTree(s)).toPrettyString();
                });
            };
        }
        throw new RuntimeException("Unknown post process " + postProcess);
    }

    @GetMapping(value = "/processed/{namespaces}/{names}", produces = "application/json")
    public CompletableFuture<String> getProcessedEvents(@PathVariable String namespaces, @PathVariable String names,
                                                        @RequestParam(required = false) String processor,
                                                        @RequestParam(required = false) Boolean trim,
                                                        @RequestParam(required = false) String postProcess) {
        var nsList = splitter.apply(namespaces);
        var nameList = splitter.apply(names);
        var postProcessFn = postProcess(postProcess);
        return IEventStore.getAll(eventStore, nsList, nameList)
                .thenCompose(map -> MapHelper.map2K(map,
                                es -> IEventProcessor.evaluate(getEventProcessor(processor), trimPredicate(trim), es, null))
                        .thenCompose(es -> {
                            Map<String, Object> rawData = MapHelper.map(es, ns2json -> ListHelper.foldLeft(ns2json.values(), null, JsonHelper::deepCombine));
                            return postProcessFn.apply(rawData);
                        }));
    }
}
