package test;

import main.implementations.Bits;
import main.implementations.FinalPBox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FinalPBoxTest {

    FinalPBox pBox = new FinalPBox();

    @Test
    public void testPermuteWithAllBitsSet() {
        Bits testData = new Bits(64);
        testData.set(0, 64);

        Bits expectedResult = new Bits(64);
        expectedResult.set(0, 64);

        Bits result = pBox.permute(testData);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testPermuteWithNoBitsSet() {
        Bits testData = new Bits(64);

        Bits expectedResult = new Bits(64);

        Bits result = pBox.permute(testData);
        assertEquals(expectedResult, result);
    }
}