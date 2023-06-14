package test.des;

import main.implementations.Bits;
import main.implementations.des.DESInitialPBox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DESInitialPBoxTest {

    DESInitialPBox pBox = new DESInitialPBox();

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