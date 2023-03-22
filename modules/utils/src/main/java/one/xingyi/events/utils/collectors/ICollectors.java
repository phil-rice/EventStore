package one.xingyi.events.utils.collectors;

import one.xingyi.events.utils.tuples.Tuple2;
import one.xingyi.events.utils.tuples.Tuple3;

public interface ICollectors {

    static <V> StringMapCollector<Tuple2<String, V>, V> stringMapForTuple2() {
        return new StringMapCollector<Tuple2<String, V>, V>(Tuple2::one, Tuple2::two);
    }

    static <V> StringMapMapCollector<Tuple3<String, String, V>, V> stringMapMapForTuple3() {
        return new StringMapMapCollector<Tuple3<String, String, V>, V>(Tuple3::one, Tuple3::two, Tuple3::three);
    }

}
