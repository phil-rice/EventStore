package one.xingyi.events.utils.interfaces;

@FunctionalInterface
public interface RunnableWithException<E extends Exception> {
    void run() throws E;
}
