package one.xingyi.events.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface JsonHelper {
    final static ObjectMapper mapper = new ObjectMapper();

    static Object deepCombine(Object left, Object right) {
        if (left == null) return right;
        if (right == null) return left;
        if (left instanceof Map<?, ?> && right instanceof Map<?, ?>) {
            Map<String, Object> result = new HashMap<>((Map<String, Object>) left);
            ((Map<String, Object>) right).forEach((k, v) -> result.put(k, deepCombine(result.get(k), v)));
            return result;
        }
        if (left instanceof List && right instanceof List) {
            List<Object> result = new ArrayList<>((List<Object>) left);
            result.addAll((List<Object>) right);
            return result;
        }
        return right;
    }

    static BiFunctionWithException<String, String, Object, Exception> toJsonParser =
            (p, s) -> {
                if (p != "json") throw new RuntimeException("Unknown parser " + p);
                return JsonHelper.toJson(s);
            };

    static Object toJson(String value) throws JsonProcessingException {
        if (value.matches("^\\d+$")) return Integer.parseInt(value);
        if (value.matches("^\\d+.\\d+$")) return Double.parseDouble(value);
        if (value.matches("^\".*\"$")) return mapper.readValue(value, String.class);
        if (value.startsWith("[")) return mapper.readValue(value, List.class);
        if (value.startsWith("{")) return mapper.readValue(value, Object.class);
        throw new RuntimeException("Unknown json " + value);
    }
}
