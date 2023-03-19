package one.xingyi.events.optics.iso;

import java.util.function.Function;

public record Iso<From, To>(Function<From, To> to, Function<To, From> from) implements IIso<From, To> {

    @Override
    public From from(To to) {
        return from.apply(to);
    }

    @Override
    public To to(From from) {
        return to.apply(from);
    }
}
