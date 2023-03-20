package one.xingyi.events.optics.lens;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ILensTest {

    @Test
    void testJsonLens() {
        var lens = ILens.jsonLens("a");
        Map<String, Integer> a2 = Map.of("a", 2);
        assertEquals(2, lens.get(a2));
        assertEquals(Map.of("a", 3), lens.set(a2, 3));
    }
}