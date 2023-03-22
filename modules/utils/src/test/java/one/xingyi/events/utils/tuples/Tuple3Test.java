package one.xingyi.events.utils.tuples;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Tuple3Test {
    @Test
    public void testMap3() {
        assertEquals(Tuple3.of(1, 2, 3), Tuple3.of(1, 2, 2).map3(i -> i + 1));
    }

}