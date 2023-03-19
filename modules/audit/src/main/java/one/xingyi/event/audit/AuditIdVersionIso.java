package one.xingyi.event.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import one.xingyi.events.optics.iso.IIso;
import one.xingyi.events.optics.iso.Iso;
import one.xingyi.events.utils.helpers.JsonHelper;
import one.xingyi.events.utils.helpers.StringHelper;

import java.util.function.Function;

import static one.xingyi.events.utils.exceptions.WrappedException.wrapValue;

public interface AuditIdVersionIso {

    static <T> IIso<String, AndVersionIdAndAudit<T>> iso(IIso<String, T> thingIso) {
        return new Iso<>(AuditIdVersionIso.parser(thingIso::to), AuditIdVersionIso.printer(thingIso::from));
    }


    static <T> Function<AndVersionIdAndAudit<T>, String> printer(Function<T, String> thingPrinter) {
        return andAudit -> {
            try {
                String auditString = JsonHelper.mapper.writeValueAsString(andAudit.audit());
                String thingString = thingPrinter.apply(andAudit.payload());
                return andAudit.version() + "\t" + andAudit.id() + "\t" + auditString + "\t" + thingString;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    static <T> Function<String, AndVersionIdAndAudit<T>> parser(Function<String, T> thingParser) {
        var splitter = StringHelper.splitIn4("\t");
        return s -> splitter.apply(s).map((version, id, auditString, thingString) -> wrapValue(() ->
                new AndVersionIdAndAudit<>(version, id,
                        thingParser.apply(thingString),
                        JsonHelper.mapper.readValue(auditString, Audit.class))));
    }
}
