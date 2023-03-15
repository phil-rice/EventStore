package one.xingyi.events.utils;

import java.util.Arrays;
import java.util.List;

public interface StringHelper {
    static List<String> split(String defn, String sep) {
        return Arrays.stream(defn.split(sep)).filter(s -> !s.isEmpty()).toList();
    }
}
