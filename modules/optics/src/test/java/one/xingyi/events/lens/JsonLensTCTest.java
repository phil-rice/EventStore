package one.xingyi.events.lens;

import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class JsonLensTCTest {

    @Test
    public void testMapLensGet() {
        var al = ILensTC.jsonLensTc.mapLens("a");
        assertEquals(3, al.get(Map.of("a", 3, "b", 1)));
    }

    @Test
    public void testMapLensSet() {
        var al = ILensTC.jsonLensTc.mapLens("a");
        assertEquals(
                Map.of("a", 1, "b", 2),
                al.set(Map.of("a", 3, "b", 2), 1));
    }
}