package one.xingyi.store;

import one.xingyi.events.utils.StringHelper;

import java.security.MessageDigest;

import static one.xingyi.events.utils.WrappedException.wrapValue;

public interface IHash {
    String hash(byte[] value);

    static void checkHash(String id, byte[] value, IHash hash) {
        String actual = hash.hash(value);
        if (!id.equals(actual))
            throw new HashMismatchException(id, actual);
    }

    IHash sha256 = new IHash() {
        @Override
        public String hash(byte[] value) {
            return wrapValue(() -> StringHelper.toHex(MessageDigest.getInstance("SHA-256").digest(value)));
        }
    };
}
