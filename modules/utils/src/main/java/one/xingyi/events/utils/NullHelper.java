package one.xingyi.events.utils;

public interface NullHelper {
    static  <T> T orElse(T t, T t1) { return t == null ? t1 : t; }
}
