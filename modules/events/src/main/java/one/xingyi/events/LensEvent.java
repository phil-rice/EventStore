package one.xingyi.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import one.xingyi.optics.lens.LensDefn;
import one.xingyi.events.utils.JsonHelper;

public record LensEvent(LensDefn lefnsDefn, String value, String parser, Audit audit) implements IEvent {
    public Object parsedValue() throws JsonProcessingException {
        if ("json".equals(parser)) return JsonHelper.toJson(value);
        throw new RuntimeException("Unknown parser " + parser);
    }
}
