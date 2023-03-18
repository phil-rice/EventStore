package one.xingyi.store;

import one.xingyi.audit.Audit;
import one.xingyi.events.utils.WrappedException;
import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

abstract public class AbstractIdAndValueStoreTest {

    Supplier<IIdAndValueStore> storeSupplier;

    protected AbstractIdAndValueStoreTest(Supplier<IIdAndValueStore> storeSupplier) {
        this.storeSupplier = storeSupplier;
    }

    Metadata m1 = new Metadata("ext", "mimeType1", new Audit("user1", 0, "action"));
    Metadata m2 = new Metadata("ext", "mimeType1", new Audit("user1", 0, "action"));
    byte[] value1 = "value1".getBytes();
    byte[] value2 = "value2".getBytes();

    ValueAndMetadata mv1 = new ValueAndMetadata(value1, m1);
    ValueAndMetadata mv2 = new ValueAndMetadata(value2, m2);

    @Test
    public void testPutGet() {
        var store = storeSupplier.get();

        PutResult pr1 = store.put(mv1).join();
        IHash.checkHash(pr1.id(), value1, IHash.sha256);
        assertEquals(Optional.empty(), pr1.existingMetadata());

        ValueAndMetadata res1 = store.get(pr1.id()).join();
        assertEquals(m1, res1.metadata());
        assertArrayEquals(value1, res1.value());

        PutResult pr2 = store.put(mv2).join();
        IHash.checkHash(pr2.id(), value2, IHash.sha256);
        assertTrue(pr2.existingMetadata().isEmpty());

        ValueAndMetadata res2 = store.get(pr2.id()).join();
        assertEquals(m2, res2.metadata());
        assertArrayEquals(value2, res2.value());
    }

    @Test
    public void testPutTwice() {
        var store = storeSupplier.get();

        PutResult pr1 = store.put(mv1).join();
        PutResult pr2 = store.put(mv1).join();
        IHash.checkHash(pr2.id(), value1, IHash.sha256);
        assertEquals(m1, pr2.existingMetadata().get());

        ValueAndMetadata res1 = store.get(pr1.id()).join();
        assertEquals(m1, res1.metadata());
        assertArrayEquals(value1, res1.value());
    }

    @Test
    public void testNotFound() {
        var store = storeSupplier.get();
        var notFoundF = store.get("notFound");
        var e = assertThrows(CompletionException.class, notFoundF::join);
        assertEquals(NotFoundException.class, WrappedException.unwrap(e).getClass());
    }
}
