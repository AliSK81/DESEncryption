package test;

import main.implementations.Bits;
import main.implementations.ParityDropPBox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParityDropPBoxTest {

    ParityDropPBox pBox = new ParityDropPBox();

    @Test
    public void testPermute() {
        Bits testData = new Bits(64);
        testData.set(56);
        testData.set(40);
        testData.set(32);
        testData.set(24);
        testData.set(8);
        testData.set(3);

        Bits expectedResult = new Bits(56);
        expectedResult.set(0);
        expectedResult.set(2);
        expectedResult.set(3);
        expectedResult.set(4);
        expectedResult.set(6);
        expectedResult.set(55);

        Bits result = pBox.permute(testData);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testPermuteWithAllBitsSet() {
        Bits testData = new Bits(64);
        testData.set(0, 64);

        Bits expectedResult = new Bits(56);
        expectedResult.set(0, 56);

        Bits result = pBox.permute(testData);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testPermuteWithNoBitsSet() {
        Bits testData = new Bits(64);

        Bits expectedResult = new Bits(56);

        Bits result = pBox.permute(testData);
        assertEquals(expectedResult, result);
    }
}