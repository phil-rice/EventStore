package one.xingyi.events;

public record Audit(String who, long when, String what) {
}
