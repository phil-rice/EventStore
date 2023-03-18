package one.xingyi.events.utils;

public interface SupplierWithException<T,E extends Exception> {
    T get() throws E;

}
