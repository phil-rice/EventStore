package one.xingyi.events.utils.interfaces;

public interface BiConsumerWithException<T1,T2,E extends Exception> {
    void accept(T1 t1, T2 t2) throws E;
}
