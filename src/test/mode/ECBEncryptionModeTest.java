package test.mode;

import main.abstractions.*;
import main.implementations.Bits;
import main.implementations.des.*;
import main.implementations.mode.ECBEncryptionMode;
import main.tables.DESTables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ECBEncryptionModeTest {
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

        encryptionMode = new ECBEncryptionMode(desEncryptor);

    }

    @Test
    public void testEncryptDecrypt() {
        var plaintext = Bits.fromTxt("This is a test plaintext");
        var key = Bits.fromTxt("AAAAAAAA");

        var ciphertext = encryptionMode.encrypt(plaintext, key, null);
        var decrypted = encryptionMode.decrypt(ciphertext, key, null);

        assertEquals(plaintext, decrypted);
    }

}
