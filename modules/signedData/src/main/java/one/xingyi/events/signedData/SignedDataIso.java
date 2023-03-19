package one.xingyi.events.signedData;

import one.xingyi.events.utils.helpers.JsonHelper;
import one.xingyi.events.utils.helpers.StringHelper;
import one.xingyi.events.optics.iso.IIso;
import one.xingyi.events.utils.services.IHash;

import java.nio.charset.StandardCharsets;

public record SignedDataIso<T>(String secret, String seperator, IHash hash,
                               Class<T> tClass) implements IIso<String, T> {


    @Override
    public String from(T t) {
        String json = JsonHelper.printJson(t);
        String toHash = hash.hash((secret + json).getBytes(StandardCharsets.UTF_8));
        if (toHash.contains(seperator))
            throw new RuntimeException("Hash contains seperator [" + seperator + "] hash is [" + toHash + "]");
        return toHash + "\t" + json;
    }

    @Override
    public T to(String s) {
        return StringHelper.splitIn2(seperator).apply(s).mapTo((sig, value) -> {
            IHash.checkHashString(sig, secret + value, hash);
            return JsonHelper.mapper.readValue(value, tClass);
        });
    }
}
