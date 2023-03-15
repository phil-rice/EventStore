package one.xingyi.values;

import java.util.function.Function;

public interface IIdAndValue<T> {
    String id();

    <T1> IIdAndValue<T1> map(Function<T, T1> fn);

}
