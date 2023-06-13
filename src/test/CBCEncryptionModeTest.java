package test;

import main.abstractions.*;
import main.implementations.*;
import main.tables.DESTables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CBCEncryptionModeTest {
    static int[][] SUBSTITUTION_TABLES = DESTables.DES_SUBSTITUTION_TABLES;

    private EncryptionMode encryptionMode;


    @BeforeEach
    public void setUp() {
        SBox[] sBoxes = Arrays.stream(SUBSTITUTION_TABLES).map(SBoxImpl::new).toArray(SBox[]::new);

        Mixer mixer = new MixerImp(new ExpansionPBox(), new StraightPBox(), sBoxes);
        PBox initialPBox = new InitialPBox();
        PBox finalPBox = new FinalPBox();
        KeyGenerator keyGenerator = new DESKeyGenerator(new ParityDropPBox(), new CompressionPBox());

        DESEncryptor desEncryptor = new DESEncryptor(mixer, initialPBox, finalPBox, keyGenerator);

        encryptionMode = new CBCEncryptionMode(desEncryptor);

    }

    @Test
    public void testEncryptDecrypt() {
        var plaintext = Bits.fromText("This is a test plaintext");
        var key = Bits.fromText("AAAAAAAA");
        var iv = Bits.fromText("BBBBBBBD");

        var ciphertext = encryptionMode.encrypt(plaintext, key, iv);
        var decrypted = encryptionMode.decrypt(ciphertext, key, iv);

        assertEquals(plaintext, decrypted);
    }

}
