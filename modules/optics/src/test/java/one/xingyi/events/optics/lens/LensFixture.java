package one.xingyi.events.optics.lens;

import java.util.List;
import java.util.Map;

public interface LensFixture {

    Map<String, Object> abc = Map.of("a", Map.of("b", Map.of("c", 3)));
    Map<String, Object> abc4 = Map.of("a", Map.of("b", Map.of("c", 4)));
    Map<String, Object> abcArray123 = Map.of("a", Map.of("b", Map.of("c", List.of(1, 2, 3))));
    Map<String, Object> abcArray143 = Map.of("a", Map.of("b", Map.of("c", List.of(1, 4, 3))));
    Map<String, Object> abcArray1234 = Map.of("a", Map.of("b", Map.of("c", List.of(1, 2, 3, 4))));
}
