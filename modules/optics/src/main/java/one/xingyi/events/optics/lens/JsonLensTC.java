package one.xingyi.events.optics.lens;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonLensTC implements ILensTC<Object> {
    @Override
    public ILens<Object, Object> mapLens(String key) {
        return new Lens<>(
                m -> m instanceof Map ? ((Map) m).get(key) : null,
                (m, t) -> {
                    if (m == null) return Map.of(key, t);
                    if (m instanceof Map) {
                        Map<String, Object> map = new HashMap<>((Map) m);
                        map.put(key, t);
                        return map;
                    }
                    throw new RuntimeException("Cannot set value on non-map object" + m.getClass() + "\n" + m);
                }
        );
    }

    @Override
    public ILens<Object, Object> mapNth(int n) {
        return new Lens<>(
                m -> m instanceof List ? ((List) m).get(n) : null,
                (m, t) -> {
                    if (m instanceof List) {
                        var result = new ArrayList<>((List) m);
                        result.set(n, t);
                        return result;
                    }
                    if (m == null) {
                        var result = new ArrayList<>(n + 1);
                        result.set(n, t);
                        return result;
                    }
                    throw new RuntimeException("Cannot set value on non-list object" + m.getClass() + "\n" + m);
                }
        );

    }

    @Override
    public ILens<Object, Object> append() {
        return new Lens<>(
                m -> {
                    throw new RuntimeException("Cannot get value from append");
                },
                (m, t) -> {
                    if (m instanceof List) {
                        var result = new ArrayList<Object>((List) m);
                        result.add(t);
                        return result;
                    }
                    if (m == null) return List.of(t);
                    throw new RuntimeException("Cannot append value on non-list object" + m.getClass() + "\n" + m);
                }
        );
    }

}
