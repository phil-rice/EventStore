package one.xingyi.events.utils;

import java.util.function.Function;

import static one.xingyi.events.utils.WrappedException.wrapValue;

public record Tuple2<One, Two>(One one, Two two) {
    public <T> Tuple2<T, Two> mapOne(Function<One, T> fn) {
        return new Tuple2<>(fn.apply(one), two);
    }

    public <T> Tuple2<One, T> mapTwo(Function<Two, T> fn) {
        return new Tuple2<>(one, fn.apply(two));
    }

    public <T1, T2> Tuple2<T1, T2> map2(FunctionWithException<One, T1, Exception> fn1, FunctionWithException<Two, T2, Exception> fn2) {
        return wrapValue(() -> new Tuple2<>(fn1.apply(one), fn2.apply(two)));
    }
    public <T> T mapTo(BiFunctionWithException<One, Two, T, Exception> fn) {
        return wrapValue(() -> fn.apply(one, two));
    }

}
