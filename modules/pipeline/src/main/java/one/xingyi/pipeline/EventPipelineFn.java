package one.xingyi.pipeline;

import one.xingyi.events.utils.helpers.AsyncHelper;
import one.xingyi.events.utils.tuples.Tuple2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Stream;


/*
Want composibility...
Pretty much most loops will look like this


  for n in names
    for ns in namespsaces
        get events for ns and n
        pipline(ns, n) -> T
        postprocess -> O1
     postprocess
  postprocess


 */

class PipelineSink<Ref, T, O> implements Function<Ref, Collector<CompletableFuture<T>, List<CompletableFuture<Tuple2<Ref, T>>>, CompletableFuture<O>>> {
    final Function<List<Tuple2<Ref, T>>, O> combiner;

    PipelineSink(Function<List<Tuple2<Ref, T>>, O> combiner) {
        this.combiner = combiner;
    }

    @Override
    public Collector<CompletableFuture<T>, List<CompletableFuture<Tuple2<Ref, T>>>, CompletableFuture<O>> apply(Ref ref) {
        return new CollectFuturesWithRef<>(ref, combiner);
    }
}

class CollectFuturesWithRef<Ref, T, O> implements Collector<CompletableFuture<T>, List<CompletableFuture<Tuple2<Ref, T>>>, CompletableFuture<O>> {

    final Ref ref;
    final Function<List<Tuple2<Ref, T>>, O> combiner;

    CollectFuturesWithRef(Ref ref, Function<List<Tuple2<Ref, T>>, O> combiner) {
        this.ref = ref;
        this.combiner = combiner;
    }


    @Override
    public Supplier<List<CompletableFuture<Tuple2<Ref, T>>>> supplier() {
        return () -> Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public BiConsumer<List<CompletableFuture<Tuple2<Ref, T>>>, CompletableFuture<T>> accumulator() {
        return (acc, future) -> acc.add(future.thenApply(t -> Tuple2.of(ref, t)));
    }

    @Override
    public BinaryOperator<List<CompletableFuture<Tuple2<Ref, T>>>> combiner() {
        return (a, b) -> {
            a.addAll(b);
            return a;
        };
    }

    @Override
    public Function<List<CompletableFuture<Tuple2<Ref, T>>>, CompletableFuture<O>> finisher() {
        return futures -> AsyncHelper.toFutureOfList(futures).thenApply(combiner);
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED, Characteristics.CONCURRENT);
    }
}

class CollectFutures<T, O> implements Collector<CompletableFuture<T>, List<CompletableFuture<T>>, CompletableFuture<O>> {

    final Function<List<T>, O> combiner;

    CollectFutures(Function<List<T>, O> combiner) {
        this.combiner = combiner;
    }

    @Override
    public Supplier<List<CompletableFuture<T>>> supplier() {
        return () -> Collections.synchronizedList(new ArrayList<>());
    }

    @Override
    public BiConsumer<List<CompletableFuture<T>>, CompletableFuture<T>> accumulator() {
        return List::add;
    }

    @Override
    public BinaryOperator<List<CompletableFuture<T>>> combiner() {
        return (a, b) -> {
            a.addAll(b);
            return a;
        };
    }

    @Override
    public Function<List<CompletableFuture<T>>, CompletableFuture<O>> finisher() {
        return futures -> AsyncHelper.toFutureOfList(futures).thenApply(combiner);
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.UNORDERED, Characteristics.CONCURRENT);
    }
}

/**
 * @param <E>   The type of the event. Will often by IEvent or AndAudit < IEvent >
 * @param <T>   The result type of the event processor. Could be json/xml/byte[]/String/whatever
 * @param <EO>  We might choose to do a transformation on each event output. We might turn byte[] into a String, or transform a json into a different json
 * @param <NO>  We can do things with the output from each name. We might for example produce a Map<String, EO>, or deep combine them (json/xml) to get 'all the data about this name' just have them as a List
 * @param <NSO> We can do things with the output from each namespace. We might for example produce a Map<String, Map<String, EO>>, or just have them as a List
 */
public record EventPipelineFn<E, T, EO, NSO, NO>(
        Stream<String> names,
        Function<String, Stream<String>> namespace,

        BiFunction<String, String, CompletableFuture<List<E>>> eventStore,

        Function<List<E>, CompletableFuture<T>> eventProcessor,
        Function<Tuple2<String, String>, Function<T, EO>> postProcessEvents,
        PipelineSink<String, EO, NO> postProcessNamespacesSink,
        CollectFutures<NO, NSO> postProcessNamesSink
) {

    public CompletableFuture<NSO> run() {
        //names in parallel because we might as well
        //  for n in names
        //    for ns in namespaces
        //        get events for ns and n
        //        pipline(ns, n) -> T
        //        postprocess -> EO
        //     postprocess all the EOs (and their ns/n) -> NO
        //  postprocess all the NOs (and their n) -> NSO
        return names.parallel().map(name -> namespace.apply(name).map(namespace ->
                                eventStore.apply(namespace, name).thenCompose(eventProcessor).thenApply(postProcessEvents.apply(Tuple2.of(namespace, name))))
                        .collect(postProcessNamespacesSink.apply(name)))
                .collect(postProcessNamesSink);
    }

}
