package one.xingyi.event.postprocessor;

import one.xingyi.events.utils.functions.PartialAnd;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CompositePostProcessor implements IPostProcessor {
    final private IPostProcessor defaultPostProcessor;
    final private List<PartialAnd<String, IPostProcessor>> postProcessors;


    public CompositePostProcessor(IPostProcessor defaultPostProcessor, List<PartialAnd<String, IPostProcessor>> postProcessors) {
        this.defaultPostProcessor = defaultPostProcessor;
        this.postProcessors = postProcessors;
    }

    @Override
    public CompletableFuture<byte[]> postProcess(String postProcess, Object resultFromEvents) {
        return PartialAnd.chainWithDefault(postProcess, defaultPostProcessor, postProcessors).postProcess(postProcess, resultFromEvents);
    }
}
