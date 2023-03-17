package one.xingyi.eventProcessor;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class ParentEventProcessor<Parent, Child, T> implements IEventProcessor<Parent, T> {
    private final IEventProcessor<Child, T> childProcessor;
    private final Function<Parent, Child> childFn;


    public ParentEventProcessor(IEventProcessor<Child, T> childProcessor, Function<Parent, Child> childFn) {
        this.childProcessor = childProcessor;
        this.childFn = childFn;
    }

    @Override
    public boolean canProcess(Parent event) {
        return childProcessor.canProcess(childFn.apply(event));
    }

    @Override
    public CompletableFuture<T> apply(T value, Parent event) {
        return childProcessor.apply(value, childFn.apply(event));
    }
}
