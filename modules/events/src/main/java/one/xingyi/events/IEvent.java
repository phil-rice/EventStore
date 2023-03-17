package one.xingyi.events;

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


}
