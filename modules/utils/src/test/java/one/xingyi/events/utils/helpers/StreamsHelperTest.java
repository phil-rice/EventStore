package one.xingyi.events.utils.helpers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StreamsHelperTest {

    @Test
    public void testMapFromClassPath() throws Exception {
        String result = StreamsHelper.mapFromClassPath(getClass(), "/some.test.json", s -> s);
        assertEquals("{\"some\": \"testJson\"}", result);
    }
}
