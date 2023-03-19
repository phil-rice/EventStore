package one.xingyi.events.signedData;

import one.xingyi.events.optics.iso.IIso;
import one.xingyi.events.utils.services.IHash;

public interface SignedData {
    static <T> IIso<String, T> iso(String secret, Class<T> tClass) {
        return new SignedDataIso<>(secret, "\t", IHash.sha256, tClass);

    }
}
