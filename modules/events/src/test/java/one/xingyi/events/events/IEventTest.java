package one.xingyi.events.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class IEventTest {

    @Test
    public void testIsSource() {
        assertTrue(new ZeroEvent().isSource());
        assertTrue(new SetValueEvent(null).isSource());
        assertTrue(new SetIdEvent("", "json").isSource());
        assertFalse(new LensEvent("", null).isSource());
    }
}