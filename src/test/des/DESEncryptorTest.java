package test.des;

import main.abstractions.KeyGenerator;
import main.abstractions.Mixer;
import main.abstractions.PBox;
import main.abstractions.SBox;
import main.implementations.*;
import main.implementations.des.*;
import main.tables.DESTables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DESEncryptorTest {

    static int[][] SUBSTITUTION_TABLES = DESTables.SUBSTITUTION_TABLES;

    private DESEncryptor desEncryptor;

    @BeforeEach
    public void setUp() {
        SBox[] sBoxes = Arrays.stream(SUBSTITUTION_TABLES).map(SBoxImpl::new).toArray(SBox[]::new);

        Mixer mixer = new DESMixer(new DESExpansionPBox(), new DESStraightPBox(), sBoxes);
        PBox initialPBox = new DESInitialPBox();
        PBox finalPBox = new DESFinalPBox();
        KeyGenerator keyGenerator = new DESKeyGenerator(new DESParityDropPBox(), new DESCompressionPBox());

        desEncryptor = new DESEncryptor(mixer, initialPBox, finalPBox, keyGenerator);
    }

    @Test
    public void testEncrypt() {
        var expectedCiphertext = Bits.fromBin("1100000010110111101010001101000001011111001110101000001010011100"); // C0B7A8D05F3A829C

        var plaintext = Bits.fromBin("0001001000110100010101101010101111001101000100110010010100110110"); // 123456ABCD132536
        var key = Bits.fromBin("1010101010111011000010010001100000100111001101101100110011011101"); // AABB09182736CCDD
        var ciphertext = desEncryptor.encrypt(plaintext, key);

        assertEquals(expectedCiphertext, ciphertext);
    }

    @Test
    public void testDecrypt() {
        var expectedPlaintext = Bits.fromBin("0001001000110100010101101010101111001101000100110010010100110110"); // 123456ABCD132536

        var ciphertext = Bits.fromBin("1100000010110111101010001101000001011111001110101000001010011100"); // C0B7A8D05F3A829C
        var key = Bits.fromBin("1010101010111011000010010001100000100111001101101100110011011101"); // AABB09182736CCDD
        var plaintext = desEncryptor.decrypt(ciphertext, key);

        assertEquals(expectedPlaintext, plaintext);
    }

    @Test
    public void testEncryptInvalidBlockSize() {
        var plaintext = Bits.fromBin("111");
        var key = Bits.fromBin("1010101010111011000010010001100000100111001101101100110011011101");
        assertThrows(IllegalArgumentException.class, () -> desEncryptor.encrypt(plaintext, key));
    }

    @Test
    public void testEncryptInvalidKeySize() {
        var plaintext = Bits.fromBin("0001001000110100010101101010101111001101000100110010010100110110");
        var key = Bits.fromBin("111");
        assertThrows(IllegalArgumentException.class, () -> desEncryptor.encrypt(plaintext, key));
    }

    @Test
    public void testDecryptInvalidBlockSize() {
        var ciphertext = Bits.fromBin("111");
        var key = Bits.fromBin("1010101010111011000010010001100000100111001101101100110011011101");
        assertThrows(IllegalArgumentException.class, () -> desEncryptor.decrypt(ciphertext, key));
    }

    @Test
    public void testDecryptInvalidKeySize() {
        var ciphertext = Bits.fromBin("1100000010110111101010001101000001011111001110101000001010011100");
        var key = Bits.fromBin("111");
        assertThrows(IllegalArgumentException.class, () -> desEncryptor.decrypt(ciphertext, key));
    }
}
