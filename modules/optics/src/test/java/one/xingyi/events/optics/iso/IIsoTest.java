package one.xingyi.events.optics.iso;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class IIsoTest {

    @Test
    public void testIso() {
        IIso<String, Integer> iso = new Iso<>(Integer::parseInt, Object::toString);
        assertEquals("1", iso.from(1));
        assertEquals(1, iso.to("1"));
    }

    @Test
    public void testIsoReverse() {
        IIso<String, Integer> iso = new Iso<>(Integer::parseInt, Object::toString);
        IIso<Integer, String> reverse = IIso.reverse(iso);
        assertEquals(1, reverse.from("1"));
        assertEquals("1", reverse.to(1));
    }

    @Test
    public void testStringToBytes() {
        IIso<String, byte[]> iso = IIso.stringToBytes();
        assertEquals("hello", new String(iso.from("hello".getBytes(StandardCharsets.UTF_8))));
        assertTrue(Arrays.equals("hello".getBytes(StandardCharsets.UTF_8), iso.to("hello")));
    }

    @Test
    public void testListStringToBytes() {
        IIso<List<String>, byte[]> iso = IIso.listStringToBytes();
        assertEquals(List.of("hello", "world"), iso.from("hello\nworld".getBytes(StandardCharsets.UTF_8)));
        assertEquals("hello\nworld\n", new String(iso.to(List.of("hello", "world")), StandardCharsets.UTF_8));

    }
}