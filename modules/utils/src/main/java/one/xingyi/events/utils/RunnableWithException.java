package one.xingyi.events.utils;

@FunctionalInterface
public interface RunnableWithException<E extends Exception> {
    void run() throws E;
}
