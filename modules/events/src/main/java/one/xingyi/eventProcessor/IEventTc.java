package one.xingyi.eventProcessor;

import one.xingyi.events.lens.ILensTC;

import java.util.function.BiFunction;
import java.util.function.Function;

public record IEventTc<T>(ILensTC<T> lensTC, BiFunction<String, String, T> parser, T zero, Function<String, T> id2Value) {
}
