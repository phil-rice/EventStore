package one.xingyi.events.utils.exceptions;

import one.xingyi.events.utils.exceptions.WrappedException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WrappedExceptionTest {

    Exception e = new Exception("hello");

    @Test
    public void testWrap() {
        var wrappedException = WrappedException.wrap(e);
        assertEquals(e, wrappedException.getCause());
        assertEquals("hello", wrappedException.getMessage());
    }

    @Test
    public void testWrapValue() {
        assertEquals("hello", WrappedException.wrapValue(() -> "hello"));
        var we = assertThrows(WrappedException.class, () -> WrappedException.wrapValue(() -> {
            throw e;
        }));
        assertSame(e, we.getCause());
    }

    @Test
    public void testWrapSupplier() {
        assertEquals("hello", WrappedException.wrapSupplier(() -> "hello").get());
        var we = assertThrows(WrappedException.class, () -> WrappedException.wrapSupplier(() -> {
            throw e;
        }).get());
        assertSame(e, we.getCause());
    }

    @Test
    public void testWrapRunnable() {
        WrappedException.wrapRunnable(() -> {
        });
        var we = assertThrows(WrappedException.class, () -> WrappedException.wrapRunnable(() -> {
            throw e;
        }));
        assertSame(e, we.getCause());
    }

    @Test
    public void testWrapFn() {
        assertEquals("hello", WrappedException.wrapFn(s -> s).apply("hello"));
        assertSame(e, assertThrows(WrappedException.class, () -> WrappedException.wrapFn(s -> {
            throw e;
        }).apply("hello")).getCause());

    }
}

