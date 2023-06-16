package main.implementations.eve;

import main.abstractions.*;
import main.implementations.Bits;
import main.implementations.des.DESEncryptor;
import main.implementations.des.DESMixer;
import main.implementations.des.PBoxImpl;
import main.implementations.des.SBoxImpl;
import main.implementations.mode.ECBEncryptionMode;
import main.tables.DESTables;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EVEAttacker {
    private final static int BLOCK_SIZE = 64;
    private final PBox initialPBox;
    private final PBox finalPBox;
    private final PBox identityFinalPBox;
    private final PBox identityStraightPBox;
    private final PBox expansionPBox;
    private final KeyGenerator keyGenerator;
    private final SBox[] sBoxes;

    public EVEAttacker(PBox initialPBox, PBox finalPBox, PBox identityFinalPBox, PBox identityStraightPBox, PBox expansionPBox, KeyGenerator keyGenerator) {
        this.initialPBox = initialPBox;
        this.finalPBox = finalPBox;
        this.identityFinalPBox = identityFinalPBox;
        this.identityStraightPBox = identityStraightPBox;
        this.expansionPBox = expansionPBox;
        this.keyGenerator = keyGenerator;
        sBoxes = Arrays.stream(DESTables.SUBSTITUTION_TABLES).map(SBoxImpl::new).toArray(SBox[]::new);
    }

    public int[] attack(Map<String, String> cipherByPlainText, Bits key) {
        Map<String, String> outputByInput = generateOutputByInputMap(cipherByPlainText, key);

        List<int[]> possiblePBoxes = findPossiblePBoxes(outputByInput);

        Map.Entry<String, String> firstEntry = cipherByPlainText.entrySet().iterator().next();
        Bits targetPlaintext = Bits.fromTxt(firstEntry.getKey());
        Bits targetCiphertext = Bits.fromHex(firstEntry.getValue());

        return possiblePBoxes.stream()
                .filter(pBox -> isTargetPBox(pBox, key, targetPlaintext, targetCiphertext))
                .findAny().orElseThrow(AttackFailureException::new);
    }

    private boolean isTargetPBox(int[] pBox, Bits key, Bits targetPlaintext, Bits targetCiphertext) {
        Encryptor encryptor = createEncryptorWithStraightPBox(pBox);
        EncryptionMode mode = new ECBEncryptionMode(encryptor);

        Bits ciphertext = mode.encrypt(targetPlaintext, key, null);

        return ciphertext.equals(targetCiphertext);
    }

    private Map<String, String> generateOutputByInputMap(Map<String, String> cipherByPlainText, Bits key) {

        Mixer mixer = new DESMixer(expansionPBox, identityStraightPBox, sBoxes);
        Encryptor encryptor = new DESEncryptor(mixer, initialPBox, identityFinalPBox, keyGenerator, 1);

        Map<String, String> outputByInput = new HashMap<>();

        for (Map.Entry<String, String> entry : cipherByPlainText.entrySet()) {
            List<Bits> plainBlocks = getPlaintextBlocks(Bits.fromTxt(entry.getKey()));
            List<Bits> cipherBlocks = getCiphertextBlocks(Bits.fromHex(entry.getValue()));

            for (int i = 0; i < plainBlocks.size(); i++) {
                Bits plainBlock = plainBlocks.get(i);
                Bits exceptedCipherBlock = cipherBlocks.get(i);

                Bits actualCipherBlock = encryptor.encrypt(plainBlock, key);

                Bits leftPermuted = initialPBox.permute(plainBlock).getFirstHalf();

                Bits straightPBoxInput = findStraightPBoxInput(leftPermuted, actualCipherBlock);
                Bits straightPBoxOutput = findStraightPBoxOutput(leftPermuted, exceptedCipherBlock);

                outputByInput.put(straightPBoxInput.toBinString(), straightPBoxOutput.toBinString());
            }
        }

        return outputByInput;
    }

    private List<Bits> getPlaintextBlocks(Bits plaintext) {
        Bits paddedPlaintext = plaintext.pad(BLOCK_SIZE);
        return paddedPlaintext.split(BLOCK_SIZE);
    }

    private List<Bits> getCiphertextBlocks(Bits ciphertext) {
        return ciphertext.split(BLOCK_SIZE);
    }

    private Bits findStraightPBoxInput(Bits leftPermuted, Bits ciphertextBloc) {
        Bits straightPBoxInput = leftPermuted.copy();
        straightPBoxInput.xor(ciphertextBloc.getFirstHalf());
        return straightPBoxInput;
    }

    private Bits findStraightPBoxOutput(Bits leftPermuted, Bits ciphertextBloc) {
        Bits straightPBoxOutput = initialPBox.permute(ciphertextBloc).getFirstHalf();
        straightPBoxOutput.xor(leftPermuted);
        return straightPBoxOutput;
    }

    private List<int[]> findPossiblePBoxes(Map<String, String> outputByInput) {
        return PBoxFinder.findPossiblePBoxes(outputByInput);
    }

    private Encryptor createEncryptorWithStraightPBox(int[] pBox) {
        Mixer mixer = new DESMixer(expansionPBox, new PBoxImpl(pBox), sBoxes);
        return new DESEncryptor(mixer, initialPBox, finalPBox, keyGenerator, 1);
    }
}