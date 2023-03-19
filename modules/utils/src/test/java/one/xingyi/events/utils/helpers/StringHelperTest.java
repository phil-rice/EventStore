package one.xingyi.events.utils.helpers;

import one.xingyi.events.utils.helpers.StringHelper;
import org.junit.jupiter.api.Test;

import java.security.NoSuchAlgorithmException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class StringHelperTest {
    @Test
    public void testSplit() {
        assertEquals(List.of(), StringHelper.split("", ","));
        assertEquals(List.of("abc"), StringHelper.split("abc", ","));
        assertEquals(List.of("a", "c"), StringHelper.split("abc", "b"));
        assertEquals(List.of("a", "b", "c"), StringHelper.split(",,a,,b,,c,,", ","));
    }

    @Test
    public void testToHex() {
        assertEquals("", StringHelper.toHex(new byte[]{}));
        assertEquals("00", StringHelper.toHex(new byte[]{0}));
        assertEquals("ff", StringHelper.toHex(new byte[]{-1}));
        assertEquals("0102", StringHelper.toHex(new byte[]{1, 2}));
    }

    @Test
    public void testSha256() throws NoSuchAlgorithmException {
        assertEquals("2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824", StringHelper.sha256("hello"));
    }

    @Test
    public void testAsDirectories() {
        assertEquals("", StringHelper.asDirectories("/").apply("abc"));
        assertEquals("a/b", StringHelper.asDirectories("/", 1, 1).apply("abc"));
        assertEquals("2c&f2&4d", StringHelper.asDirectories("&", 2, 2, 2).apply("2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"));
        var e = assertThrows(IllegalArgumentException.class, () -> StringHelper.asDirectories("/", 2, 2).apply("a"));
        assertEquals("String is too short to be a directory path. Min length 4 String is a", e.getMessage());
        assertThrows(IllegalArgumentException.class, () -> StringHelper.asDirectories("/", 2, 2).apply("abc"));
        assertEquals("ab/cd", StringHelper.asDirectories("/", 2, 2).apply("abcd"));
    }

    @Test
    public void testAsFileNoExtension(){
        assertEquals("root/7/d/1/abcdefg", StringHelper.asFileNoExtension("root", "/", 1, 1, 1).apply("abcdefg"));
    }

}