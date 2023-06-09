package one.xingyi.events.utils.helpers;

import one.xingyi.events.utils.tuples.Tuple2;
import one.xingyi.events.utils.tuples.Tuple4;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static one.xingyi.events.utils.exceptions.WrappedException.wrapValue;

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
            int index = findIndex(separator, s, 0);
            return new Tuple2<>(s.substring(0, index), s.substring(index + 1));
        };
    }

    static Function<String, Tuple4<String, String, String, String>> splitIn4(String separator) {
        return s -> {
            int index1 = findIndex(separator, s, 0);
            int index2 = findIndex(separator, s, index1 + 1);
            int index3 = findIndex(separator, s, index2 + 1);
            return new Tuple4<>(s.substring(0, index1), s.substring(index1 + 1, index2), s.substring(index2 + 1, index3), s.substring(index3 + 1));
        };
    }

    private static int findIndex(String separator, String s, int start) {
        int index = s.indexOf(separator, Math.max(start, 0));
        if (index < 0 || start < 0)
            throw new IllegalArgumentException("Cannot split string " + s + " using separator " + separator);
        return index;
    }

    static String to1Quote(String s) {
        return s.replace("\"", "'");
    }

    static String to2Quote(String s) {
        return s.replace("'", "\"");
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
