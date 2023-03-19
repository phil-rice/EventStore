package one.xingyi.events.api.controllers;

import one.xingyi.audit.AndAudit;
import one.xingyi.audit.Audit;
import one.xingyi.eventStore.IEventStore;
import one.xingyi.eventStore.MapEventStore;
import one.xingyi.events.IEvent;
import one.xingyi.events.LensEvent;
import one.xingyi.events.SetIdEvent;
import one.xingyi.events.SetValueEvent;
import one.xingyi.events.utils.JsonHelper;
import one.xingyi.store.IIdAndValueStore;
import one.xingyi.store.IdAndValueMemoryStore;
import one.xingyi.store.Metadata;
import one.xingyi.store.ValueAndMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static one.xingyi.eventFixture.EventProcessorFixture.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProcessedControllerTest {
    MockMvc mockMvc;

    ProcessedController controller = null;
    IEventStore eventStore = null;

    IIdAndValueStore idAndValue = null;

    @BeforeEach
    void setup() {
        new IntegrationTestContext();
        eventStore = new MapEventStore();
        idAndValue = new IdAndValueMemoryStore();
        controller = new ProcessedController(idAndValue, eventStore);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }

    @Test
    public void testCanProcessValueEventDetails() throws Exception {
        eventStore.appendEvent("ns", "name", evA1).join();
        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/processed/ns/name/details").contentType("application/json")),
                m -> m.andExpect(status().isOk()).andExpect(content().json(JsonHelper.printJson(Map.of("name", Map.of("ns", Map.of("a", 1, "b", 2)))))));

        eventStore.appendEvent("ns", "name", evA2).join();
        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/processed/ns/name/details").contentType("application/json")),
                m -> m.andExpect(status().isOk()).andExpect(content().json(JsonHelper.printJson(Map.of("name", Map.of("ns", Map.of("a", 2, "b", 3)))))));

        eventStore.appendEvent("ns", "name", evA1).join();
        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/processed/ns/name/details").contentType("application/json")),
                m -> m.andExpect(status().isOk()).andExpect(content().json(JsonHelper.printJson(Map.of("name", Map.of("ns", Map.of("a", 1, "b", 2)))))));
    }

    @Test
    public void testCanProcessLensEventDetails() throws Exception {
        eventStore.appendEvent("ns", "name", evA2).join();
        eventStore.appendEvent("ns", "name", evA4).join();
        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/processed/ns/name/details").contentType("application/json")),
                m -> m.andExpect(status().isOk()).andExpect(content().json(JsonHelper.printJson(Map.of("name", Map.of("ns", Map.of("a", 44, "b", 3)))))));
    }

    @Test
    public void testCanProcessDetailsZeroEventDetails() throws Exception {
        eventStore.appendEvent("ns", "name", evA0).join();
        Map<String, Object> nsNull = new HashMap<>();
        nsNull.put("ns", null);
        Map<String, Map<String, Object>> expected = Map.of("name", nsNull);
        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/processed/ns/name/details").contentType("application/json")),
                m -> m.andExpect(status().isOk()).andExpect(content().json(JsonHelper.printJson(expected))));
    }

    @Test
    public void testCanProcessDetailsForIdEventDetails() throws Exception {
        Audit audit = new Audit("user", 0, "test");
        var metadata = new Metadata("json", "application/json", audit);
        var pr = idAndValue.put(new ValueAndMetadata(
                JsonHelper.printJson(Map.of("a", 1, "b", 2)).getBytes(StandardCharsets.UTF_8), metadata)).join();

        var ev = new AndAudit<IEvent>(new SetIdEvent(pr.id(), "json"), audit);
        eventStore.appendEvent("ns", "name", ev).join();

        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/processed/ns/name/details").contentType("application/json")),
                m -> m.andExpect(status().isOk()).andExpect(content().json(JsonHelper.printJson(Map.of("name", Map.of("ns", Map.of("a", 1, "b", 2)))))));
    }

    @Test
    public void testCanProcessMultipleNameSpacesDetails() throws Exception {
        var evForNs2 = new AndAudit<IEvent>(new SetValueEvent(Map.of("p", 1)), audit0);
        var evForNs3 = new AndAudit<IEvent>(new SetValueEvent(Map.of("q", 1)), audit0);
        eventStore.appendEvent("ns1", "name", evA1).join();
        eventStore.appendEvent("ns2", "name", evForNs2).join();
        eventStore.appendEvent("ns3", "name", evForNs3).join();
        var expected = Map.of("name", Map.of(
                "ns1", Map.of("a", 1, "b", 2),
                "ns2", Map.of("p", 1),
                "ns3", Map.of("q", 1)));
        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/processed/ns1,ns2,ns3/name/details").contentType("application/json")),
                m -> m.andExpect(status().isOk()).andExpect(content().json(JsonHelper.printJson(expected))));

    }

    @Test
    public void testCanProcessMultipleNameSpaces() throws Exception {
        var evForNs2 = new AndAudit<IEvent>(new SetValueEvent(Map.of("p", 1)), audit0);
        var evForNs3 = new AndAudit<IEvent>(new SetValueEvent(Map.of("q", 1)), audit0);
        eventStore.appendEvent("ns1", "name1", evA1).join();
        eventStore.appendEvent("ns2", "name1", evForNs2).join();
        eventStore.appendEvent("ns3", "name1", evForNs3).join();
        eventStore.appendEvent("ns1", "name2", evA1).join();
        eventStore.appendEvent("ns3", "name2", evForNs3).join();
        var expected = Map.of(
                "name1", Map.of("a", 1, "b", 2, "p", 1, "q", 1),
                "name2", Map.of("a", 1, "b", 2, "q", 1));
        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/processed/ns1,ns2,ns3/name1,name2").contentType("application/json")),
                m -> m.andExpect(status().isOk()).andExpect(content().json(JsonHelper.printJson(expected))));

    }


}


