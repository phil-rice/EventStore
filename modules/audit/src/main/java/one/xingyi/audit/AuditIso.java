package one.xingyi.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import one.xingyi.events.utils.JsonHelper;
import one.xingyi.events.utils.StringHelper;
import one.xingyi.events.utils.Tuple2;
import one.xingyi.optics.iso.IIso;
import one.xingyi.optics.iso.Iso;

import java.util.function.Function;

public interface AuditIso {

    static <T> IIso<String, AndAudit<T>> iso(IIso<String, T> thingIso) {
        return new Iso<>(AuditIso.parser(thingIso::to), AuditIso.printer(thingIso::from));
    }


    static <T> Function<AndAudit<T>, String> printer(Function<T, String> thingPrinter) {
        return andAudit -> {
            try {
                String auditString = JsonHelper.mapper.writeValueAsString(andAudit.audit());
                String thingString = thingPrinter.apply(andAudit.payload());
                return auditString + "\t" + thingString;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    static <T> Function<String, AndAudit<T>> parser(Function<String, T> thingParser) {
        var splitter = StringHelper.splitIn2("\t");
        return s -> splitter.apply(s).mapTo((auditString, thingString) -> new AndAudit<>(
                thingParser.apply(thingString),
                JsonHelper.mapper.readValue(auditString, Audit.class)));
    }
}
