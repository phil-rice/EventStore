package one.xingyi.eventProcessor;

import one.xingyi.events.ZeroEvent;

public class ZeroEventProcessor extends AbstractEventProcessor<ZeroEvent> {

    protected ZeroEventProcessor() {
        super(ZeroEvent.class);
    }

    @Override
    protected <T> T applyEvent(IEventTc<T> tc, ZeroEvent event, T value) {
        return tc.zero();
    }
}
