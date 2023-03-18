package one.xingyi.events.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MimeHelperTest {

    @Test
    public void testMimeHelper() {
        var mimeHelper = new MimeHelper();
        assertEquals("json", mimeHelper.extensionForMime("application/json"));
    }
}