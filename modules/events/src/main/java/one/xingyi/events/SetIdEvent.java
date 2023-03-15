package one.xingyi.events;

public record SetIdEvent(String id, String parser, Audit audit) implements IEvent {
}
