package one.xingyi.events.optics.lens;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
        assertEquals(3, lens.get(LensFixture.abc));
        assertEquals(List.of(1, 2, 3), lens.get(LensFixture.abcArray123));
        assertEquals(LensFixture.abc4, lens.set(LensFixture.abc, 4));
    }


    @Test
    public void testLensDefnJsonLensArrayGet() {
        LensDefn lensDefn = new LensDefn("a.b.c[1]");
        var lens = lensDefn.jsonLens();
        assertEquals(2, lens.get(LensFixture.abcArray123));
        assertEquals(LensFixture.abcArray143, lens.set(LensFixture.abcArray123, 4));
    }
}