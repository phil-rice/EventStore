package one.xingyi.events.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR, reason = "Hash mismatch")
public class HashMismatchException extends RuntimeException {
    public HashMismatchException(String expected, String actual) {
        super("Hash mismatch. Expected " + expected + " but was " + actual);
    }
}
