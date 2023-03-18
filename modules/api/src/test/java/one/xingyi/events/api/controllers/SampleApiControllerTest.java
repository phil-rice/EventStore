package one.xingyi.events.api.controllers;

import one.xingyi.audit.Who;
import one.xingyi.eventStore.MapEventStore;
import one.xingyi.events.EventAndWhy;
import one.xingyi.events.utils.JsonHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import static one.xingyi.eventFixture.EventProcessorFixture.valueEvent1;
import static one.xingyi.eventFixture.EventProcessorFixture.zeroEvent;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@ContextConfiguration(classes = {IntegrationTestContext.class})
class SampleApiControllerTest {

    MockMvc mockMvc;

    SampleController controller = null;

    @BeforeEach
    void setup() {
        new IntegrationTestContext();
        var count = new AtomicLong();
        controller = new SampleController(() -> 1000 * count.getAndIncrement(), new Who(), new MapEventStore());
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();

    }


    @Test
    public void testGetNumberedSample() throws Exception {
        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/sampleEvent/0")),
                m -> m.andExpect(status().isOk()).andExpect(content().string(JsonHelper.printJson(new EventAndWhy(zeroEvent, "sample")))));
        MockMvcHelper.performAsync(mockMvc,
                m -> m.perform(get("/sampleEvent/1")),
                m -> m.andExpect(status().isOk()).andExpect(content().string(JsonHelper.printJson(new EventAndWhy(valueEvent1, "sample")))));
    }


}