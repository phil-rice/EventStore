package one.xingyi.events.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import one.xingyi.events.Audit;
import one.xingyi.events.LensEvent;
import one.xingyi.optics.lens.LensDefn;
import org.junit.jupiter.api.Test;


import static one.xingyi.events.events.LensEventFixture.abc;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LensEventTest {
    Audit audit1 = new Audit("who1", "when1", "what1");

    LensEvent lensEvent = new LensEvent(new LensDefn("a.b.c"), "3", "json", audit1);



    @Test
    public void testLensEventParsedValue() throws JsonProcessingException {
        assertEquals(3, lensEvent.parsedValue());
    }

    @Test
    public void testLensEventLensDefn() {
        assertEquals(LensEventFixture.abc4, lensEvent.lefnsDefn().jsonLens().set(abc, 4));
    }

}