package test;

import main.implementations.Bits;
import main.implementations.CompressionPBox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CompressionPBoxTest {

    CompressionPBox pBox = new CompressionPBox();

    @Test
    public void testPermuteWithAllBitsSet() {
        Bits testData = new Bits(56);
        testData.set(0, 56);

        Bits expectedResult = new Bits(48);
        expectedResult.set(0, 48);

        Bits result = pBox.permute(testData);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testPermuteWithNoBitsSet() {
        Bits testData = new Bits(56);

        Bits expectedResult = new Bits(48);

        Bits result = pBox.permute(testData);
        assertEquals(expectedResult, result);
    }
}