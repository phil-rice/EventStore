package one.xingyi.events.utils.helpers;

import one.xingyi.events.utils.services.Time;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TimeTest {

    @Test
    public void testTime() {//within 10 seconds of system time to allow for variable / slow test runs
        assertEquals(0, (new Time().time() - System.currentTimeMillis()) / 10000);
    }
}

