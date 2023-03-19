package one.xingyi.events.utils.helpers;

import one.xingyi.events.utils.helpers.NullHelper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NullHelperTest {

    @Test
    void testOrElse() {
        assertEquals("a", NullHelper.orElse("a", "b"));
        assertEquals("b", NullHelper.orElse(null, "b"));
    }
}