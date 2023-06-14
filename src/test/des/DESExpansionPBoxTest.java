package test.des;


import main.implementations.Bits;
import main.implementations.des.DESExpansionPBox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DESExpansionPBoxTest {

    DESExpansionPBox pBox = new DESExpansionPBox();

    @Test
    public void testPermuteWithAllBitsSet() {
        Bits testData = new Bits(32);
        testData.set(0, 32);

        Bits expectedResult = new Bits(48);
        expectedResult.set(0, 48);

        Bits result = pBox.permute(testData);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testPermuteWithNoBitsSet() {
        Bits testData = new Bits(32);

        Bits expectedResult = new Bits(48);

        Bits result = pBox.permute(testData);
        assertEquals(expectedResult, result);
    }
}