package one.xingyi.events.api.controllers;

import one.xingyi.event.audit.Who;
import one.xingyi.events.eventStore.store.MapEventStore;
import one.xingyi.events.utils.helpers.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static one.xingyi.events.eventFixture.EventProcessorFixture.valueEvent1;
import static one.xingyi.events.eventFixture.EventProcessorFixture.zeroEvent;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
//@ContextConfiguration(classes = {IntegrationTestContext.class})
class EventsControllerTest {

    MockMvc mockMvc;

    EventsController controller = null;

    @BeforeEach
    void setup() {
        var count = new AtomicLong();
        controller = new EventsController(() -> 1000 * count.getAndIncrement(), new Who(), new MapEventStore());
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }


    @Test
    public void testGetEmptyEvents() throws Exception {
        String expected = JsonHelper.printJson(Map.of("name", Map.of("ns", List.of())));
        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/events/ns/name")),
                m -> m.andExpect(status().isOk()).andExpect(content().json(expected)));
    }

    @Test
    public void testOneAppendAndGetEvents() throws Exception {
        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(post("/events/ns/name").contentType("application/json").content(JsonHelper.printJson(zeroEvent))),
                m -> m.andExpect(status().isOk()));

        String expected = JsonHelper.printJson(
                Map.of("name", Map.of("ns", List.of(
                        Map.of("payload", Map.of("type", "zero"),
                                "audit", Map.of("who", "anonymous", "when", 0, "what", "unknown"))
                ))));

        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/events/ns/name")),
                m -> m.andExpect(status().isOk()).andExpect(content().json(expected)));

    }

    @Test
    public void testValueEvents() throws Exception {
        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(post("/events/ns/name").contentType("application/json").content(JsonHelper.printJson(valueEvent1))),
                m -> m.andExpect(status().isOk()));

        String expected = JsonHelper.printJson(
                Map.of("name", Map.of("ns", List.of(
                        Map.of("payload", Map.of("type", "value", "value", Map.of("a", 1, "b", 2)),
                                "audit", Map.of("who", "anonymous", "when", 0, "what", "unknown"))
                ))));

        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/events/ns/name")),
                m -> m.andExpect(status().isOk()).andExpect(content().json(expected)));

    }



}