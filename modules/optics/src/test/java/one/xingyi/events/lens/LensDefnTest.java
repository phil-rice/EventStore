package one.xingyi.events.lens;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static one.xingyi.events.lens.LensFixture.*;

class LensDefnTest {


    @Test
    public void testLensDefnConstructor() {
        LensDefn lensDefn = new LensDefn("a.b.c");
        assertEquals("a.b.c", lensDefn.defn);
    }

    @Test
    public void testLensDefnJsonLensSimple() {
        LensDefn lensDefn = new LensDefn("a.b.c");
        var lens = lensDefn.jsonLens();
        assertEquals(3, lens.get(abc));
        assertEquals(List.of(1, 2, 3), lens.get(abcArray123));
        assertEquals(abc4, lens.set(abc, 4));
    }


    @Test
    public void testLensDefnJsonLensArrayGet() {
        LensDefn lensDefn = new LensDefn("a.b.c[1]");
        var lens = lensDefn.jsonLens();
        assertEquals(2, lens.get(abcArray123));
        assertEquals(abcArray143, lens.set(abcArray123, 4));
    }
}