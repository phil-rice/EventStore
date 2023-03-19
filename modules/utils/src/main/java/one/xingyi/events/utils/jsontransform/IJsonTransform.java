package one.xingyi.events.utils.jsontransform;

import com.fasterxml.jackson.databind.JsonNode;

public interface IJsonTransform<C> {
    C compile(String defn);

    JsonNode transform(C compiledDefn, Object value);
}
