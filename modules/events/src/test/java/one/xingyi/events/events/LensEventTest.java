package one.xingyi.events.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import one.xingyi.events.Audit;
import one.xingyi.events.LensEvent;
import one.xingyi.events.lens.LensDefn;
import org.junit.jupiter.api.Test;


import static one.xingyi.events.events.LensEventFixture.abc;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LensEventTest {
    Audit audit1 = new Audit("who1", "when1", "what1");

    @Test
    public void testLensEventParsedValue() throws JsonProcessingException {
        LensEvent lensEvent = new LensEvent(new LensDefn("a.b.c"), "3", "json", audit1);
        assertEquals(3, lensEvent.parsedValue());
    }

    @Test
    public void testLensEventLensDefn() {
        LensEvent lensEvent = new LensEvent(new LensDefn("a.b.c"), "3", "json", audit1);
        assertEquals(LensEventFixture.abc4, lensEvent.lefnsDefn().jsonLens().set(abc, 4));
    }
}