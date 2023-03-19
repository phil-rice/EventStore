package one.xingyi.events.eventStore;

import one.xingyi.event.audit.AndAudit;
import one.xingyi.event.audit.Audit;
import one.xingyi.events.events.IEvent;
import one.xingyi.events.events.SetValueEvent;
import one.xingyi.events.utils.helpers.FilesHelper;
import one.xingyi.events.utils.helpers.ListHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FileLockTest {
    static ExecutorService executor = Executors.newFixedThreadPool(100);

    @AfterAll
    static public void shutdown() {
        executor.shutdown();
    }

    AndAudit<IEvent> event(int i) {
        return new AndAudit<>(new SetValueEvent(i), new Audit("who" + i, i, "why" + i));
    }

    @Test
    public void test() {
        var dir = AbstractFileEventStoreTest.makeTempDir("filelock");
        var store = new FileEventStore(executor, (ns, n) -> dir + "/data.dat", FileEventStore.defaultIso);
        AtomicInteger aInt = new AtomicInteger();
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        Set<IEvent> expected = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            AndAudit<IEvent> eventAndAudit = event(i);
            futures.add(store.appendEvent("ns", "name", eventAndAudit));
            expected.add(eventAndAudit.payload());
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        var events = store.getEvents("ns", "name").join();
        Set<IEvent> actual = new HashSet<>(ListHelper.map(events, AndAudit::payload));
        assertEquals(expected, actual);
        assertTrue(FilesHelper.mutex.size() < 20); // there could be other threads around. But there should not be many
    }

    @Test
    public void testWithDifferentFiles() {
        var dir = AbstractFileEventStoreTest.makeTempDir("filelock");
        var store = new FileEventStore(executor, (ns, n) -> dir + "/" + n.charAt(0) + "/data.dat", FileEventStore.defaultIso);
        List<CompletableFuture<Void>> futures = new ArrayList<>();
        Set<IEvent> expected = new HashSet<>();
        for (int i = 0; i < 1000; i++) {
            AndAudit<IEvent> eventAndAudit = event(i);
            futures.add(store.appendEvent("ns", "name", eventAndAudit));
            expected.add(eventAndAudit.payload());
        }
        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
        var events = store.getEvents("ns", "name").join();
        Set<IEvent> actual = new HashSet<>(ListHelper.map(events, AndAudit::payload));
        assertEquals(expected, actual);
        assertTrue(FilesHelper.mutex.size() < 20); // there could be other threads around. But there should not be many

    }
}
