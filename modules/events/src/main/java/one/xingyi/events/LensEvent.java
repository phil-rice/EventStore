package one.xingyi.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import one.xingyi.events.utils.JsonHelper;

/** THe object is a json object. aka Map or List or null or String or Number or Boolean */
public record LensEvent(String lens, Object value) implements IEvent {

    @Override
    public boolean isSource() {
        return false;
    }
}
