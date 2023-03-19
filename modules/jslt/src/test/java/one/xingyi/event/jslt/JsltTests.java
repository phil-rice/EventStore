package one.xingyi.event.jslt;

import com.schibsted.spt.data.jslt.Expression;
import one.xingyi.events.utils.helpers.JsonHelper;
import one.xingyi.events.utils.helpers.StreamsHelper;
import one.xingyi.events.utils.jsontransform.IJsonTransform;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsltTests {

    IJsonTransform<Expression> jslt = new Jslt();

    @Test
    public void testJslt() throws Exception {
        var compiledDen = StreamsHelper.mapFromClassPath(getClass(), "/jslt.tx", jslt::compile);
        var input = StreamsHelper.mapFromClassPath(getClass(), "/jslt.json", JsonHelper.mapper::readTree);
        var expected = StreamsHelper.mapFromClassPath(getClass(), "/expected.json", s -> s);
        var actual = compiledDen.apply(input).toPrettyString();
        assertEquals(expected, actual);
    }
}
