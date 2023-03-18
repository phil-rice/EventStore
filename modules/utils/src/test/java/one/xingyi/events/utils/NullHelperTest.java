package one.xingyi.events.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NullHelperTest {

    @Test
    void testOrElse() {
        assertEquals("a", NullHelper.orElse("a", "b"));
        assertEquals("b", NullHelper.orElse(null, "b"));
    }
}