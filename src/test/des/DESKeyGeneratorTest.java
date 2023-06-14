package test.des;

import main.abstractions.KeyGenerator;
import main.abstractions.PBox;
import main.implementations.Bits;
import main.implementations.des.DESCompressionPBox;
import main.implementations.des.DESKeyGenerator;
import main.implementations.des.DESParityDropPBox;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DESKeyGeneratorTest {
    private final PBox parityDropPBox = new DESParityDropPBox();

    private final PBox compressionPBox = new DESCompressionPBox();


    @Test
    public void testGenerateSubKeys() {
        Bits key = Bits.fromBin("1010101010111011000010010001100000100111001101101100110011011101");


        KeyGenerator keyGenerator = new DESKeyGenerator(parityDropPBox, compressionPBox);
        Bits[] generateSubKeys = keyGenerator.generateSubKeys(key);

        assertEquals(16, generateSubKeys.length);

        Bits[] expectedSubKeys = new Bits[]{
                Bits.fromBin("000110010100110011010000011100101101111010001100"),
                Bits.fromBin("010001010110100001011000000110101011110011001110"),
                Bits.fromBin("000001101110110110100100101011001111010110110101"),
                Bits.fromBin("110110100010110100000011001010110110111011100011"),
                Bits.fromBin("011010011010011000101001111111101100100100010011"),
                Bits.fromBin("110000011001010010001110100001110100011101011110"),
                Bits.fromBin("011100001000101011010010110111011011001111000000"),
                Bits.fromBin("001101001111100000100010111100001100011001101101"),
                Bits.fromBin("100001001011101101000100011100111101110011001100"),
                Bits.fromBin("000000100111011001010111000010001011010110111111"),
                Bits.fromBin("011011010101010101100000101011110111110010100101"),
                Bits.fromBin("110000101100000111101001011010100100101111110011"),
                Bits.fromBin("100110011100001100010011100101111100100100011111"),
                Bits.fromBin("001001010001101110001011110001110001011111010000"),
                Bits.fromBin("001100110011000011000101110110011010001101101101"),
                Bits.fromBin("000110000001110001011101011101011100011001101101"),
        };

        assertArrayEquals(expectedSubKeys, generateSubKeys);
    }
}