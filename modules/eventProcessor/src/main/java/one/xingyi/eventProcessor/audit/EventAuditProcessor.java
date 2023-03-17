package one.xingyi.eventProcessor.audit;

import one.xingyi.eventProcessor.IEventProcessor;
import one.xingyi.events.Audit;
import one.xingyi.events.EventAndAudit;
import one.xingyi.events.utils.ListHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EventAuditProcessor implements IEventProcessor<EventAndAudit, List<Audit>> {
    @Override
    public boolean canProcess(EventAndAudit event) {
        return true;
    }

    @Override
    public CompletableFuture<List<Audit>> apply(List<Audit> value, EventAndAudit event) {
        return CompletableFuture.completedFuture(ListHelper.add(value, event.audit()));
    }
}
