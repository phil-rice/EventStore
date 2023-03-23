package one.xingyi.events.eventStore;

import one.xingyi.events.utils.helpers.ListHelper;
import one.xingyi.events.utils.helpers.StringHelper;
import one.xingyi.events.utils.interfaces.Consumer3;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public interface IEventStoreUsingFileStore {


    static <T> Consumer3<List<String>, byte[], Map<String, List<T>>> extractTFromFS(Function<String, T> parser) {
        return (names, bytes, map) -> {
            var s = new String(bytes, StandardCharsets.UTF_8);
            var namesWithVersionAndTab = ListHelper.map(names, name -> "1\t" + name + "\t");
            var lines = StringHelper.split(s, "\n");
            lines.forEach(line -> {
                int index = ListHelper.indexOf(namesWithVersionAndTab, line::startsWith);
                if (index >= 0)
                    map.computeIfAbsent(names.get(index), k -> new ArrayList<>()).add(parser.apply(line));
            });
        };
    }
}
