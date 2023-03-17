package one.xingyi.events.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.function.Function;

public interface StringHelper {

    static String sha256(String s) throws NoSuchAlgorithmException {
        return toBase64(MessageDigest.getInstance("SHA-256").digest(s.getBytes()));
    }

    static Function<String, List<String>> split(String separator) {
        return s -> Arrays.stream(s.split(separator)).filter(s1 -> !s1.isEmpty()).toList();
    }


    static String toBase64(byte[] bytes) {
        return Base64.getUrlEncoder().encodeToString(bytes);
    }

    static List<String> split(String defn, String sep) {
        return Arrays.stream(defn.split(sep)).filter(s -> !s.isEmpty()).toList();
    }
}
