package one.xingyi.events.utils;

public interface ConsumerWithException <T, E extends Exception> {
    void accept(T t) throws E;
}

