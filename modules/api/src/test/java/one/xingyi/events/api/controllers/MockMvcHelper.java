package one.xingyi.events.api.controllers;

import one.xingyi.events.utils.ConsumerWithException;
import one.xingyi.events.utils.FunctionWithException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.function.Consumer;
import java.util.function.Function;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.asyncDispatch;

public interface MockMvcHelper {

    static void performAsync(MockMvc mockMvc, FunctionWithException<MockMvc, ResultActions, Exception> consumer, ConsumerWithException<ResultActions, Exception> resultActionsConsumer) throws Exception {
        var rest = consumer.apply(mockMvc).andReturn();
        resultActionsConsumer.accept(mockMvc.perform(asyncDispatch(rest)));
    }
}
