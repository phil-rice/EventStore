package one.xingyi.eventProcessor;

import one.xingyi.events.LensEvent;

public class LensEventProcessor extends AbstractEventProcessor<LensEvent> {

    protected LensEventProcessor() {
        super(LensEvent.class);
    }

    @Override
    protected <T> T applyEvent(IEventTc<T> tc, LensEvent event, T value) {
        var lens = event.lefnsDefn().asLens(tc.lensTC());
        return lens.set(value, tc.parser().apply(event.parser(), event.value()));
    }
}
