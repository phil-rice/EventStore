package one.xingyi.audit;

import com.fasterxml.jackson.core.JsonProcessingException;
import one.xingyi.events.utils.JsonHelper;
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
        return s -> {
            int index = s.indexOf("\t");
            if (index < 0) throw new RuntimeException("Trying to parse an audited item. No tab in " + s);
            String auditString = s.substring(0, index);
            String thingString = s.substring(index + 1);
            try {
                Audit audit = JsonHelper.mapper.readValue(auditString, Audit.class);
                T thing = thingParser.apply(thingString);
                return new AndAudit<>(thing, audit);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
