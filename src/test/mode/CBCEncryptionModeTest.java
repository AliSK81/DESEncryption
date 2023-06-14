package test.mode;

import main.abstractions.*;
import main.implementations.*;
import main.implementations.des.*;
import main.implementations.mode.CBCEncryptionMode;
import main.tables.DESTables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CBCEncryptionModeTest {
    static int[][] SUBSTITUTION_TABLES = DESTables.SUBSTITUTION_TABLES;

    private EncryptionMode encryptionMode;


    @BeforeEach
    public void setUp() {
        SBox[] sBoxes = Arrays.stream(SUBSTITUTION_TABLES).map(SBoxImpl::new).toArray(SBox[]::new);

        Mixer mixer = new DESMixer(new DESExpansionPBox(), new DESStraightPBox(), sBoxes);
        PBox initialPBox = new DESInitialPBox();
        PBox finalPBox = new DESFinalPBox();
        KeyGenerator keyGenerator = new DESKeyGenerator(new DESParityDropPBox(), new DESCompressionPBox());

        DESEncryptor desEncryptor = new DESEncryptor(mixer, initialPBox, finalPBox, keyGenerator);

        encryptionMode = new CBCEncryptionMode(desEncryptor);

    }

    @Test
    public void testEncryptDecrypt() {
        var plaintext = Bits.fromTxt("This is a test plaintext");
        var key = Bits.fromTxt("AAAAAAAA");
        var iv = Bits.fromTxt("BBBBBBBD");

        var ciphertext = encryptionMode.encrypt(plaintext, key, iv);
        var decrypted = encryptionMode.decrypt(ciphertext, key, iv);

        assertEquals(plaintext, decrypted);
    }

}
