package test.des;

import main.abstractions.SBox;
import main.implementations.Bits;
import main.implementations.des.DESExpansionPBox;
import main.implementations.des.DESMixer;
import main.implementations.des.DESStraightPBox;
import main.implementations.des.SBoxImpl;
import main.tables.DESTables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DESMixerImplTest {

    static int[][] SUBSTITUTION_TABLES = DESTables.SUBSTITUTION_TABLES;
    private DESMixer mixer;

    @BeforeEach
    public void setup() {
        SBox[] sBoxes = Arrays.stream(SUBSTITUTION_TABLES).map(SBoxImpl::new).toArray(SBox[]::new);
        mixer = new DESMixer(new DESExpansionPBox(), new DESStraightPBox(), sBoxes);
    }

    @Test
    public void testMix() {
        Bits exceptedResult = Bits.fromBin("0101101001111000111000111001010000011000110010100001100010101101");

        Bits data = Bits.fromBin("0001010010100111110101100111100000011000110010100001100010101101");
        Bits subKey = Bits.fromBin("000110010100110011010000011100101101111010001100");

        Bits result = mixer.mix(data, subKey);
        assertEquals(exceptedResult, result);
    }

    @Test
    public void testMixInvalidSubKeySize() {
        Bits data = Bits.fromBin("0001010010100111110101100111100000011000110010100001100010101101");
        Bits subKey = Bits.fromBin("111");
        assertThrows(IllegalArgumentException.class, () -> mixer.mix(data, subKey));
    }

    @Test
    public void testMixInvalidBlockSize() {
        Bits data = Bits.fromBin("111");
        Bits subKey = Bits.fromBin("000110010100110011010000011100101101111010001100");
        assertThrows(IllegalArgumentException.class, () -> mixer.mix(data, subKey));
    }

}