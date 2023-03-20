package one.xingyi.events.optics.traversal;

import one.xingyi.events.utils.interfaces.FunctionWithException;

import java.util.stream.Stream;

public class ComposeTraversal<M, C, G> implements ITraversal<M, G> {

 public   final ITraversal<M, C> first;
    public final ITraversal<C, G> second;

    public  ComposeTraversal(ITraversal<M, C> first, ITraversal<C, G> second) {
        this.first = first;
        this.second = second;
    }

    @Override
    public Stream<G> stream(M m) {
        return first.stream(m).flatMap(second::stream);
    }

    @Override
    public <E extends Exception> FunctionWithException<M, M, E> replace(FunctionWithException<G, G, E> fn) {
        return first.replace(c -> second.replace(fn).apply(c));
    }
}
