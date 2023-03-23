package one.xingyi.events.optics.iso;

import one.xingyi.events.utils.ByteArrayHelper;
import one.xingyi.events.utils.helpers.JsonHelper;
import one.xingyi.events.utils.helpers.ListHelper;
import one.xingyi.events.utils.helpers.StringHelper;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

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

    static <From, To> IIso<To, From> reverse(IIso<From, To> iso) {
        return new Iso<>(iso::from, iso::to);
    }

    static IIso<String, byte[]> stringToBytes() {
        return new Iso<>(String::getBytes, String::new);
    }

    static IIso<List<String>, byte[]> listStringToBytes() {
        byte[] newline = "\n".getBytes(StandardCharsets.UTF_8);
        return new Iso<>(
                (List<String> ss) -> ByteArrayHelper.append(ListHelper.flatMap(ss, s -> List.of(s.getBytes(StandardCharsets.UTF_8), newline))),
                (byte[] bs) -> StringHelper.split("\n").apply(new String(bs, StandardCharsets.UTF_8)));
    }

}
