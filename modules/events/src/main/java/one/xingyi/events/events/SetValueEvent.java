package one.xingyi.events.events;

/** The value is already a 'json' structure aka Map or List or null or String or Number or Boolean */
public record SetValueEvent(Object value) implements IEvent {
}
