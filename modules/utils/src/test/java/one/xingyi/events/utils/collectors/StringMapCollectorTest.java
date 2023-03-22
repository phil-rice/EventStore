package one.xingyi.events.utils.collectors;

import one.xingyi.events.utils.tuples.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class StringMapCollectorTest {

    @Test
    public void testStringMapCollector() {
        assertEquals(Map.of("a", 1, "b", 1), Stream.of(Tuple2.of("a", 1), Tuple2.of("b", 1)).collect(ICollectors.stringMapForTuple2()));

    }

}