package one.xingyi.events;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class IEventTest {

    @Test
    public void testIsSource() {
        assertTrue(new ZeroEvent().isSource());
        assertTrue(new SetValueEvent("", "json").isSource());
        assertTrue(new SetIdEvent("", "json").isSource());
        assertFalse(new LensEvent("", "", "json").isSource());
    }
}