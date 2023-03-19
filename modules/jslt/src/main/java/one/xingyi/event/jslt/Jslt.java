package one.xingyi.event.jslt;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.schibsted.spt.data.jslt.Expression;
import com.schibsted.spt.data.jslt.Parser;
import one.xingyi.events.utils.helpers.JsonHelper;
import one.xingyi.events.utils.jsontransform.IJsonTransform;

public class Jslt implements IJsonTransform<Expression> {
    @Override
    public Expression compile(String defn) {
        return Parser.compileString(defn);
    }

    @Override
    public JsonNode transform(Expression jslt, Object value) {
        return jslt.apply(JsonHelper.mapper.valueToTree(value));
    }
}
