package one.xingyi.events.utils.interfaces;

public interface FunctionWithException<From,To,E extends Throwable> {
    To apply(From from) throws E;
}
