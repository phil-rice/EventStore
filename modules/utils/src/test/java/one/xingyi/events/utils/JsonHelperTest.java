package one.xingyi.events.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonHelperTest {

    Object abc = Map.of("a", Map.of("b", Map.of("c", 3)));
    Object abd = Map.of("a", Map.of("b", Map.of("d", 4)));
    Object abc_d = Map.of("a", Map.of("b", Map.of("c", 3, "d", 4)));

    Object abList123 = Map.of("a", Map.of("b", List.of(1, 2, 3)));
    Object abList45 = Map.of("a", Map.of("b", List.of(4, 5)));
    Object abList12345 = Map.of("a", Map.of("b", List.of(1, 2, 3, 4, 5)));

    @Test
    public void testDeepCombine() {
        assertEquals(abc, JsonHelper.deepCombine(abc, null));
        assertEquals(abc, JsonHelper.deepCombine(null, abc));
        assertEquals(abc_d, JsonHelper.deepCombine(abc, abd));
        assertEquals(abList12345, JsonHelper.deepCombine(abList123, abList45));
    }

    @Test
    public void testToJson() throws JsonProcessingException {
        assertEquals("a", JsonHelper.toJson("\"a\""));
        assertEquals(1, JsonHelper.toJson("1"));
        assertEquals(1.0d, JsonHelper.toJson("1.0"));
        assertEquals(List.of(1, 2, 3), JsonHelper.toJson("[1,2,3]"));
        assertEquals(Map.of("a", 1, "b", 2, "c", 3), JsonHelper.toJson("{'a':1,'b':2,'c':3}".replace('\'', '"')));
    }
}