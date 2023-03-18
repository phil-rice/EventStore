package one.xingyi.store;

public class HashMismatchException extends RuntimeException {
    public HashMismatchException(String expected, String actual) {
        super("Hash mismatch. Expected " + expected + " but was " + actual);
    }
}
