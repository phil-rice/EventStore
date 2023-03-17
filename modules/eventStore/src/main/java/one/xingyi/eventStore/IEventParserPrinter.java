package one.xingyi.eventStore;

import com.fasterxml.jackson.core.JsonProcessingException;
import one.xingyi.events.IEvent;
import one.xingyi.events.utils.JsonHelper;
import one.xingyi.optics.iso.IIso;
import one.xingyi.optics.iso.Iso;

public interface IEventParserPrinter {

    IIso<String, IEvent> iso = new Iso<>(IEventParserPrinter::parser, IEventParserPrinter::printer);


    static String printer(IEvent event) {
        try {
            return JsonHelper.mapper.writeValueAsString(event);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    static IEvent parser(String s) {
        try {
            return JsonHelper.mapper.readValue(s, IEvent.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
