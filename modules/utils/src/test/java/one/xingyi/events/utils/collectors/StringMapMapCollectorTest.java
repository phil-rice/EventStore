package one.xingyi.events.utils.collectors;

import one.xingyi.events.utils.tuples.Tuple3;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StringMapMapCollectorTest {

    @Test
    public void testStringMapMapCollector() {
        assertEquals(Map.of(), Stream.<Tuple3<String, String, Integer>>empty().collect(ICollectors.stringMapMapForTuple3()));
        assertEquals(Map.of("a", Map.of("b", 1)), Stream.of(Tuple3.of("a", "b", 1)).collect(ICollectors.stringMapMapForTuple3()));
        assertEquals(Map.of("a", Map.of("b", 1, "c", 2)), Stream.of(Tuple3.of("a", "b", 1), Tuple3.of("a", "c", 2)).collect(ICollectors.stringMapMapForTuple3()));
    }
}
