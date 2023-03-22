package one.xingyi.events.utils.functions;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface IKleislis {
    static <A, B, C> Function<A, CompletableFuture<C>> compose(Function<A, CompletableFuture<B>> f, Function<B, CompletableFuture<C>> g) {
        return a -> f.apply(a).thenCompose(g);
    }

}
