package one.xingyi.optics.iso;

public interface IIso<From, To> {
    From from(To to);

    To to(From from);

    static <T> IIso<T, T> identity() {
        return new Iso<>(t -> t, t -> t);
    }

}
