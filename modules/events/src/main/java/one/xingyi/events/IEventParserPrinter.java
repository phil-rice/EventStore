package one.xingyi.events;

import com.fasterxml.jackson.core.JsonProcessingException;
import one.xingyi.events.utils.JsonHelper;
import one.xingyi.optics.iso.IIso;
import one.xingyi.optics.iso.Iso;

public interface IEventParserPrinter {

    IIso<String, EventAndAudit> iso = new Iso<>(IEventParserPrinter::parser, IEventParserPrinter::printer);


    static String printer(EventAndAudit event) {
        try {
            return JsonHelper.mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static EventAndAudit parser(String s) {
        try {
            return JsonHelper.mapper.readValue(s, EventAndAudit.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
