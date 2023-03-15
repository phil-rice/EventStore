package one.xingyi.values;

import java.util.function.Function;

public record IdAndValue<T>(String id, String s, T value) implements IIdAndValue<T> {

    @Override
    public <T1> IIdAndValue<T1> map(Function<T, T1> fn) {
        return new IdAndValue<>(id, s, fn.apply(value));
    }
}
