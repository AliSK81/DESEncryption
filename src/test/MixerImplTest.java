package test;

import main.abstractions.SBox;
import main.implementations.*;
import main.tables.DESTables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class MixerImplTest {

    static int[][] SUBSTITUTION_TABLES = DESTables.DES_SUBSTITUTION_TABLES;
    private MixerImp mixer;

    @BeforeEach
    public void setup() {
        SBox[] sBoxes = Arrays.stream(SUBSTITUTION_TABLES).map(SBoxImpl::new).toArray(SBox[]::new);
        mixer = new MixerImp(new ExpansionPBox(), new StraightPBox(), sBoxes);
    }

    @Test
    public void testMix() {
        Bits exceptedResult = new Bits("0101101001111000111000111001010000011000110010100001100010101101");

        Bits data = new Bits("0001010010100111110101100111100000011000110010100001100010101101");
        Bits subKey = new Bits("000110010100110011010000011100101101111010001100");

        Bits result = mixer.mix(data, subKey);
        assertEquals(exceptedResult, result);
    }

    @Test
    public void testMixInvalidSubKeySize() {
        Bits data = new Bits("0001010010100111110101100111100000011000110010100001100010101101");
        Bits subKey = new Bits("111");
        assertThrows(IllegalArgumentException.class, () -> mixer.mix(data, subKey));
    }

    @Test
    public void testMixInvalidBlockSize() {
        Bits data = new Bits("111");
        Bits subKey = new Bits("000110010100110011010000011100101101111010001100");
        assertThrows(IllegalArgumentException.class, () -> mixer.mix(data, subKey));
    }

}