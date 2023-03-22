package one.xingyi.events.utils;

import one.xingyi.events.utils.tuples.Tuple2;

import java.util.List;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface ISuppliers {
    static <One, Two> Supplier<Stream<Tuple2<One, Two>>> cartesianJoin(List<One> ones, List<Two> twos) {
        return () -> ones.stream().flatMap(one -> twos.stream().map(two -> Tuple2.of(one, two)));
    }
}
