package one.xingyi.events;

public record SetValueEvent(String value, String parser, Audit audit) implements IEvent {
}
