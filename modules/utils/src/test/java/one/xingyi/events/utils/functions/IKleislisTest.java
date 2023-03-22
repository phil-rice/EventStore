package one.xingyi.events.utils.functions;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class IKleislisTest {

    @Test
    public void testCompose() {
        var composed = IKleislis.<Integer, Integer, Integer>compose(a -> CompletableFuture.completedFuture(a + 1), b -> CompletableFuture.completedFuture(b + 2));
        assertEquals(4, composed.apply(1).join());
    }
}