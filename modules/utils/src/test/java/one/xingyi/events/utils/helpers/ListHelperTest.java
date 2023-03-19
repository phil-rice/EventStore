package one.xingyi.events.utils.helpers;

import one.xingyi.events.utils.helpers.ListHelper;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ListHelperTest {

    @Test
    public void testFoldLeft() {
        assertEquals(0, ListHelper.foldLeft(List.<Integer>of(), 0, Integer::sum));
        assertEquals(6, ListHelper.foldLeft(List.<Integer>of(1, 2, 3), 0, Integer::sum));
    }

    @Test
    public void testAdd() {
        assertEquals(List.of(1, 2, 3), ListHelper.add(List.of(1, 2), 3));
    }

    @Test
    public void testMap() {
        assertEquals(List.of(2, 4, 6), ListHelper.map(List.of(1, 2, 3), i -> i * 2));
    }
    @Test
    public void testFlatMap() {
        assertEquals(List.of(1, 2, 2, 3, 3, 4), ListHelper.flatMap(List.of(1, 2, 3), i -> List.of(i, i + 1)));
    }
}