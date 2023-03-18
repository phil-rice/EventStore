package one.xingyi.events.utils;

import org.assertj.core.groups.Tuple;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static one.xingyi.events.utils.WrappedException.wrapValue;

public interface StringHelper {

    static Function<String, String> asFileNoExtension(String rootDir, String sep, int... lengths) {
        return s -> String.join(sep, List.of(rootDir, asDirectories(sep, lengths).apply(sha256(s)), s));
    }

    static Function<String, String> asDirectories(String sep, int... lengths) {
        int fullLength = Arrays.stream(lengths).sum();
        return s -> {
            if (s.length() < fullLength)
                throw new IllegalArgumentException("String is too short to be a directory path. Min length " + fullLength + " String is " + s);
            List<String> parts = new ArrayList<>(lengths.length + 1);
            int index = 0;
            for (int length : lengths) {
                parts.add(s.substring(index, index + length));
                index += length;
            }
            //Note the end is deliberatly ignored... this is finding the directory from the string. In usage the end is the file name
            return String.join(sep, parts);
        };
    }

    static Function<String, Tuple2<String, String>> splitIn2(String separator) {
        return s -> {
            int index = s.indexOf(separator);
            if (index < 0)
                throw new RuntimeException("Cannot split string " + s + " into 2 parts using separator " + separator);
            return new Tuple2<>(s.substring(0, index), s.substring(index + 1));
        };
    }

    static String sha256(String s) {
        return wrapValue(() -> toHex(MessageDigest.getInstance("SHA-256").digest(s.getBytes())));
    }

    static Function<String, List<String>> split(String separator) {
        return s -> Arrays.stream(s.split(separator)).filter(s1 -> !s1.isEmpty()).toList();
    }

    static String toHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) sb.append(String.format("%02x", b));
        return sb.toString();
    }


    static List<String> split(String defn, String sep) {
        return Arrays.stream(defn.split(sep)).filter(s -> !s.isEmpty()).toList();
    }
}
