package one.xingyi.store;

import one.xingyi.events.utils.HashMismatchException;
import one.xingyi.events.utils.IHash;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class IHashTest {
    @Test
    public void testSha256() {
        assertEquals("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", IHash.sha256.hash(new byte[]{}));
        assertEquals("2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824", IHash.sha256.hash("hello".getBytes()));
    }

    @Test
    public void testCheckHash() {
        IHash.checkHash("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855", new byte[]{}, IHash.sha256);
        var e = assertThrows(HashMismatchException.class, () -> IHash.checkHash("a", "hello".getBytes(), IHash.sha256));

        assertEquals("Hash mismatch. Expected a but was 2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824", e.getMessage());
    }

}