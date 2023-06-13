package test;

import main.implementations.Bits;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BitsTest {

    @Test
    public void testConstructor() {
        Bits bits = new Bits(10);
        assertEquals(10, bits.size());
    }

    @Test
    public void testGetSet() {
        Bits bits = new Bits(10);
        assertFalse(bits.get(0));
        bits.set(0);
        assertTrue(bits.get(0));
        bits.set(1, 4);
        assertTrue(bits.get(1));
        assertTrue(bits.get(2));
        assertTrue(bits.get(3));
        assertFalse(bits.get(4));
    }

    @Test
    public void testGetRange() {
        Bits bits = new Bits("10101010");
        Bits range = bits.get(1, 6);
        assertEquals(new Bits("01010"), range);
    }

    @Test
    public void testConcat() {
        Bits bits1 = new Bits("1010");
        Bits bits2 = new Bits("1100");
        Bits concatenated = bits1.concat(bits2);
        assertEquals(new Bits("10101100"), concatenated);
    }

    @Test
    public void testXor() {
        Bits bits1 = new Bits("1010");
        Bits bits2 = new Bits("1100");
        bits1.xor(bits2);
        assertEquals(new Bits("0110"), bits1);
    }

    @Test
    public void testOr() {
        Bits bits1 = new Bits("1010");
        Bits bits2 = new Bits("1100");
        bits1.or(bits2);
        assertEquals(new Bits("1110"), bits1);
    }

    @Test
    public void testCircularShiftLeft() {
        Bits bits = new Bits("1001101000");
        bits.circularShiftLeft(0);
        assertEquals(new Bits("1001101000"), bits);
        bits.circularShiftLeft(1);
        assertEquals(new Bits("0011010001"), bits);
        bits.circularShiftLeft(2);
        assertEquals(new Bits("1101000100"), bits);
        bits.circularShiftLeft(3);
        assertEquals(new Bits("1000100110"), bits);
        bits.circularShiftLeft(4);
        assertEquals(new Bits("1001101000"), bits);
        bits.circularShiftLeft(5);
        assertEquals(new Bits("0100010011"), bits);
    }

    @Test
    public void testSwapHalves() {
        Bits bits = new Bits("11100001");
        bits.swapHalves();
        assertEquals(new Bits("00011110"), bits);
    }

    @Test
    public void testToString() {
        Bits bits = new Bits("11001100");
        String expected = "Bits{\ndata: {0, 1, 4, 5}\nbinary: 11001100\nhex: CC\n}";
        assertEquals(expected, bits.toString());
    }

    @Test
    public void testFromText() {
        String text = "hello";
        Bits bits = Bits.fromText(text);
        assertEquals(new Bits("0110100001100101011011000110110001101111"), bits);
    }

    @Test
    public void testToText() {
        Bits textBits = Bits.fromText("hello");
        String resultText = textBits.toText();
        assertEquals("hello", resultText);
    }
}