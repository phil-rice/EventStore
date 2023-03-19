package one.xingyi.events.utils.interfaces;

public interface SupplierWithException<T,E extends Exception> {
    T get() throws E;

}
