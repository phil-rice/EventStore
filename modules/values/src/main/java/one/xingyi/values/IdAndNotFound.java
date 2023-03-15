package one.xingyi.values;

import java.util.function.Function;

public record IdAndNotFound<T>(String id) implements IIdAndValue<T> {
    @Override
    public <T1> IdAndNotFound<T1> map(Function<T, T1> fn) {
        return (IdAndNotFound<T1>) this;
    }
}
