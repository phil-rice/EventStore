package one.xingyi.events.eventProcessor.audit;

import one.xingyi.event.audit.AndAudit;
import one.xingyi.event.audit.Audit;
import one.xingyi.events.eventProcessor.IEventProcessor;
import one.xingyi.events.events.IEvent;
import one.xingyi.events.utils.helpers.ListHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class EventAuditProcessor implements IEventProcessor<AndAudit<IEvent>, List<Audit>> {
    @Override
    public boolean canProcess(AndAudit<IEvent> event) {
        return true;
    }

    @Override
    public CompletableFuture<List<Audit>> apply(List<Audit> value, AndAudit<IEvent> event) {
        return CompletableFuture.completedFuture(ListHelper.add(value == null ? new ArrayList<>() : value, event.audit()));
    }
}
