package one.xingyi.eventFixture;

import com.fasterxml.jackson.core.JsonProcessingException;
import one.xingyi.audit.AndAudit;
import one.xingyi.audit.Audit;
import one.xingyi.events.*;
import one.xingyi.events.utils.JsonHelper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public interface EventProcessorFixture {
    Audit audit0 = new Audit("who0", 0, "when0");
    Audit audit1 = new Audit("who1", 1, "when1");
    Audit audit2 = new Audit("who2", 2, "when2");
    Audit audit3 = new Audit("who3", 3, "when3");
    Audit audit4 = new Audit("who4", 4, "when4");
    List<Audit> audit01234 = List.of(audit0, audit1, audit2, audit3, audit4);

    ZeroEvent zeroEvent = new ZeroEvent();

    static String value(int a) {
        return ("{'a':" + a + ",'b': " + (a + 1) + "}").replaceAll("'", "\"");
    }

    SetValueEvent valueEvent1 = new SetValueEvent(value(1), "json");
    SetValueEvent valueEvent2 = new SetValueEvent(value(2), "json");
    SetIdEvent idEvent3 = new SetIdEvent("id3", "json");
    LensEvent lensEvent4 = new LensEvent("a", "44", "json");
    List<IEvent> events01234 = List.of(zeroEvent, valueEvent1, valueEvent2, idEvent3, lensEvent4);

    AndAudit<IEvent> evA0 = new AndAudit<IEvent>(zeroEvent, audit0);
    AndAudit<IEvent> evA1 = new AndAudit<IEvent>(valueEvent1, audit1);
    AndAudit<IEvent> evA2 = new AndAudit<IEvent>(valueEvent2, audit2);
    AndAudit<IEvent> evA3 = new AndAudit<IEvent>(idEvent3, audit3);
    AndAudit<IEvent> evA4 = new AndAudit<IEvent>(lensEvent4, audit4);
    List<AndAudit<IEvent>> evA01234 = List.of(evA0, evA1, evA2, evA3, evA4);


    static Function<String, CompletableFuture<Object>> idToValue = s -> {
        try {
            if (s.equals("id3")) return CompletableFuture.completedFuture(JsonHelper.parseJson(value(3)));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Unknown id " + s);
    };

}