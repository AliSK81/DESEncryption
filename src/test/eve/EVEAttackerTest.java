package test.eve;

import main.abstractions.*;
import main.implementations.Bits;
import main.implementations.des.*;
import main.implementations.eve.*;
import main.implementations.mode.ECBEncryptionMode;
import main.tables.DESTables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class EVEAttackerTest {
    private static final Map<String, String> cipherByPlainText = new HashMap<>();
    private final PBox initialPBox = new EVEInitialPBox();
    private final PBox finalPBox = new EVEFinalPBox();
    private final PBox identityFinalPBox = new EVEIdentityFinalPBox();
    private final PBox identityStraightPBox = new EVEIdentityStraightPBox();
    private final PBox expansionPBox = new DESExpansionPBox();
    private final KeyGenerator keyGenerator = new DESKeyGenerator(new DESParityDropPBox(), new DESCompressionPBox());
    private final EVEAttacker EVEAttacker = new EVEAttacker(initialPBox,
            finalPBox,
            identityFinalPBox,
            identityStraightPBox,
            expansionPBox,
            keyGenerator);
    private Bits key;
    private SBox[] sBoxes;


    @BeforeEach
    public void setUp() {
        sBoxes = Arrays.stream(DESTables.SUBSTITUTION_TABLES).map(SBoxImpl::new).toArray(SBox[]::new);

        cipherByPlainText.put("kootahe", "6E2F7B25307C3144");
        cipherByPlainText.put("Zendegi", "CF646E7170632D45");
        cipherByPlainText.put("Edame", "D070257820560746");
        cipherByPlainText.put("Dare", "5574223505051150");
        cipherByPlainText.put("JolotYe", "DB2E393F61586144");
        cipherByPlainText.put("Daame", "D175257820560746");
        cipherByPlainText.put("DaemKe", "D135603D1A705746");
        cipherByPlainText.put("Mioftan", "D83C6F7321752A54");
        cipherByPlainText.put("Toosh", "413A2B666D024747");
        cipherByPlainText.put("HattaMo", "5974216034186B44");
        cipherByPlainText.put("khayeSa", "EA29302D74463545");
        cipherByPlainText.put("05753jj", "B1203330722B7A04");
        cipherByPlainText.put("==j95697", "38693B6824232D231D1C0D0C4959590D");

        key = Bits.fromHex("4355262724562343");
    }

    @Test
    public void testAttack() {
        var actualPBox = EVEAttacker.attack(cipherByPlainText, key);

        int[] exceptedPBox = new int[]{
                6, 26, 20, 28, 29, 12, 21, 17,
                31, 15, 23, 10, 8, 18, 2, 16,
                7, 5, 24, 14, 19, 27, 1, 9,
                32, 13, 30, 4, 11, 22, 25, 3
        };

        assertArrayEquals(exceptedPBox, actualPBox);
    }

    @Test
    public void testDecryptAfterAttack() {
        var pBox = EVEAttacker.attack(cipherByPlainText, key);
        Mixer mixer = new DESMixer(expansionPBox, new PBoxImpl(pBox), sBoxes);
        Encryptor encryptor = new DESEncryptor(mixer, initialPBox, finalPBox, keyGenerator, 1);
        EncryptionMode mode = new ECBEncryptionMode(encryptor);
        Bits ciphertext = Bits.fromHex("59346E29456A723B62354B61756D44257871650320277C741D1C0D0C4959590D");
        Bits decrypted = mode.decrypt(ciphertext, key, null);
        assertEquals(Bits.fromTxt("HajiDorostZadiDametGarm!"), decrypted);
    }
}
