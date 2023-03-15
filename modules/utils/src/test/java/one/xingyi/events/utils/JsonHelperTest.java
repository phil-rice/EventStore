package one.xingyi.events.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonHelperTest {

    @Test
    public void testToJson() throws JsonProcessingException {
        assertEquals("a", JsonHelper.toJson("\"a\""));
        assertEquals(1, JsonHelper.toJson("1"));
        assertEquals(1.0d, JsonHelper.toJson("1.0"));
        assertEquals(List.of(1, 2, 3), JsonHelper.toJson("[1,2,3]"));
        assertEquals(Map.of("a", 1, "b", 2, "c", 3), JsonHelper.toJson("{'a':1,'b':2,'c':3}".replace('\'', '"')));
    }
}