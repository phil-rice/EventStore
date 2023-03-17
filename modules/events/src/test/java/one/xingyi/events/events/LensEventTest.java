package one.xingyi.events.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import one.xingyi.events.Audit;
import one.xingyi.events.LensEvent;
import one.xingyi.optics.lens.LensDefn;
import org.junit.jupiter.api.Test;


import static one.xingyi.events.events.LensEventFixture.abc;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LensEventTest {

    LensEvent lensEvent = new LensEvent("a.b.c", "3", "json");


    @Test
    public void testLensEventParsedValue() throws JsonProcessingException {
        assertEquals(3, lensEvent.parsedValue());
    }

    @Test
    public void testLensEventLensDefn() {
        assertEquals(LensEventFixture.abc4, new LensDefn(lensEvent.lens()).jsonLens().set(abc, 4));
    }

}