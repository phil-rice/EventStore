package one.xingyi.store;

import one.xingyi.events.utils.StringHelper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import static one.xingyi.events.utils.WrappedException.wrapValue;

public interface IHash {
    String hash(byte[] value);

    static String hashString(IHash hash, String value) {
        return hash.hash(value.getBytes(StandardCharsets.UTF_8));
    }

    static void checkHash(String id, byte[] value, IHash hash) {
        String actual = hash.hash(value);
        if (!id.equals(actual))
            throw new HashMismatchException(id, actual);
    }

    static void checkHashString(String id, String s, IHash hash) {
        checkHash(id, s.getBytes(StandardCharsets.UTF_8), hash);
    }


    IHash sha256 = new IHash() {
        @Override
        public String hash(byte[] value) {
            return wrapValue(() -> StringHelper.toHex(MessageDigest.getInstance("SHA-256").digest(value)));
        }
    };
}
