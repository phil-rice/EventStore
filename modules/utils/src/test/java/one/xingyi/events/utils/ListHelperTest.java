package one.xingyi.events.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListHelperTest {

    @Test
    public void testFoldLeft() {
        assertEquals(0, ListHelper.foldLeft(List.<Integer>of(), 0, Integer::sum));
        assertEquals(6, ListHelper.foldLeft(List.<Integer>of(1, 2, 3), 0, Integer::sum));
    }
}