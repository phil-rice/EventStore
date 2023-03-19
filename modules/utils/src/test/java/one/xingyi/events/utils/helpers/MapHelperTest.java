package one.xingyi.events.utils.helpers;

import one.xingyi.events.utils.helpers.MapHelper;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class MapHelperTest {

    @Test
    public void testGet2() {
        List<Integer> list123 = List.of(1, 2, 3);
        var ab123 = Map.of("a", Map.of("b", list123));
        assertEquals(list123, MapHelper.get2(ab123, "a", "b"));
        assertNull(MapHelper.get2(ab123, "a", "c"));
        assertNull(MapHelper.get2(ab123, "b", "c"));
    }

    List<Integer> list1 = List.of(1);
    List<Integer> list12 = List.of(1, 2);

    Map<String, Map<String, List<Integer>>> ab1 = Map.of("a", Map.of("b", list1));
    Map<String, Map<String, List<Integer>>> ab12 = Map.of("a", Map.of("b", list12));

    @Test
    public void testAddToListWhenEmpty() {
        Map<String, Map<String, List<Object>>> map = new HashMap<>();
        MapHelper.addToList2(map, "a", "b", 1);
        assertEquals(ab1, map);
    }

    @Test
    public void testAddToListWhenSecondEmpty() {
        Map<String, Map<String, List<Integer>>> map = new HashMap<>();
        map.put("a", new HashMap<>());
        MapHelper.addToList2(map, "a", "b", 1);
        assertEquals(ab1, map);
    }

    @Test
    public void testAddToListWhenSecondExisting() {
        var list1 = new ArrayList<>(List.of(1));
        Map<String, List<Integer>> b1 = new HashMap<>(Map.of("b", list1));
        Map<String, Map<String, List<Integer>>> map = new HashMap<>(Map.of("a", b1));
        MapHelper.addToList2(map, "a", "b", 2);
        assertEquals(ab12, map);
    }

    @Test
    public void testPut2WhenEmpty() {
        Map<String, Map<String, Integer>> map = new HashMap<>();
        MapHelper.put2(map, "a", "b", 1);
        assertEquals(Map.of("a", Map.of("b", 1)), map);
    }

    @Test
    public void testPut2WhenSecondEmpty() {
        Map<String, Map<String, Integer>> map = new HashMap<>();
        map.put("a", new HashMap<>());
        MapHelper.put2(map, "a", "b", 1);
        assertEquals(Map.of("a", Map.of("b", 1)), map);
    }

    @Test
    public void testPut2WhenSecondPresent() {
        var list1 = new ArrayList<>(List.of(1));
        Map<String, List<Integer>> b1 = new HashMap<>(Map.of("b", list1));
        Map<String, Map<String, List<Integer>>> map = new HashMap<>(Map.of("a", b1));
        MapHelper.put2(map, "a", "b", list12);
        assertEquals(ab12, map);
    }
}