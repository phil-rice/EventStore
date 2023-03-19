package one.xingyi.events.utils.tuples;

import one.xingyi.events.utils.interfaces.Function4;

import java.util.function.Function;

public record Tuple4<One, Two, Three, Four>(One one, Two two, Three three, Four four) {
    public <T> T map(Function4<One, Two, Three, Four, T> fn)  {
        return fn.apply(one, two, three, four);
    }
}
