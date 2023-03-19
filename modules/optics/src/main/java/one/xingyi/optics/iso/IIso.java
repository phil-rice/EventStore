package one.xingyi.optics.iso;

import one.xingyi.events.utils.helpers.JsonHelper;

import static one.xingyi.events.utils.exceptions.WrappedException.wrapFn;

public interface IIso<From, To> {
    From from(To to);

    To to(From from);

    static <T> IIso<T, T> identity() {
        return new Iso<>(t -> t, t -> t);
    }

    static <T> IIso<String, T> jsonIso(Class<T> tClass) {
        return new Iso<>(wrapFn(t -> JsonHelper.mapper.readValue(t, tClass)), wrapFn(JsonHelper.mapper::writeValueAsString));
    }

}
