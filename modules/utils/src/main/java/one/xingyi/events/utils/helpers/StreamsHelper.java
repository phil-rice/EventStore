package one.xingyi.events.utils.helpers;

import one.xingyi.events.utils.interfaces.FunctionWithException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public interface StreamsHelper {

    static <T> T mapFromClassPath(Class<?> clazz, String resourceName, FunctionWithException<String, T, Exception> fn) throws Exception {
        try (InputStream inputStream = clazz.getResourceAsStream(resourceName)) {
            return fn.apply(asString(inputStream));
        }
    }

    static String asString(InputStream inputStream) throws IOException {
        BufferedInputStream bis = new BufferedInputStream(inputStream);
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        for (int result = bis.read(); result != -1; result = bis.read()) buf.write((byte) result);
        return buf.toString("UTF-8");
    }
}
