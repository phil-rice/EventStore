package one.xingyi.events.utils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

class AsyncHelperTest {

    static ExecutorService executor = Executors.newFixedThreadPool(5);

    @AfterAll
    public static void afterAll() {
        executor.shutdown();
    }

    CompletableFuture<Integer> asyncInt(int i) {
        return AsyncHelper.wrapSupplier(executor, () -> {
            Thread.sleep(Math.round(Math.random() * 10));
            return i;
        });
    }

    @Test
    public void testToFutureOfList() {
        var list123 = List.of(1, 2, 3);
        assertEquals(list123, AsyncHelper.toFutureOfList(ListHelper.map(list123, this::asyncInt)).join());
    }

    @Test
    public void testForEach() {
        var list123 = List.of(1, 2, 3);
        var result = Collections.synchronizedList(new ArrayList<Integer>(list123.size()));
        AsyncHelper.forEach(list123, i -> asyncInt(i).thenAccept(result::add)).join();
        assertEquals(new HashSet<>(list123), new HashSet<>(result));
        assertEquals(list123.size(), result.size());
    }

    @Test
    public void testRunnableWithException() {
        RuntimeException e = new RuntimeException("Hello");
        var actual = assertThrows(CompletionException.class, () -> AsyncHelper.wrapRunnable(executor, () -> {
            Thread.sleep(10);
            throw e;
        }).join());
        assertSame(e, actual.getCause());
    }

    @Test
    public void testRunnable() {
        var result = new ArrayList<Integer>();
        AsyncHelper.wrapRunnable(executor, () -> {
            Thread.sleep(10);
            result.add(1);
        }).join();
        assertEquals(List.of(1), result);
    }

    @Test
    public void testSupplierWithException() {
        RuntimeException e = new RuntimeException("Hello");
        var actual = assertThrows(CompletionException.class, () -> AsyncHelper.wrapSupplier(executor, () -> {
            Thread.sleep(10);
            throw e;
        }).join());
        assertSame(e, actual.getCause());
    }

    @Test
    public void testSupplier() {
        var result = AsyncHelper.wrapSupplier(executor, () -> {
            Thread.sleep(10);
            return 1;
        }).join();
        assertEquals(1, result);
    }
}
