package one.xingyi.events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = LensEvent.class, name = "lens"),
        @JsonSubTypes.Type(value = ZeroEvent.class, name = "zero"),
        @JsonSubTypes.Type(value = SetValueEvent.class, name = "value"),
        @JsonSubTypes.Type(value = SetIdEvent.class, name = "id")
})
public interface IEvent {
    /**
     * If this is true, no previous events in a list of events,need to be considered
     */
    @JsonIgnore
    default boolean isSource() {
        return true;
    }


}
