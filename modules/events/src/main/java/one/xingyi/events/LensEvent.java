package one.xingyi.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import one.xingyi.events.utils.JsonHelper;

public record LensEvent(String lens, String value, String parser) implements IEvent {
    public Object parsedValue() throws JsonProcessingException {
        if ("json".equals(parser)) return JsonHelper.parseJson(value);
        throw new RuntimeException("Unknown parser " + parser);
    }
}
