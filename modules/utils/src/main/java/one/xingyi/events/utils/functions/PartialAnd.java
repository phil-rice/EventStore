package one.xingyi.events.utils.functions;

import java.util.List;
import java.util.function.Predicate;

public record PartialAnd<From, T>(Predicate<From> accept, T t) {

    public static <From, T> T chainWithDefault(From f, T theDefault, List<PartialAnd<From, T>> partials) {
        for (PartialAnd<From, T> partialAnd : partials)
                if (partialAnd.accept().test(f))
                    return partialAnd.t();
        return theDefault;
    }

    public static <From, T> PartialAnd<From, T> match(From f, T t) {
        return new PartialAnd<>(f::equals, t);
    }

    public static <T> PartialAnd<String, T> startsWith(String start, T t) {
        return new PartialAnd<>(s -> s != null && s.startsWith(start), t);
    }

}
