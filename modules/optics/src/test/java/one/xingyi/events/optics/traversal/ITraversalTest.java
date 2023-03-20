package one.xingyi.events.optics.traversal;

import one.xingyi.events.optics.lens.ILens;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ITraversalTest {

    @Test
    public void testListTraversal() {
        var traversal = ITraversal.<Integer>listT();
        assertEquals(List.of(1, 2, 3), traversal.getAll(List.of(1, 2, 3)));
        assertEquals(List.of(2, 4, 6), traversal.replace(x -> x * 2).apply(List.of(1, 2, 3)));
    }

    @Test
    public void testMapTraversal() {
        var traversal = ITraversal.<String, Integer>mapT();
        assertEquals(Set.of(1, 2, 3), new HashSet<>(traversal.getAll(Map.of("a", 1, "b", 2, "c", 3))));
        assertEquals(Map.of("a", 2, "b", 4, "c", 6), traversal.replace(x -> x * 2).apply(Map.of("a", 1, "b", 2, "c", 3)));
    }

    @Test
    void testComposeIntoMapMapTraversal() {
        var traversal = ITraversal.<String, Map<String, Integer>>mapT().andThen(ITraversal.mapT());
        Map<String, Map<String, Integer>> map = Map.of("p", Map.of("a", 1, "b", 2), "q", Map.of("c", 3, "d", 4));
        assertEquals(Set.of(1, 2, 3, 4), new HashSet<>(traversal.getAll(map)));
        assertEquals(Map.of("p", Map.of("a", 2, "b", 4), "q", Map.of("c", 6, "d", 8)), traversal.replace(x -> x * 2).apply(map));
    }

    @Test
    public void testLensTraversal() {
        ITraversal<List<Map<String, Integer>>, Integer> traversal = ITraversal.<Map<String, Integer>>listT().focuson(ILens.jsonLens("a"));
        var data = List.of(Map.of("a", 1, "b", 2), Map.of("a", 3, "b", 4));
        assertEquals(List.of(1, 3), traversal.getAll(data));
        assertEquals(List.of(Map.of("a", 2, "b", 2), Map.of("a", 6, "b", 4)), traversal.replace(x -> x * 2).apply(data));
    }


}