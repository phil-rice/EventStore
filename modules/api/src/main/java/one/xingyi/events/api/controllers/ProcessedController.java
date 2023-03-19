package one.xingyi.events.api.controllers;

import one.xingyi.event.audit.AndAudit;
import one.xingyi.event.audit.Audit;
import one.xingyi.event.postprocessor.IPostProcessor;
import one.xingyi.events.eventProcessor.IEventProcessor;
import one.xingyi.events.eventProcessor.IEventTc;
import one.xingyi.events.eventStore.IEventStore;
import one.xingyi.events.events.IEvent;
import one.xingyi.events.utils.helpers.JsonHelper;
import one.xingyi.events.utils.helpers.ListHelper;
import one.xingyi.events.utils.helpers.MapHelper;
import one.xingyi.store.idvaluestore.IIdAndValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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


    public ProcessedController(@Autowired IIdAndValueStore idAndValueStore, @Autowired IEventStore eventStore, @Autowired IPostProcessor postProcessor) {
        this.idAndValueStore = idAndValueStore;
        this.eventStore = eventStore;
        this.postProcessor = postProcessor;
    }

    final private IPostProcessor postProcessor;

    CompletableFuture<byte[]> id2Value(String id) {
        return IIdAndValueStore.getJsonBytes(idAndValueStore).apply(id);
    }


    @GetMapping(value = "/processed/{namespaces}/{names}/details", produces = "application/json")
    public CompletableFuture<byte[]> getProcessedEventDetails(@PathVariable String namespaces, @PathVariable String names, @RequestParam(required = false) String processor, @RequestParam(required = false) Boolean trim) {
        return IEventStore.getAll(eventStore, namespaces, names).thenCompose(nsToNameToEvents -> MapHelper.map2K(nsToNameToEvents, evaluateEvents(processor, trim)).thenCompose(j -> postProcessor.postProcess(processor, j)));

    }


    @GetMapping(value = "/processed/{namespaces}/{names}", produces = "application/json")
    public CompletableFuture<byte[]> getProcessedEvents(@PathVariable String namespaces, @PathVariable String names, @RequestParam(required = false) String processor, @RequestParam(required = false) Boolean trim, @RequestParam(required = false) String postProcess) {
        return IEventStore.getAll(eventStore, namespaces, names).thenCompose(nsToNameToEvents -> MapHelper.map2K(nsToNameToEvents, evaluateEvents(processor, trim)).thenCompose(es -> postProcessor.postProcess(postProcess, combineValues(es))));
    }

    private static Map<String, Object> combineValues(Map<String, Map<String, Object>> es) {
        return MapHelper.map(es, ns2json -> ListHelper.foldLeft(ns2json.values(), null, JsonHelper::deepCombine));
    }

    private Function<List<AndAudit<IEvent>>, CompletableFuture<Object>> evaluateEvents(String processor, Boolean trim) {
        return es -> IEventProcessor.evaluate(getEventProcessor(processor), trimPredicate(trim), es, null);
    }

    private static Predicate<AndAudit<IEvent>> trimPredicate(Boolean trim) {
        return trim == null || trim ? (e -> e.payload().isSource()) : (e -> false);
    }

    IEventTc<Object> tc = IEventTc.jsonEventTC(this::id2Value);
    IEventProcessor<AndAudit<IEvent>, Object> valueEventProcessor = IEventProcessor.parentEventProcessor(IEventProcessor.defaultEventProcessor(tc), AndAudit::payload);
    IEventProcessor<AndAudit<IEvent>, List<Audit>> auditEventProcessor = IEventProcessor.auditEventProcessor();

    private IEventProcessor<AndAudit<IEvent>, Object> getEventProcessor(String processor) {
        if (processor == null) return (IEventProcessor) valueEventProcessor;
        if ("audit".equals(processor)) return (IEventProcessor) auditEventProcessor;
        if ("value".equals(processor)) return (IEventProcessor) valueEventProcessor;

        throw new UnknownProcessorException(processor);
    }
}
