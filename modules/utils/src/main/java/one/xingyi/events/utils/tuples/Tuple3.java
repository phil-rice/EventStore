package one.xingyi.events.utils.tuples;

import java.util.function.Function;

public record Tuple3<One, Two, Three>(One one, Two two, Three three) {

    <T> Tuple3<One, Two, T> map3(Function<Three, T> fn){
        return new Tuple3<>(one, two, fn.apply(three));
    }
}
