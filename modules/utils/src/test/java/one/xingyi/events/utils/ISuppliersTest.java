package one.xingyi.events.utils;

import one.xingyi.events.utils.collectors.ICollectors;
import one.xingyi.events.utils.tuples.Tuple2;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ISuppliersTest {

    @Test
    public void testCartesianJoin() {
        assertEquals(List.of(), ISuppliers.cartesianJoin(List.of(), List.of()).get().toList());
        assertEquals(List.of(), ISuppliers.cartesianJoin(List.of("a"), List.of()).get().toList());
        assertEquals(List.of(), ISuppliers.cartesianJoin(List.of(), List.of("a")).get().toList());
        assertEquals(List.of(Tuple2.of("a", 1)), ISuppliers.cartesianJoin(List.of("a"), List.of(1)).get().toList());
        assertEquals(List.of(Tuple2.of("a", 1), Tuple2.of("a", 2), Tuple2.of("b", 1), Tuple2.of("b", 2)), ISuppliers.cartesianJoin(List.of("a", "b"), List.of(1, 2)).get().toList());
    }

    @Test //OK This is doubling up on tests... but I want to know it works
    public void testCartesianJoinIntoCollectorMapMap() {
        AtomicInteger i = new AtomicInteger();
        assertEquals(Map.of(), ISuppliers.<String, String>cartesianJoin(List.of(), List.of()).get().map(t2 -> t2.and(i.incrementAndGet())).collect(ICollectors.stringMapMapForTuple3()));
        assertEquals(Map.of("p", Map.of("a", 1)), ISuppliers.cartesianJoin(List.of("p"), List.of("a")).get().map(t2 -> t2.and(i.incrementAndGet())).collect(ICollectors.stringMapMapForTuple3()));
        assertEquals(Map.of("p", Map.of("a", 2, "b", 3), "q", Map.of("a", 4, "b", 5)), ISuppliers.cartesianJoin(List.of("p", "q"), List.of("a", "b")).get().map(t2 -> t2.and(i.incrementAndGet())).collect(ICollectors.stringMapMapForTuple3()));
    }

}