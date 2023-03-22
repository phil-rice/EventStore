package one.xingyi.events.utils.functions;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PartialAndTest {
    @Test
    public void testChainWithDefault(){
        assertEquals("c", PartialAnd.chainWithDefault("a", "b", List.of(PartialAnd.match("a", "c"), PartialAnd.match("b", "d"))));
        assertEquals("b", PartialAnd.chainWithDefault("c", "b", List.of(PartialAnd.match("a", "c"), PartialAnd.match("b", "d"))));
    }

    @Test
    public void testMatch(){
        assertEquals(true, PartialAnd.match("a", "c").accept().test("a"));
        assertEquals(false, PartialAnd.match("a", "c").accept().test("b"));
    }

    @Test
    public void testStartsWith(){
        assertEquals(true, PartialAnd.startsWith("a", "c").accept().test("a"));
        assertEquals(true, PartialAnd.startsWith("a", "c").accept().test("abc"));
        assertEquals(false, PartialAnd.startsWith("a", "c").accept().test("b"));
    }
}