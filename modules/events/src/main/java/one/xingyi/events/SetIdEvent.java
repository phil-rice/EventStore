package one.xingyi.events;

public record SetIdEvent(String id, String parser) implements IEvent {
}
