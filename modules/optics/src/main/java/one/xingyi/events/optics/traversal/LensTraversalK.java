package one.xingyi.events.optics.traversal;

import one.xingyi.events.optics.lens.ILens;
import one.xingyi.events.utils.interfaces.FunctionWithException;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Stream;

class LensTraversalK<M, C, G> extends LensTraversal<M, C, G> implements ITraversalK<M, G> {
    final ITraversalK<M, C> traversalK;
    private final ILens<C, G> lens;

    public LensTraversalK(ITraversalK<M, C> traversalK, ILens<C, G> lens) {
        super(traversalK, lens);
        this.traversalK = traversalK;
        this.lens = lens;
    }


    public Function<M, CompletableFuture<M>> replaceK(Function<G, CompletableFuture<G>> fn) {
        return traversalK.replaceK(c -> fn.apply(lens.get(c)).thenApply(gnew -> lens.set(c, gnew)));
    }
}
