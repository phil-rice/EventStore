package one.xingyi.events.optics.traversal;

import one.xingyi.events.optics.lens.ILens;
import one.xingyi.events.utils.interfaces.FunctionWithException;

import java.util.stream.Stream;

class LensTraversal<M, C, G> implements ITraversal<M, G> {
    private final ITraversal<M, C> traversal;
    private final ILens<C, G> lens;

    LensTraversal(ITraversal<M, C> traversal, ILens<C, G> lens) {
        this.traversal = traversal;
        this.lens = lens;
    }

    @Override
    public Stream<G> stream(M m) {
        return traversal.stream(m).map(lens::get);
    }

    @Override
    public <E extends Exception> FunctionWithException<M, M, E> replace(FunctionWithException<G, G, E> fn) {
        return traversal.replace(c -> lens.set(c, fn.apply(lens.get(c))));
    }
}
