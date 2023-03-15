package one.xingyi.events.lens;


import one.xingyi.events.utils.ListHelper;
import one.xingyi.events.utils.StringHelper;

import java.util.List;

public class LensDefn {
    final String defn;
    private ILens<Object, Object> jsonLens = null;

    public LensDefn(String defn) {
        this.defn = defn;
    }

    public ILens<Object, Object> jsonLens() {
        if (jsonLens == null) {
            jsonLens = asLens(ILensTC.jsonLensTc);
        }
        return jsonLens;
    }


    static <T> ILens<T, T> partLens(ILensTC<T> tc, String part) {
        if (part.matches(".+\\[\\d+]")) {
            var index = part.lastIndexOf('[');
            return partLens(tc, part.substring(0, index)).andThen(tc.mapNth(Integer.parseInt(part.substring(index + 1, part.length() - 1))));
        }
        if (part.matches(".+\\[append]"))
            return partLens(tc, part.substring(0, part.length() - 9)).andThen(tc.append());
        if (part.matches("\\w"))
            return tc.mapLens(part);
        throw new RuntimeException("Invalid part of a lens '" + part + "'");
    }

    public <T> ILens<T, T> asLens(ILensTC<T> tc) {
        List<String> parts = StringHelper.split(defn, "\\.");
        return ListHelper.foldLeft(parts, ILens.<T>identityL(), (acc, part) -> acc.andThen(partLens(tc, part)));

    }
}
