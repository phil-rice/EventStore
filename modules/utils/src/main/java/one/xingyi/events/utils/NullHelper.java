package one.xingyi.events.utils;

import java.util.function.Function;

public interface NullHelper {
    static <T> T orElse(T t, T t1) {
        return t == null ? t1 : t;
    }

    static <T, T1> T1 mapOr(T t, Function<T, T1> fn, T1 def) {
        return t == null ? def : fn.apply(t);
    }
}
