package one.xingyi.events.utils;

public interface FunctionWithException<From,To,E extends Throwable> {
    To apply(From from) throws E;
}
