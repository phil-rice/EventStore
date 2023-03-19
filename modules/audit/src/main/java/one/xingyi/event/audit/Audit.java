package one.xingyi.event.audit;

public record Audit(String who, long when, String what) {
}
