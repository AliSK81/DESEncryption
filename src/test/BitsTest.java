package test;

import main.implementations.Bits;
import org.junit.jupiter.api.Test;

import java.util.List;

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
        Bits bits = Bits.fromBin("10101010");
        Bits range = bits.get(1, 6);
        assertEquals(Bits.fromBin("01010"), range);
    }

    @Test
    public void testConcat() {
        Bits bits1 = Bits.fromBin("1010");
        Bits bits2 = Bits.fromBin("1100");
        Bits concatenated = bits1.concat(bits2);
        assertEquals(Bits.fromBin("10101100"), concatenated);
    }

    @Test
    public void testXor() {
        Bits bits1 = Bits.fromBin("1010");
        Bits bits2 = Bits.fromBin("1100");
        bits1.xor(bits2);
        assertEquals(Bits.fromBin("0110"), bits1);
    }

    @Test
    public void testOr() {
        Bits bits1 = Bits.fromBin("1010");
        Bits bits2 = Bits.fromBin("1100");
        bits1.or(bits2);
        assertEquals(Bits.fromBin("1110"), bits1);
    }

    @Test
    public void testCircularShiftLeft() {
        Bits bits = Bits.fromBin("1001101000");
        bits.circularShiftLeft(0);
        assertEquals(Bits.fromBin("1001101000"), bits);
        bits.circularShiftLeft(1);
        assertEquals(Bits.fromBin("0011010001"), bits);
        bits.circularShiftLeft(2);
        assertEquals(Bits.fromBin("1101000100"), bits);
        bits.circularShiftLeft(3);
        assertEquals(Bits.fromBin("1000100110"), bits);
        bits.circularShiftLeft(4);
        assertEquals(Bits.fromBin("1001101000"), bits);
        bits.circularShiftLeft(5);
        assertEquals(Bits.fromBin("0100010011"), bits);
    }

    @Test
    public void testSwapHalves() {
        Bits bits = Bits.fromBin("11100001");
        bits.swapHalves();
        assertEquals(Bits.fromBin("00011110"), bits);
    }

    @Test
    public void testToString() {
        Bits bits = Bits.fromBin("11001100");
        String expected = "Bits{ Data: {0, 1, 4, 5}\t Binary: 11001100\t Hex: CC }";
        assertEquals(expected, bits.toString());
    }

    @Test
    public void testFromTxt() {
        String text = "hello";
        Bits bits = Bits.fromTxt(text);
        assertEquals(Bits.fromBin("0110100001100101011011000110110001101111"), bits);
    }

    @Test
    public void testToTxt() {
        Bits textBits = Bits.fromTxt("hello");
        String resultText = textBits.toTxt();
        assertEquals("hello", resultText);
    }

    @Test
    public void testEmpty() {
        Bits emptyBits = Bits.empty();
        assertEquals(0, emptyBits.size());
    }

    @Test
    public void testTrim() {
        Bits paddedBits = Bits.fromBin("10000001000000110000001100000011");
        Bits trimmedBits = paddedBits.trim();
        assertEquals(Bits.fromBin("10000001"), trimmedBits);
    }

    @Test
    public void testPad() {
        Bits bits = Bits.fromBin("10000001");
        Bits paddedBits = bits.pad(32);
        assertEquals(Bits.fromBin("10000001" + "000000110000001100000011"), paddedBits);
    }

    @Test
    public void testSplit() {
        Bits bits = Bits.fromBin("01100001011000100110001101110100");
        List<Bits> blocks = bits.split(8);
        assertEquals(4, blocks.size());
        assertEquals(Bits.fromBin("01100001"), blocks.get(0));
        assertEquals(Bits.fromBin("01100010"), blocks.get(1));
        assertEquals(Bits.fromBin("01100011"), blocks.get(2));
        assertEquals(Bits.fromBin("01110100"), blocks.get(3));
    }

    @Test
    public void testSwap() {
        Bits bits = Bits.fromBin("0000111100000000");
        bits.swap(4, 8, 12, 16);
        assertEquals(Bits.fromBin("0000000000001111"), bits);
    }

    @Test
    public void testSwapInvalidRange() {
        Bits bits = Bits.fromBin("1100110011001100");
        assertThrows(IllegalArgumentException.class, () -> bits.swap(0, 4, 8, 9));
    }

    @Test
    public void testFromBinary() {
        Bits bits = Bits.fromBin("101010");
        assertEquals(Bits.fromBin("101010"), bits);
    }

    @Test
    public void testToBinaryString() {
        Bits bits = Bits.fromBin("11110000");
        assertEquals("11110000", bits.toBinString());
    }

    @Test
    public void testFromHex() {
        Bits bits = Bits.fromHex("CF646E7170632D45");
//        assertEquals(Bits.fromBin("00111010"), bits);
        assertEquals(64, bits.size());
    }

    @Test
    public void testToHexString() {
        Bits bits = Bits.fromBin("10101010");
        assertEquals("AA", bits.toHexString());
    }

    @Test
    public void testFromByteArray() {
        byte[] bytes = {0x01, 0x23, 0x45, 0x67};
        Bits bits = Bits.fromByteArray(bytes);
        assertEquals(Bits.fromBin("00000001001000110100010101100111"), bits);
        assertEquals(bytes.length * 8 , bits.size());
    }

    @Test
    public void testFromByteArrayEmpty() {
        byte[] bytes = {};
        Bits bits = Bits.fromByteArray(bytes);
        assertEquals(Bits.empty(), bits);
    }
}