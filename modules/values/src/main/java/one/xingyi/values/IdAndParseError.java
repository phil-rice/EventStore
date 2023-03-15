package one.xingyi.values;

import java.util.function.Function;

public record IdAndParseError<T>(String id, String s) implements IIdAndValue<T> {
    @Override
    public <T1> IdAndParseError<T1> map(Function<T, T1> fn) {
        return (IdAndParseError<T1>) this;
    }

}
