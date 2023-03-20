package one.xingyi.events.optics.lens;

import java.util.function.BiFunction;
import java.util.function.Function;

public interface ILens<Main, Child> {
    Main set(Main main, Child child);

    Child get(Main main);

    default <GC> ILens<Main, GC> andThen(ILens<Child, GC> lens) {
        return new Lens<>(
                m -> lens.get(get(m)),
                (m, gc) -> set(m, lens.set(get(m), gc))
        );
    }

    static <M> ILens<M, M> identityL() {
        return new Lens<>(m -> m, (m, m1) -> m1);
    }

    static <K, V> ILens<K, V> jsonLens(String path) {
        return (ILens) LensDefn.partLens(ILensTC.jsonLensTc, path);
    }

}

record Lens<Main, Child>(Function<Main, Child> getter,
                         BiFunction<Main, Child, Main> setter) implements ILens<Main, Child> {
    @Override
    public Main set(Main main, Child child) {
        return setter.apply(main, child);
    }

    @Override
    public Child get(Main main) {
        return getter.apply(main);
    }
}
