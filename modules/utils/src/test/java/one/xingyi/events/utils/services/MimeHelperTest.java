package one.xingyi.events.utils.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MimeHelperTest {

    @Test
    public void testMimeHelper() {
        var mimeHelper = new MimeHelper();
        assertEquals("json", mimeHelper.extensionForMime("application/json"));
    }
}