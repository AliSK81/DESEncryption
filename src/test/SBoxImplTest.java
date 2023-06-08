package test;

import main.implementations.Bits;
import main.implementations.SBoxImpl;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SBoxImplTest {

    private static final int[] SUBSTITUTION_TABLE = {
            14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
            0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
            4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
            15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13
    };

    private final SBoxImpl sBox = new SBoxImpl(SUBSTITUTION_TABLE);

    @Test
    public void testSubstitute() {
        Bits testData = new Bits("110100"); // S[2][8+2] => 9
        Bits expectedResult = new Bits("1001"); // 8 + 1 => 9
        Bits result = sBox.substitute(testData);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSubstituteWithAllBitsSet() {
        Bits testData = new Bits("111111"); // S[3][15] => 13
        Bits expectedResult = new Bits("1101"); // 8 + 4 + 1 => 13
        Bits result = sBox.substitute(testData);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testSubstituteWithNoBitsSet() {
        Bits testData = new Bits("000000"); // S[0][0] => 14
        Bits expectedResult = new Bits("1110"); //  8 + 4 + 2 => 14
        Bits result = sBox.substitute(testData);
        assertEquals(expectedResult, result);
    }
}