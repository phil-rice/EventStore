package one.xingyi.events.utils.tuples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Tuple2Test {

    @Test
    public void testMapOne() {
        assertEquals(Tuple2.of(2, 1), Tuple2.of(1, 1).mapOne(i -> i + 1));
    }

    @Test
    public void testMapTwo() {
        assertEquals(Tuple2.of(1, 2), Tuple2.of(1, 1).mapTwo(i -> i + 1));
    }

    @Test
    public void testMap2() {
        assertEquals(Tuple2.of(2, 2), Tuple2.of(1, 1).map2(i -> i + 1, i -> i + 1));
    }

    @Test
    public void testMapTo() {
        assertEquals(2, Tuple2.of(1, 1).mapTo(Integer::sum));
    }

}