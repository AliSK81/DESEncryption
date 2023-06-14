package main.implementations.custom;

import main.abstractions.*;
import main.implementations.*;
import main.implementations.custom.CustomFinalPBox;
import main.implementations.custom.CustomInitialPBox;
import main.implementations.custom.CustomStraightPBox;
import main.implementations.des.*;
import main.implementations.mode.ECBEncryptionMode;
import main.tables.DESTables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Attack {
    static int[][] SUBSTITUTION_TABLES = DESTables.SUBSTITUTION_TABLES;

    private static final Map<String, String> plaintextToCiphertext = new HashMap<>();

    private final EncryptionMode encryptionMode;

    public Attack() {
        plaintextToCiphertext.put("kootahe", "6E2F7B25307C3144");
        plaintextToCiphertext.put("Zendegi", "CF646E7170632D45");
        plaintextToCiphertext.put("Edame", "D070257820560746");
        plaintextToCiphertext.put("Dare", "5574223505051150");
        plaintextToCiphertext.put("JolotYe", "DB2E393F61586144");
        plaintextToCiphertext.put("Daame", "D175257820560746");
        plaintextToCiphertext.put("DaemKe", "D135603D1A705746");
        plaintextToCiphertext.put("Mioftan", "D83C6F7321752A54");
        plaintextToCiphertext.put("Toosh", "413A2B666D024747");
        plaintextToCiphertext.put("HattaMo", "5974216034186B44");
        plaintextToCiphertext.put("khayeSa", "EA29302D74463545");
        plaintextToCiphertext.put("05753jj", "B1203330722B7A04");
        plaintextToCiphertext.put("==j95697", "38693B6824232D231D1C0D0C4959590D");

        SBox[] sBoxes = Arrays.stream(SUBSTITUTION_TABLES).map(SBoxImpl::new).toArray(SBox[]::new);

        Mixer mixer = new DESMixer(new DESExpansionPBox(), new CustomStraightPBox(), sBoxes);
        PBox initialPBox = new CustomInitialPBox();
        PBox finalPBox = new CustomFinalIdentityPBox();
        KeyGenerator keyGenerator = new DESKeyGenerator(new DESParityDropPBox(), new DESCompressionPBox());

        DESEncryptor desEncryptor = new DESEncryptor(mixer, initialPBox, finalPBox, keyGenerator, 1);

        encryptionMode = new ECBEncryptionMode(desEncryptor);
    }

    public void attack() {
        var key = Bits.fromHex("4355262724562343");

        for (Map.Entry<String, String> entry: plaintextToCiphertext.entrySet()) {
            Bits plainText = Bits.fromTxt(entry.getKey());
            Bits cipherText = Bits.fromTxt(entry.getValue());
            var actualCiphertext = encryptionMode.encrypt(plainText, key, null);

            var leftHalfPlain = plainText.getFirstHalf();
            var leftHalfCipher = actualCiphertext.getFirstHalf();
            leftHalfPlain.xor(leftHalfCipher);
        }
    }
}
