package one.xingyi.eventProcessor;

import one.xingyi.events.SetValueEvent;

public class SetValueEventProcessor extends AbstractEventProcessor<SetValueEvent> {

    protected SetValueEventProcessor() {
        super(SetValueEvent.class);
    }

    @Override
    protected <T> T applyEvent(IEventTc<T> tc, SetValueEvent event, T value) {
        return tc.parser().apply(event.parser(), event.value());
    }
}
