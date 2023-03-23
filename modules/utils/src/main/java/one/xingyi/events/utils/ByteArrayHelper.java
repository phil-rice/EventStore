package one.xingyi.events.utils;

import java.util.List;

public interface ByteArrayHelper {

    static byte[] append(List<byte[]> bytes) {
        int length = 0;
        for (int i = 0; i < bytes.size(); i++) length += bytes.get(i).length;
        byte[] result = new byte[length];
        int offset = 0;
        for (int i = 0; i < bytes.size(); i++) {
            var b = bytes.get(i);
            System.arraycopy(b, 0, result, offset, b.length);
            offset += b.length;
        }
        return result;
    }

    static byte[] append(byte[]... bytes) {
        int length = 0;
        for (int i = 0; i < bytes.length; i++) length += bytes[i].length;
        byte[] result = new byte[length];
        int offset = 0;
        for (int i = 0; i < bytes.length; i++) {
            var b = bytes[i];
            System.arraycopy(b, 0, result, offset, b.length);
            offset += b.length;
        }
        return result;
    }
}
