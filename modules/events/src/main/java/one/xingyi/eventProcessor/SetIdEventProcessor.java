package one.xingyi.eventProcessor;

import one.xingyi.events.SetIdEvent;

public class SetIdEventProcessor extends AbstractEventProcessor<SetIdEvent> {

    protected SetIdEventProcessor() {
        super(SetIdEvent.class);
    }

    @Override
    protected <T> T applyEvent(IEventTc<T> tc, SetIdEvent event, T value) {
        return tc.id2Value().apply(event.id());
    }
}
