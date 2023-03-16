package one.xingyi.eventProcessor;

import com.fasterxml.jackson.core.JsonProcessingException;
import one.xingyi.events.*;
import one.xingyi.events.utils.JsonHelper;
import one.xingyi.optics.lens.LensDefn;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface EventProcessorFixture {
    Audit audit0 = new Audit("who0", "why0", "when0");
    Audit audit1 = new Audit("who1", "why1", "when1");
    Audit audit2 = new Audit("who2", "why2", "when2");
    Audit audit3 = new Audit("who3", "why3", "when3");
    Audit audit4 = new Audit("who4", "why4", "when4");
    List<Audit> audit01234 = List.of(audit0, audit1, audit2, audit3, audit4);

    ZeroEvent zeroEvent = new ZeroEvent(audit0);

    static String value(int a) {
        return ("{'a':" + a + ",'b': " + (a + 1) + "}").replaceAll("'", "\"");
    }

    SetValueEvent valueEvent1 = new SetValueEvent(value(1), "json", audit1);
    SetValueEvent valueEvent2 = new SetValueEvent(value(2), "json", audit2);
    SetIdEvent idEvent3 = new SetIdEvent("id3", "json", audit3);
    LensEvent lensEvent4 = new LensEvent(new LensDefn("a"), "44", "json", audit4);

    static Function<String, CompletableFuture<Object>> idToValue = s -> {
        try {
            if (s.equals("id3")) return CompletableFuture.completedFuture(JsonHelper.toJson(value(3)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Unknown id " + s);
    };

}
