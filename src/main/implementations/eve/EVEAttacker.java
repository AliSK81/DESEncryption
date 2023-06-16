package main.implementations.eve;

import main.abstractions.*;
import main.implementations.Bits;
import main.implementations.des.*;
import main.implementations.mode.ECBEncryptionMode;
import main.tables.DESTables;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EVEAttacker {
    private final static int BLOCK_SIZE = 64;
    static int[][] SUBSTITUTION_TABLES = DESTables.SUBSTITUTION_TABLES;
    private final Encryptor encryptor;
    PBox initialPBox = new EVEInitialPBox();
    PBox finalReversedPBox = new EVEInitialPBox();

    public EVEAttacker() {
        SBox[] sBoxes = Arrays.stream(SUBSTITUTION_TABLES).map(SBoxImpl::new).toArray(SBox[]::new);

        Mixer mixer = new DESMixer(new DESExpansionPBox(), new EVEIdentityStraightPBox(), sBoxes);
        PBox initialPBox = new EVEInitialPBox();
        PBox finalPBox = new EVEIdentityFinalPBox();
        KeyGenerator keyGenerator = new DESKeyGenerator(new DESParityDropPBox(), new DESCompressionPBox());

        encryptor = new DESEncryptor(mixer, initialPBox, finalPBox, keyGenerator, 1);
    }

    public int[] attack(Map<String, String> plaintextToCiphertext, Bits key) {

        Map<String, String> outputByInput = new HashMap<>();

        for (Map.Entry<String, String> entry : plaintextToCiphertext.entrySet()) {
            Bits plainText = Bits.fromTxt(entry.getKey());
            Bits cipherText = Bits.fromHex(entry.getValue());

            Bits paddedPlaintext = plainText.pad(BLOCK_SIZE);

            List<Bits> plainBlocks = paddedPlaintext.split(BLOCK_SIZE);
            List<Bits> cipherBlocks = cipherText.split(BLOCK_SIZE);

            for (int i = 0; i < plainBlocks.size(); i++) {

                Bits plainBlock = plainBlocks.get(i);
                Bits exceptedCipherBlock = cipherBlocks.get(i);

                Bits actualCipherBlock = encryptor.encrypt(plainBlock, key);

                Bits leftPermuted = initialPBox.permute(plainBlock).getFirstHalf();

                // find straightPBox input
                Bits straightPBoxInput = leftPermuted.copy();
                straightPBoxInput.xor(actualCipherBlock.getFirstHalf());

                // find straightPBox output
                Bits straightPBoxOutput = finalReversedPBox.permute(exceptedCipherBlock).getFirstHalf();
                straightPBoxOutput.xor(leftPermuted);

                outputByInput.put(straightPBoxInput.toBinString(), straightPBoxOutput.toBinString());
            }
        }

        var possiblePBoxes = PBoxFinder.findPossiblePBoxes(outputByInput);

        for (int[] pBox : possiblePBoxes) {
            SBox[] sBoxes = Arrays.stream(SUBSTITUTION_TABLES).map(SBoxImpl::new).toArray(SBox[]::new);

            Mixer mixer = new DESMixer(new DESExpansionPBox(), new AbstractPBox(pBox), sBoxes);
            PBox initialPBox = new EVEInitialPBox();
            PBox finalPBox = new EVEFinalPBox();
            KeyGenerator keyGenerator = new DESKeyGenerator(new DESParityDropPBox(), new DESCompressionPBox());

            Encryptor encryptor = new DESEncryptor(mixer, initialPBox, finalPBox, keyGenerator, 1);
            var mode = new ECBEncryptionMode(encryptor);

            Map.Entry<String, String> firstEntry = plaintextToCiphertext.entrySet().iterator().next();

            Bits ciphertext = mode.encrypt(Bits.fromTxt(firstEntry.getKey()), key, null);
            if (ciphertext.equals(Bits.fromHex(firstEntry.getValue()))) {
                return pBox;
            }
        }

        return null;
    }
}
