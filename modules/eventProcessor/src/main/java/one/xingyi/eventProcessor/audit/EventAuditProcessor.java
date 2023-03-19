package one.xingyi.eventProcessor.audit;

import one.xingyi.audit.AndAudit;
import one.xingyi.audit.Audit;
import one.xingyi.eventProcessor.IEventProcessor;
import one.xingyi.events.IEvent;
import one.xingyi.events.utils.ListHelper;

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
