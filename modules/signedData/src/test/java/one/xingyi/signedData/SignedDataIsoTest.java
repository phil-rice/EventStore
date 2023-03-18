package one.xingyi.signedData;

import one.xingyi.optics.iso.IIso;
import one.xingyi.events.utils.HashMismatchException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SignedDataIsoTest {

    static record TestRecord(String name, int age) {
    }

    IIso<String, TestRecord> iso = SignedData.iso("secret", TestRecord.class);

    @Test
    public void testPrint() {
        String fredString = iso.from(new TestRecord("fredString", 12));
        assertEquals("47a12cc4dd2d7f5e0b8b8af6ed87f96731a4c6846cc3286190a33f0246a924d4\t{\"name\":\"fredString\",\"age\":12}", fredString);
        var newFred = iso.to(fredString);
        assertEquals(new TestRecord("fredString", 12), newFred);
    }

    @Test
    public void testDetectsTampering() {
        String fredString = iso.from(new TestRecord("fredString", 12));
        assertEquals("47a12cc4dd2d7f5e0b8b8af6ed87f96731a4c6846cc3286190a33f0246a924d4\t{\"name\":\"fredString\",\"age\":12}", fredString);
        assertThrows(HashMismatchException.class, () -> iso.to(fredString + "tampered"));
    }

}