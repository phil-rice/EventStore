package one.xingyi.events.utils.interfaces;

public interface ConsumerWithException <T, E extends Exception> {
    void accept(T t) throws E;
}

