package one.xingyi.events.api.controllers;

import one.xingyi.audit.AndAudit;
import one.xingyi.audit.Audit;
import one.xingyi.eventStore.IEventStore;
import one.xingyi.eventStore.MapEventStore;
import one.xingyi.events.IEvent;
import one.xingyi.events.LensEvent;
import one.xingyi.events.SetIdEvent;
import one.xingyi.events.utils.JsonHelper;
import one.xingyi.store.IIdAndValueStore;
import one.xingyi.store.IdAndValueMemoryStore;
import one.xingyi.store.Metadata;
import one.xingyi.store.ValueAndMetadata;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
        var count = new AtomicLong();
        eventStore = new MapEventStore();
        idAndValue = new IdAndValueMemoryStore();
        controller = new ProcessedController(idAndValue, eventStore);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    }

    @Test
    public void testCanProcessValueEvent() throws Exception {
        eventStore.appendEvent("ns", "name", evA1).join();
        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/processed/ns/name").contentType("application/json")),
                m -> m.andExpect(status().isOk()).andExpect(content().string(JsonHelper.printJson(Map.of("name", Map.of("ns", Map.of("a", 1, "b", 2)))))));

        eventStore.appendEvent("ns", "name", evA2).join();
        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/processed/ns/name").contentType("application/json")),
                m -> m.andExpect(status().isOk()).andExpect(content().string(JsonHelper.printJson(Map.of("name", Map.of("ns", Map.of("a", 2, "b", 3)))))));

        eventStore.appendEvent("ns", "name", evA1).join();
        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/processed/ns/name").contentType("application/json")),
                m -> m.andExpect(status().isOk()).andExpect(content().string(JsonHelper.printJson(Map.of("name", Map.of("ns", Map.of("a", 1, "b", 2)))))));
    }

    @Test
    public void testCanProcessLensEvent() throws Exception {
        eventStore.appendEvent("ns", "name", evA2).join();
        eventStore.appendEvent("ns", "name", evA4).join();
        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/processed/ns/name").contentType("application/json")),
                m -> m.andExpect(status().isOk()).andExpect(content().string(JsonHelper.printJson(Map.of("name", Map.of("ns", Map.of("a", 44, "b", 3)))))));
    }

    @Test
    public void testCanProcessIdEvent() throws Exception {
        Audit audit = new Audit("user", 0, "test");
        var metadata = new Metadata("json", "application/json", audit);
        var pr = idAndValue.put(new ValueAndMetadata(
                JsonHelper.printJson(Map.of("a", 1, "b", 2)).getBytes("UTF-8"), metadata)).join();

        var ev = new AndAudit<IEvent>(new SetIdEvent(pr.id(), "json"), audit);
        eventStore.appendEvent("ns", "name", ev).join();

        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/processed/ns/name").contentType("application/json")),
                m -> m.andExpect(status().isOk()).andExpect(content().string(JsonHelper.printJson(Map.of("name", Map.of("ns", Map.of("a", 1, "b", 2)))))));
    }

}


