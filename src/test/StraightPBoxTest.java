package test;

import main.implementations.Bits;
import main.implementations.StraightPBox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StraightPBoxTest {

    StraightPBox pBox = new StraightPBox();

    @Test
    public void testPermuteWithAllBitsSet() {
        Bits testData = new Bits(32);
        testData.set(0, 32);

        Bits expectedResult = new Bits(32);
        expectedResult.set(0, 32);

        Bits result = pBox.permute(testData);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testPermuteWithNoBitsSet() {
        Bits testData = new Bits(32);

        Bits expectedResult = new Bits(32);

        Bits result = pBox.permute(testData);
        assertEquals(expectedResult, result);
    }
}