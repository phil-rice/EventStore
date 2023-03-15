package one.xingyi.events.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonHelper {
    private final static ObjectMapper mapper = new ObjectMapper();

    public static Object toJson(String value) throws JsonProcessingException {
        if (value.matches("^\\d+$")) return Integer.parseInt(value);
        if (value.matches("^\\d+.\\d+$")) return Double.parseDouble(value);
        if (value.matches("^\".*\"$")) return mapper.readValue(value, String.class);
        if (value.startsWith("[")) return mapper.readValue(value, List.class);
        if (value.startsWith("{")) return mapper.readValue(value, Object.class);
        throw new RuntimeException("Unknown json " + value);
    }
}
